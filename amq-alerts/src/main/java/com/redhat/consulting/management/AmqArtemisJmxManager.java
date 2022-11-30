package com.redhat.consulting.management;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

public class AmqArtemisJmxManager {

	private static final String DEFAULT_JMX_URL = "service:jmx:rmi:///jndi/rmi://192.168.1.167:1099/jmxrmi";

	private final String jmxrmiUrl;

	private JMXConnector connector;
	private MBeanServerConnection serverConnection;

	private ObjectName queueObjectName;
	private ObjectName serverObjectName;

	private QueueControl queueControl;
	private ActiveMQServerControl serverControl;

	public AmqArtemisJmxManager(String brokerName, String brokerHost, String brokerJmxrmiPort) {

		this.jmxrmiUrl = "service:jmx:rmi:///jndi/rmi://" + brokerHost + ":" + brokerJmxrmiPort + "/jmxrmi";

	}

	/***
	 * Method to establish a remote connection
	 * 
	 * @param username
	 * @param password
	 */
	public void connect(String username, String password) throws IOException {

		final Map<String, String[]> env = new HashMap<String, String[]>();
		String[] creds = { username, password };
		env.put(JMXConnector.CREDENTIALS, creds);

		connector = JMXConnectorFactory.connect(new JMXServiceURL(DEFAULT_JMX_URL), env);
		serverConnection = connector.getMBeanServerConnection();

	}

	public QueueControl getQueueControlMBean(String address, String name) throws Exception {

		queueObjectName = ObjectNameBuilder.create("org.apache.activemq.artemis", "0.0.0.0", true).getQueueObjectName(
				SimpleString.toSimpleString(address), SimpleString.toSimpleString(name), RoutingType.ANYCAST);

		queueControl = MBeanServerInvocationHandler.newProxyInstance(serverConnection, queueObjectName,
				QueueControl.class, false);

		return this.queueControl;

	}

	public ActiveMQServerControl getActiveMQServerControl() throws Exception {

		serverObjectName = ObjectNameBuilder.create("org.apache.activemq.artemis", "0.0.0.0", true)
				.getActiveMQServerObjectName();

		serverControl = MBeanServerInvocationHandler.newProxyInstance(serverConnection, serverObjectName,
				ActiveMQServerControl.class, false);

		return this.serverControl;

	}

	/**
	 * Close Connection to JMX RMI
	 * 
	 */
	public void close() throws IOException {
		connector.close();
	}

}
