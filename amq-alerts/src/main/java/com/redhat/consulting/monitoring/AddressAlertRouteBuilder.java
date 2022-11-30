package com.redhat.consulting.monitoring;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class AddressAlertRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// @formatter:off		
		from("direct:determineQueueAlert")
			.routeId("determine.address.alert")
			.setHeader("testing", constant("true"));
		// @formatter:on

	}
}
