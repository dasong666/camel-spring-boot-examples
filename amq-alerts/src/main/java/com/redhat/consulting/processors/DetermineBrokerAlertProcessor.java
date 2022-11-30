package com.redhat.consulting.processors;

import org.apache.activemq.artemis.api.core.management.ActiveMQServerControl;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class DetermineBrokerAlertProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		ActiveMQServerControl serverControl = exchange.getIn().getBody(ActiveMQServerControl.class);

		final String brokerRole = exchange.getIn().getHeader("brokerRole", String.class);
		final boolean isClustered = exchange.getIn().getHeader("isClustered", boolean.class);

		exchange.getIn().setHeader("alertExists", false);
		exchange.getIn().setHeader("brokerRoleAlert", false);
		exchange.getIn().setHeader("clusteredAlert", false);

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

	}

}
