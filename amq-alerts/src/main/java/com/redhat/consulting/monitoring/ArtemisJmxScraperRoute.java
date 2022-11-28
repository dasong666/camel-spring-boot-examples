package com.redhat.consulting.monitoring;

import java.io.IOException;
import java.util.HashMap;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.artemis.api.core.RoutingType;
import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.api.core.management.ActiveMQServerControl;
import org.apache.activemq.artemis.api.core.management.ObjectNameBuilder;
import org.apache.activemq.artemis.api.core.management.QueueControl;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ArtemisJmxScraperRoute extends RouteBuilder {

	private static final String JMX_URL = "service:jmx:rmi:///jndi/rmi://192.168.1.167:1099/jmxrmi";

	public ArtemisJmxScraperRoute() throws IOException, Exception {
		
		// TODO
		// This is an example of connecting to the broker via JMX.  This approach has the advantage
		// of using AMQ broker class types. Would recommend the code below be part of a Camel Route
		// and perhaps maintain a single connection.
		
		// TODO
		// In addition, these value will need to come from properties.
		

		System.out.println("ScraperRoute::constructor");

		ObjectName queueObjectName = ObjectNameBuilder.create("org.apache.activemq.artemis", "0.0.0.0", true)
				.getQueueObjectName(SimpleString.toSimpleString("TEST"), SimpleString.toSimpleString("TEST"),
						RoutingType.ANYCAST);

		ObjectName serverObjectName = ObjectNameBuilder.create("org.apache.activemq.artemis", "0.0.0.0", true)
				.getActiveMQServerObjectName();

		HashMap env = new HashMap();
		String[] creds = { "joe", "joe" };
		env.put(JMXConnector.CREDENTIALS, creds);

		JMXConnector connector = JMXConnectorFactory.connect(new JMXServiceURL(ArtemisJmxScraperRoute.JMX_URL), env);

		MBeanServerConnection serverConnection = connector.getMBeanServerConnection();

		QueueControl queueControl = MBeanServerInvocationHandler.newProxyInstance(serverConnection, queueObjectName,
				QueueControl.class, false);

		ActiveMQServerControl serverControl = MBeanServerInvocationHandler.newProxyInstance(serverConnection,
				serverObjectName, ActiveMQServerControl.class, false);

		System.out.println("NodeID: " + serverControl.getNodeID() + " addressMemoryUsage: "
				+ serverControl.getAddressMemoryUsage());

		System.out.println("Connection Count: " + serverControl.getConnectionCount());
		System.out.println("Total Message Count: " + serverControl.getTotalMessageCount());

		System.out.println(queueControl.getName() + " contains " + queueControl.getMessageCount() + " messages");

		connector.close();

	}

	@Override
	public void configure() throws Exception {

		System.out.println("config brokerName: {{amq.brokerName)}}");

		// @formatter:off
		from("timer:hello?period={{timer.period}}").routeId("monitoring.artemis.scraper")
			.log("About to Scrape {{amq.brokerName}}")
		
		// TODO
		// Scrap the Broker for various ObjectNames
		// Write a Java class to validate the ObjectNames against a set of thresholds.
		// If threshold exceed then send an email.
		
		// TODO 
		// Write a Java class that formats the email subject and email body based on the rules.
		
			.to("direct:determineNotification")
			
			.setHeader("sendSummary", constant("{{notification.summary}}"))
			.to("direct:emailNotification");
		
		// @formatter:on

	}

}
