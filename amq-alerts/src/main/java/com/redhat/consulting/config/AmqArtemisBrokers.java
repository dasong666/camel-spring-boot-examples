package com.redhat.consulting.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "amq")
public class AmqArtemisBrokers {

	private List<AmqArtemisBroker> brokers = new ArrayList<>();

	public List<AmqArtemisBroker> getBrokers() {
		return brokers;
	}

	public void setBrokers(List<AmqArtemisBroker> brokers) {
		this.brokers = brokers;
	}

}