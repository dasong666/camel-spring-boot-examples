package com.redhat.consulting.monitoring;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class DetermineNotificationRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// @formatter:off		
		from("direct:determineNotification").routeId("monitoring.determine.notification")
		
			// Load Up Broker Data
			.setHeader("brokerName", constant("brokerNameActive"))
			.setHeader("backup", constant("true"))
			.setHeader("totalMessagesAdded", constant("5"))
			.setHeader("totalMessagesAckd", constant("5"))
			.setHeader("uptime", constant("forever"))
			.setHeader("diskStoreUsage", constant("40"))
			.setHeader("connections", constant("10"))
			.setHeader("totalConnections", constant("20"))
			
			
			// Determine Alert
			.setHeader("sendMemoryAlert", constant("true"));
		
		
		// @formatter:on

	}

}
