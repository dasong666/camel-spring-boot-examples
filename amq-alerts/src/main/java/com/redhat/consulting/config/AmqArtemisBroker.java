package com.redhat.consulting.config;

import java.util.List;

public class AmqArtemisBroker {

	private String name;
	private String host;
	private String jmxrmiPort;
	private List<String> addressesToMonitor;
	private String role;
	private boolean isClustered;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
