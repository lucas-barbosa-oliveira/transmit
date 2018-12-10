package br.edu.uepb.nutes.sdn.apirest;

public class Port {

	private int number;
	private boolean actived = false;
	private long peakDataRate;
	private int time;
	private String uuidQosMonitoringCentral;
	private String uuidQueueMonitoringCentral;
	
	private Category category;
	private Queue queue;
	private FlowMonitoring monitor;

	public Port(int number, int time, Category category) {
		// TODO Auto-generated constructor stub
		this.number = number;
		this.time = time;
		this.category = category;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isActived() {
		return actived;
	}

	public void setActived(boolean actived) {
		this.actived = actived;
	}

	public long getPeakDataRate() {
		return peakDataRate;
	}

	public void setPeakDataRate(long peakDataRte) {
		this.peakDataRate = peakDataRte;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public String getQosMonitoringCentral() {
		return uuidQosMonitoringCentral;
	}

	public void setQosMonitoringCentral(String qosMonitoringCentral) {
		this.uuidQosMonitoringCentral = qosMonitoringCentral;
	}
	
	public String getUuidQueueMonitoringCentral() {
		return uuidQueueMonitoringCentral;
	}

	public void setUuidQueueMonitoringCentral(String uuidQueueMonitoringCentral) {
		this.uuidQueueMonitoringCentral = uuidQueueMonitoringCentral;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	public void initFlowMonitoring(String switchId) {
		// TODO Auto-generated method stub
		monitor = new FlowMonitoring(switchId, this);
		monitor.initializeMonitoring();
	}

	public void stopFlowMonitoring() {
		// TODO Auto-generated method stub
		monitor.stopMonitoring();
	}

}
