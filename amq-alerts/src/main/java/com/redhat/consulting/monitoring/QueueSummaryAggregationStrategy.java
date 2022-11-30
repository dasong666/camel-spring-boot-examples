package com.redhat.consulting.monitoring;

import java.util.ArrayList;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import com.redhat.consulting.processors.QueueSummary;

public class QueueSummaryAggregationStrategy implements AggregationStrategy {

	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

		QueueSummary newQueueSummary = newExchange.getIn().getBody(QueueSummary.class);
		ArrayList<QueueSummary> list = null;
		if (oldExchange == null) {
			list = new ArrayList<QueueSummary>();
			list.add(newQueueSummary);
			newExchange.getIn().setBody(list);
			return newExchange;
		} else {
			list = oldExchange.getIn().getBody(ArrayList.class);
			list.add(newQueueSummary);
			return oldExchange;
		}

	}

}
