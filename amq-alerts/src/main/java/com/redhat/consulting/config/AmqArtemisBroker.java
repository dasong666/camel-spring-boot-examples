package com.redhat.consulting.config;

import java.util.List;

public class AmqArtemisBroker {

	private String brokerName;
	private String username;
	private String password;
	private String host;
	private String jmxrmiPort;
	private List<String> addressesToMonitor;
	private String role;
	private boolean isClustered;

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getJmxrmiPort() {
		return jmxrmiPort;
	}

	public void setJmxrmiPort(String jmxrmiPort) {
		this.jmxrmiPort = jmxrmiPort;
	}

	public List<String> getAddressesToMonitor() {
		return addressesToMonitor;
	}

	public void setAddressesToMonitor(List<String> addressesToMonitor) {
		this.addressesToMonitor = addressesToMonitor;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isClustered() {
		return isClustered;
	}

	public void setClustered(boolean isClustered) {
		this.isClustered = isClustered;
	}

}
