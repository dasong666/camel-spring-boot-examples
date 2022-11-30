package com.redhat.consulting.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.alert.broker-thresholds")
public class BrokerAlertThresholds {

	private int connections;
	private int totalConnections;
	private double diskStoreUsage;
	private double addressMemoryPercentage;

	public int getConnections() {
		return connections;
	}

	public void setConnections(int connections) {
		this.connections = connections;
	}

	public int getTotalConnections() {
		return totalConnections;
	}

	public void setTotalConnections(int totalConnections) {
		this.totalConnections = totalConnections;
	}

	public double getDiskStoreUsage() {
		return diskStoreUsage;
	}

	public void setDiskStoreUsage(double diskStoreUsage) {
		this.diskStoreUsage = diskStoreUsage;
	}

	public double getAddressMemoryPercentage() {
		return addressMemoryPercentage;
	}

	public void setAddressMemoryPercentage(double addressMemoryPercentage) {
		this.addressMemoryPercentage = addressMemoryPercentage;
	}

}