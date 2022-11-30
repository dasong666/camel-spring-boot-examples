package com.redhat.consulting.monitoring;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.redhat.consulting.management.AmqArtemisJmxManager;
import com.redhat.consulting.processors.DetermineBrokerSummaryProcessor;

public class AmqArtemisJmxSummaryScraperRouteBuilder extends RouteBuilder {

	final private String brokerName;
	final private String brokerHost;
	final private String brokerJmxrmiPort;

	final private AmqArtemisJmxManager amqManager;

	public AmqArtemisJmxSummaryScraperRouteBuilder(String brokerName, String brokerHost, String brokerJmxrmiPort) {

		this.brokerName = brokerName;
		this.brokerHost = brokerHost;
		this.brokerJmxrmiPort = brokerJmxrmiPort;

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
			.to("direct:emailSummaryNotification");
			
	}

}
