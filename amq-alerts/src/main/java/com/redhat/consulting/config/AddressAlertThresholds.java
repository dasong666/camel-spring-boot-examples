package com.redhat.consulting.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.alert.address-thresholds")
public class AddressAlertThresholds {

	private int consumers;
	private int messageCount;

	public int getConsumers() {
		return consumers;
	}

	public void setConsumers(int consumers) {
		this.consumers = consumers;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

}