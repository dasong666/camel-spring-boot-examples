package com.redhat.consulting.monitoring;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailQueueAlertNotificationRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// @formatter:off
		from("direct:emailQueueAlertNotification")
			.routeId("email.queueAlert.notification")
			
			.choice()
									
			// alert Notifications 
			.when(header("queueAlertExists").contains("true"))
				.log("Queue Alert Exists")
				.to("velocity:QueueAlertTemplate.vm")
				.to("smtp://{{smtp.host}}:{{smtp.port}}?username={{smtp.username}}&password={{smtp.password}}&from={{smtp.fromEmail}}&contentType={{smtp.contentType}}")
				
			.otherwise()
				.log("Evaluation Complete")
			
			.end();
		// @formatter:on

	}

}
