package br.edu.uepb.nutes.sdn.apirest;

public class Port {

	//private int number;
	private boolean actived = false;
	private boolean entryFlow = false;
//	private long throughputRate = 0;
	private int time;
	private String address = null;

	private long minRateQueueMonitoringCentral;
	
	private Category category;
	private Queue queue;
	private FlowMonitoring monitor;

	public Port(Category category, int time) {
		// TODO Auto-generated constructor stub
//		this.number = number;
		this.time = time;
		this.category = category;
		queue = new Queue();
	}

//	public int getNumber() {
//		return number;
//	}
//
//	public void setNumber(int number) {
//		this.number = number;
//	}

	public boolean isActived() {
		return actived;
	}

	public void setActived(boolean actived) {
		this.actived = actived;
	}

//	public long getThroughputRate() {
//		return throughputRate;
//	}
//
//	public void setThroughputRate(long throughputRate) {
//		this.throughputRate = throughputRate;
//	}

	public boolean isEntryFlow() {
		return entryFlow;
	}

	public void setEntryFlow(boolean entryFlow) {
		this.entryFlow = entryFlow;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public long getMinRateQueueMonitoringCentral() {
		return minRateQueueMonitoringCentral;
	}

	public void setMinRateQueueMonitoringCentral(long minRateQueueMonitoringCentral) {
		this.minRateQueueMonitoringCentral = minRateQueueMonitoringCentral;
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
	
	public void setAddress(String address) {
		// TODO Auto-generated method stub
		this.address = address;
	}

	public String getAddress() {
		// TODO Auto-generated method stub
		return address;
	}

}
