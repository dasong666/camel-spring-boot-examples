package com.redhat.consulting.processors;

import org.apache.activemq.artemis.api.core.management.ActiveMQServerControl;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.redhat.consulting.config.BrokerAlertThresholds;

public class DetermineBrokerAlertProcessor implements Processor {

	private BrokerAlertThresholds brokerAlertThresholds;

	public DetermineBrokerAlertProcessor(BrokerAlertThresholds brokerAlertThresholds) {
		this.brokerAlertThresholds = brokerAlertThresholds;
	}

	@Override
	public void process(Exchange exchange) throws Exception {

		ActiveMQServerControl serverControl = exchange.getIn().getBody(ActiveMQServerControl.class);

		final String brokerRole = exchange.getIn().getHeader("brokerRole", String.class);
		final boolean isClustered = exchange.getIn().getHeader("isClustered", boolean.class);

		exchange.getIn().setHeader("brokerRoleAlert", false);
		exchange.getIn().setHeader("clusteredAlert", false);
		exchange.getIn().setHeader("connectionsAlert", false);
		exchange.getIn().setHeader("totalConnectionsAlert", false);
		exchange.getIn().setHeader("diskStorageAlert", false);
		exchange.getIn().setHeader("addressMemoryPercentageAlert", false);

		// Is Active Alert
		if (serverControl.isBackup() != brokerRole.equalsIgnoreCase("passive")) {
			exchange.getIn().setHeader("alertExists", true);
			exchange.getIn().setHeader("brokerRoleAlert", true);
			if (serverControl.isBackup()) {
				exchange.getIn().setHeader("brokerRoleState", "passive");
			} else {
				exchange.getIn().setHeader("brokerRoleState", "active");
			}
		}

		// Is Clustered Alert
		if (serverControl.isClustered() != isClustered) {
			exchange.getIn().setHeader("alertExists", true);
			exchange.getIn().setHeader("clusteredAlert", true);
			exchange.getIn().setHeader("clusteredState", serverControl.isClustered());
		}

		// Connections Alert
		if (serverControl.getConnectionCount() > brokerAlertThresholds.getConnections()) {
			exchange.getIn().setHeader("alertExists", true);
			exchange.getIn().setHeader("connectionsAlert", true);
			exchange.getIn().setHeader("connectionsCurrent", serverControl.getConnectionCount());
			exchange.getIn().setHeader("connectionsThreshold", brokerAlertThresholds.getConnections());
		}

		if (serverControl.getTotalConnectionCount() > brokerAlertThresholds.getTotalConnections()) {
			exchange.getIn().setHeader("alertExists", true);
			exchange.getIn().setHeader("totalConnectionsAlert", true);
			exchange.getIn().setHeader("totalConnectionsCurrent", serverControl.getTotalConnectionCount());
			exchange.getIn().setHeader("totalConnectionsThreshold", brokerAlertThresholds.getTotalConnections());
		}

		if (serverControl.getDiskStoreUsage() > brokerAlertThresholds.getDiskStoreUsage()) {
			exchange.getIn().setHeader("alertExists", true);
			exchange.getIn().setHeader("diskStorageAlert", true);
			exchange.getIn().setHeader("diskStorageCurrent", serverControl.getDiskStoreUsage());
			exchange.getIn().setHeader("diskStorageThreshold", brokerAlertThresholds.getDiskStoreUsage());
		}

		if (serverControl.getAddressMemoryUsagePercentage() > brokerAlertThresholds.getAddressMemoryPercentage()) {
			exchange.getIn().setHeader("alertExists", true);
			exchange.getIn().setHeader("addressMemoryPercentageAlert", true);
			exchange.getIn().setHeader("addressMemoryPercentageCurrent",
					serverControl.getAddressMemoryUsagePercentage());
			exchange.getIn().setHeader("addressMemoryPercentageThreshold",
					brokerAlertThresholds.getAddressMemoryPercentage());
		}

	}

}
