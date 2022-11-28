package com.redhat.consulting.monitoring;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// @formatter:off
		from("direct:emailNotification").routeId("monitoring.email.notification")
			.log("About to send an email")
			
			.choice()
			
			// summary Notification			
			.when(header("sendSummary").contains("true"))
				.log("Summary Email Send")
				.to("velocity:BrokerHealthTemplate.vm")
				.log("${body}")
				.to("smtp://{{smtp.host}}:{{smtp.port}}?username={{smtp.username}}&password={{smtp.password}}&from={{smtp.from.email}}&contentType={{smtp.contentType}}")
				.log("Summary Email Sent")
						
			// alert Notifications 
			.when(header("sendMemoryAlert").contains("true"))
				.log("Send Memory Alert")
				.to("velocity:BrokerAlertTemplate.vm")
				.log("${body}")
				
			.otherwise()
				.log("Evaluation Complete")
			
			.end();
				
		
		// @formatter:on

	}

}
