package com.redhat.consulting.processors;

import org.apache.activemq.artemis.api.core.management.ActiveMQServerControl;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class DetermineQueueAlertProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		ActiveMQServerControl serverControl = exchange.getIn().getBody(ActiveMQServerControl.class);

	}

}
