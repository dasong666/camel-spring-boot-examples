package com.redhat.consulting.monitoring;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.redhat.consulting.config.AddressAlertThresholds;
import com.redhat.consulting.config.AmqArtemisBroker;
import com.redhat.consulting.config.BrokerAlertThresholds;
import com.redhat.consulting.management.AmqArtemisJmxManager;
import com.redhat.consulting.processors.DetermineBrokerAlertProcessor;
import com.redhat.consulting.processors.DetermineQueueAlertProcessor;

public class AmqArtemisJmxAlertScraperRouteBuilder extends RouteBuilder {

	private BrokerAlertThresholds brokerAlertThresholds;
	private AddressAlertThresholds addressAlertThresholds;
	private AmqArtemisBroker amqArtemisBroker;

	final private String brokerName;
	final private String brokerHost;
	final private String brokerJmxrmiPort;
	final private String brokerRole;
	final private boolean isClustered;

	final private AmqArtemisJmxManager amqManager;

	public AmqArtemisJmxAlertScraperRouteBuilder(AmqArtemisBroker amqArtemisBroker,
			BrokerAlertThresholds brokerAlertThresholds, AddressAlertThresholds addressAlertThresholds) {

		this.amqArtemisBroker = amqArtemisBroker;
		this.brokerAlertThresholds = brokerAlertThresholds;
		this.addressAlertThresholds = addressAlertThresholds;

		this.brokerName = amqArtemisBroker.getBrokerName();
		this.brokerHost = amqArtemisBroker.getHost();
		this.brokerJmxrmiPort = amqArtemisBroker.getJmxrmiPort();
		this.brokerRole = amqArtemisBroker.getRole();
		this.isClustered = amqArtemisBroker.isClustered();

		this.amqManager = new AmqArtemisJmxManager(this.brokerName, this.brokerHost, this.brokerJmxrmiPort);

		try {
			this.amqManager.connect(amqArtemisBroker.getUsername(), amqArtemisBroker.getPassword());
		} catch (IOException e) {
			System.out.println("Unable to Connect to Broker");
			e.printStackTrace();
		}

	}

	@Override
	public void configure() throws Exception {

		// @formatter:off
		from("timer:alertScraper?period={{application.alert.period}}")
			.routeId("scrape.jmx.alert." + brokerName)
			.log("Scrape::Alert::" + this.brokerName)
			.setHeader("brokerName", constant(this.brokerName) )
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					exchange.getIn().setHeader("brokerRole", brokerRole);
					exchange.getIn().setHeader("isClustered", isClustered);
					exchange.getIn().setHeader("alertExists", false);
					exchange.getIn().setBody(amqManager.getActiveMQServerControl());
				}
			})
			.process(new DetermineBrokerAlertProcessor(this.brokerAlertThresholds))
			.to("direct:emailBrokerAlertNotification")
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					exchange.getIn().setBody(amqArtemisBroker.getAddressesToMonitor());
				}
			})
			.split()
			.body()
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						final String queueName = exchange.getIn().getBody(String.class);
						exchange.getIn().setHeader("queueAlertExists", false);
						exchange.getIn().setHeader("queueName", queueName);
						exchange.getIn().setBody(amqManager.getQueueControlMBean(queueName, queueName));
					}
				})
				.process(new DetermineQueueAlertProcessor(this.addressAlertThresholds))
				.to("direct:emailQueueAlertNotification")
			.end();
			
		// @formatter:on

	}

}