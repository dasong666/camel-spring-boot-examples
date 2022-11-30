package com.redhat.consulting.monitoring;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailSummaryNotificationRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// @formatter:off
		from("direct:emailSummaryNotification")
			.routeId("email.summary.notification")
			.log("Send Summary Notification")
			.to("velocity:BrokerHealthTemplate.vm")
			.to("smtp://{{smtp.host}}:{{smtp.port}}?username={{smtp.username}}&password={{smtp.password}}&from={{smtp.fromEmail}}&contentType={{smtp.contentType}}");
		
		// @formatter:on

	}

}
