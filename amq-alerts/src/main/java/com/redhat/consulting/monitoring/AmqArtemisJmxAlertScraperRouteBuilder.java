package com.redhat.consulting.monitoring;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.redhat.consulting.management.AmqArtemisJmxManager;
import com.redhat.consulting.processors.DetermineBrokerAlertProcessor;
import com.redhat.consulting.processors.DetermineQueueAlertProcessor;

public class AmqArtemisJmxAlertScraperRouteBuilder extends RouteBuilder {

	final private String brokerName;
	final private String brokerHost;
	final private String brokerJmxrmiPort;
	final private String brokerRole;
	final private boolean isClustered;

	final private AmqArtemisJmxManager amqManager;

	public AmqArtemisJmxAlertScraperRouteBuilder(String brokerName, String brokerHost, String brokerJmxrmiPort,
			String brokerRole, boolean isClustered) {

		this.brokerName = brokerName;
		this.brokerHost = brokerHost;
		this.brokerJmxrmiPort = brokerJmxrmiPort;
		this.brokerRole = brokerRole;
		this.isClustered = isClustered;

		this.amqManager = new AmqArtemisJmxManager(this.brokerName, this.brokerHost, this.brokerJmxrmiPort);

		try {
			this.amqManager.connect("joe", "joe");
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
					exchange.getIn().setBody(amqManager.getActiveMQServerControl());
				}
			})
			.process(new DetermineBrokerAlertProcessor())
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					exchange.getIn().setBody(amqManager.getQueueControlMBean());
				}
			})
			.process(new DetermineQueueAlertProcessor())
			.to("direct:emailAlertNotification");
		
		// @formatter:on

	}

}