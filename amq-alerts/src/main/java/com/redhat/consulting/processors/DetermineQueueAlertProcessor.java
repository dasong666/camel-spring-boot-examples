package com.redhat.consulting.processors;

import org.apache.activemq.artemis.api.core.management.QueueControl;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.redhat.consulting.config.AddressAlertThresholds;

public class DetermineQueueAlertProcessor implements Processor {

	private AddressAlertThresholds addressAlertThresholds = new AddressAlertThresholds();

	public DetermineQueueAlertProcessor(AddressAlertThresholds addressAlertThresholds) {
		this.addressAlertThresholds = addressAlertThresholds;
	}

	@Override
	public void process(Exchange exchange) throws Exception {

		QueueControl queueControl = exchange.getIn().getBody(QueueControl.class);

		exchange.getIn().setHeader("messageCountAlert", false);
		exchange.getIn().setHeader("consumerCountAlert", false);

		if (queueControl.getMessageCount() > addressAlertThresholds.getMessageCount()) {
			exchange.getIn().setHeader("queueAlertExists", true);
			exchange.getIn().setHeader("messageCountAlert", true);
			exchange.getIn().setHeader("messageCountCurrent", queueControl.getMessageCount());
			exchange.getIn().setHeader("messageCountThreshold", addressAlertThresholds.getMessageCount());
		}

		if (queueControl.getConsumerCount() > addressAlertThresholds.getConsumers()) {
			exchange.getIn().setHeader("queueAlertExists", true);
			exchange.getIn().setHeader("consumerCountAlert", true);
			exchange.getIn().setHeader("consumerCountCurrent", queueControl.getConsumerCount());
			exchange.getIn().setHeader("consumerCountThreshold", addressAlertThresholds.getConsumers());
		}

	}

}
