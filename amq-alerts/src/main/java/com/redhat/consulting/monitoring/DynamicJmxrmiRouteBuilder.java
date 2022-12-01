package com.redhat.consulting.monitoring;

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.redhat.consulting.config.AddressAlertThresholds;
import com.redhat.consulting.config.AmqArtemisBroker;
import com.redhat.consulting.config.AmqArtemisBrokers;
import com.redhat.consulting.config.BrokerAlertThresholds;

@Component
public class DynamicJmxrmiRouteBuilder extends RouteBuilder {

	@Autowired
	private CamelContext camelContext;

	@Autowired
	private AmqArtemisBrokers amqProps = new AmqArtemisBrokers();

	@Autowired
	private BrokerAlertThresholds brokerAlertThresholds = new BrokerAlertThresholds();

	@Autowired
	private AddressAlertThresholds addressAlertThresholds = new AddressAlertThresholds();

	@Value("${application.alert.enabled}")
	private boolean applicationAlerts;

	@Value("${application.summary.enabled}")
	private boolean applicationSummary;

	public void configure() throws Exception {

		final List<AmqArtemisBroker> brokers = amqProps.getBrokers();

		if (0 == brokers.size()) {
			System.out.println("No Brokers Set");
		}

		// @formatter:off
		
		onException(Exception.class)
			.handled(true)
			.onRedelivery(new Processor() {
				public void process(Exchange exchange) throws Exception {
					System.out.println("@@@ Retrying");
				}
			})
			.log("Unable");
		
		// @formatter:on

		if (true == this.applicationAlerts) {

			for (AmqArtemisBroker broker : brokers) {

				this.camelContext.addRoutes(new AmqArtemisJmxAlertScraperRouteBuilder(broker, brokerAlertThresholds,
						addressAlertThresholds));

			}

		} else {
			System.out.println("No Alerts");
		}

		if (true == this.applicationSummary) {

			for (AmqArtemisBroker broker : brokers) {

				this.camelContext.addRoutes(new AmqArtemisJmxSummaryScraperRouteBuilder(broker));

			}

		} else {
			System.out.println("No Summary Reports");
		}

	}

}