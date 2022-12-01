package com.redhat.consulting.monitoring;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.redhat.consulting.config.AmqArtemisBroker;
import com.redhat.consulting.management.AmqArtemisJmxManager;
import com.redhat.consulting.processors.DetermineBrokerSummaryProcessor;
import com.redhat.consulting.processors.DetermineQueueSummaryProcessor;
import com.redhat.consulting.processors.QueueSummary;

public class AmqArtemisJmxSummaryScraperRouteBuilder extends RouteBuilder {

	final private AmqArtemisBroker amqArtemisBroker;

	final private String brokerName;
	final private String brokerHost;
	final private String brokerJmxrmiPort;

	final private AmqArtemisJmxManager amqManager;

	public AmqArtemisJmxSummaryScraperRouteBuilder(AmqArtemisBroker amqArtemisBroker) {

		this.amqArtemisBroker = amqArtemisBroker;

		this.brokerName = amqArtemisBroker.getBrokerName();
		this.brokerHost = amqArtemisBroker.getHost();
		this.brokerJmxrmiPort = amqArtemisBroker.getJmxrmiPort();

		this.amqManager = new AmqArtemisJmxManager(this.brokerName, this.brokerHost, this.brokerJmxrmiPort);

		try {
			this.amqManager.connect(amqArtemisBroker.getUsername(), amqArtemisBroker.getPassword());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void configure() throws Exception {

		// @formatter:off
		from("timer:summaryScraper?period={{application.summary.period}}")
			.routeId("scrape.jmx.summary." + brokerName)
			.log("Scrape::Summary " + this.brokerName)
			.setHeader("brokerName", constant(this.brokerName) )
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					exchange.getIn().setBody(amqManager.getActiveMQServerControl());
				}
			})
			.process(new DetermineBrokerSummaryProcessor())
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					exchange.getIn().setBody(amqArtemisBroker.getAddressesToMonitor());
				}
			})
			.split(body(), new QueueSummaryAggregationStrategy())
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						final String queueName = exchange.getIn().getBody(String.class);
						exchange.getIn().setHeader("queueName", queueName);
						exchange.getIn().setHeader("queueSummary", new ArrayList<QueueSummary>());
						exchange.getIn().setBody(amqManager.getQueueControlMBean(queueName, queueName));
					}
				})
				.process(new DetermineQueueSummaryProcessor())
			.end()
			
			.to("direct:emailSummaryNotification");
			
	}

}
