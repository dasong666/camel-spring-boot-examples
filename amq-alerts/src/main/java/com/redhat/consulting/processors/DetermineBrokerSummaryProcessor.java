package com.redhat.consulting.processors;

import org.apache.activemq.artemis.api.core.management.ActiveMQServerControl;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class DetermineBrokerSummaryProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		ActiveMQServerControl serverControl = exchange.getIn().getBody(ActiveMQServerControl.class);

		exchange.getIn().setHeader("summaryBackup", serverControl.isBackup());
		exchange.getIn().setHeader("totalMessagesAdded", serverControl.getTotalMessagesAdded());
		exchange.getIn().setHeader("totalMessagesAckd", serverControl.getTotalMessagesAcknowledged());
		exchange.getIn().setHeader("uptime", serverControl.getUptime());
		exchange.getIn().setHeader("diskStoreUsage", serverControl.getDiskStoreUsage());
		exchange.getIn().setHeader("connections", serverControl.getConnectionCount());
		exchange.getIn().setHeader("totalConnections", serverControl.getTotalConnectionCount());

	}

}
