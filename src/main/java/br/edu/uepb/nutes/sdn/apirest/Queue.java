package br.edu.uepb.nutes.sdn.apirest;

public class Queue {
	
	private String uuidQosMonitoringCentral;
	private String uuidQueueMonitoringCentral;
	private String name;
	private long minRate = 0;
	private long maxRate;
	private long burst = 0;
	

	public String getUuidQosMonitoringCentral() {
		return uuidQosMonitoringCentral;
	}

	public void setUuidQosMonitoringCentral(String uuidQosMonitoringCentral) {
		this.uuidQosMonitoringCentral = uuidQosMonitoringCentral;
	}

	public String getUuidQueueMonitoringCentral() {
		return uuidQueueMonitoringCentral;
	}

	public void setUuidQueueMonitoringCentral(String uuid) {
		this.uuidQueueMonitoringCentral = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getMinRate() {
		return minRate;
	}

	public void setMinRate(long minRate) {
		this.minRate = minRate;
	}

	public long getMaxRate() {
		return maxRate;
	}

	public void setMaxRate(long maxRate) {
		this.maxRate = maxRate;
	}

	public long getBurst() {
		return burst;
	}

	public void setBurst(long burst) {
		if(burst > this.burst || burst == 0)
			this.burst = burst;
	}

	

}
