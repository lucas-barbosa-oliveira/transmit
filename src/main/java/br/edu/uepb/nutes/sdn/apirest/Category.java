package br.edu.uepb.nutes.sdn.apirest;

public enum Category {
//	MONITORING_CENTRAL("eth0.3", 1, null),
//	MEDICAL_DEVICES("eth0.4", 2, 5),
//	CALL_VOIP("eth0.5", 3, 5),
//	VIDEO_AUDIO("br-lan", 6, 6),
//	PESSONAL_MEDICAL_DEVICES("br-lan", 6, 6),
//	RMI_IMAGES("eth0.5", 2, null),
//	DICOM_IMAGES("br-lan", 6, null),
//	BROWSER("br-lan", 6, null);
	
	MONITORING_CENTRAL(1, null),
	MEDICAL_DEVICES(2, 5),
	CALL_VOIP(3, 5),
	VIDEO_AUDIO(4, 6),
	PESSONAL_MEDICAL_DEVICES(4, 6),
	RMI_IMAGES(4, null),
	DICOM_IMAGES(4, null),
	BROWSER(4, null);
	
	private final String switchId = "00:00:c0:25:e9:01:28:2a";
	private final String interfacePort;
	private final int portNumber;
	private Integer priority;
	private final Integer defaultPriority;
	private final int alarmValue = 4;
	
	Category(int portNumber, Integer priority) {
		this.interfacePort = SwitchController.getInterfacePort(switchId, portNumber);
		this.portNumber = portNumber;
		this.priority = priority;
		this.defaultPriority = priority;
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
	
	public Integer getDefaultPriority() {
		return defaultPriority;
	}
	
	public boolean activeAlarm(boolean isAlarming) {
		Integer priorityBeforeChange = this.priority;
		
		if(isAlarming && this.priority == this.defaultPriority)
			if(this.priority != null)
				this.priority = this.priority - alarmValue;
			else
				this.priority = alarmValue;
		else if(!isAlarming && this.defaultPriority == null)
			this.priority = null;
		else if(!isAlarming && this.priority < this.defaultPriority)
			this.priority = this.priority + alarmValue;
		
		if(this.priority != priorityBeforeChange)
			return true;
		else
			return false;
	}
}
//
