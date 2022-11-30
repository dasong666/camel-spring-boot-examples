package com.redhat.consulting.processors;

import org.apache.activemq.artemis.api.core.management.QueueControl;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class DetermineQueueSummaryProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		QueueControl queueControl = exchange.getIn().getBody(QueueControl.class);

		QueueSummary queueSummary = new QueueSummary();
		queueSummary.setQueueName(exchange.getIn().getHeader("queueName", String.class));
		queueSummary.setConsumerCount(queueControl.getConsumerCount());
		queueSummary.setMessageAdded(queueControl.getMessagesAdded());
		queueSummary.setMessageAcknowledged(queueControl.getMessagesAcknowledged());
		queueSummary.setMessageCount(queueControl.getMessageCount());

		exchange.getIn().setBody(queueSummary);

	}

}