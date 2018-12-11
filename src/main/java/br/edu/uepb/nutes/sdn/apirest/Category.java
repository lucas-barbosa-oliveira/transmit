package br.edu.uepb.nutes.sdn.apirest;

public enum Category {
	MONITORING_CENTRAL("eth0.3", 1, null),
	MEDICAL_DEVICES("eth0.4", 2, 1),
	CALL_VOIP("eth0.5", 3, 2),
	PESSONAL_MEDICAL_DEVICES("br-lan", 6, 3),
	RMI_IMAGES("br-lan", 6, null),
	DICOM_IMAGES("br-lan", 6, null),
	BROWSER("br-lan", 6, null);
	
	private final String interfacePort;
	private final int portNumber;
	private final Integer priority;
	
	Category(String interfacePort, int portNumber, Integer priority) {
		this.interfacePort = interfacePort;
		this.portNumber = portNumber;
		this.priority = priority;
	}
	
	public String getInterfacePort() {
		return interfacePort;
	}
	
	public int getPortNumber() {
		return portNumber;
	}
	
	public Integer getPriority() {
		return priority;
	}
}
//
