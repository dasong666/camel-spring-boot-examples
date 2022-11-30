package com.redhat.consulting.processors;

public class QueueSummary {

	private String queueName;
	private int consumerCount;
	private long messageAdded;
	private long messageAcknowledged;
	private long messageCount;

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public int getConsumerCount() {
		return consumerCount;
	}

	public void setConsumerCount(int consumerCount) {
		this.consumerCount = consumerCount;
	}

	public long getMessageAdded() {
		return messageAdded;
	}

	public void setMessageAdded(long messageAdded) {
		this.messageAdded = messageAdded;
	}

	public long getMessageAcknowledged() {
		return messageAcknowledged;
	}

	public void setMessageAcknowledged(long messageAcknowledged) {
		this.messageAcknowledged = messageAcknowledged;
	}

	public long getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(long messageCount) {
		this.messageCount = messageCount;
	}

}
