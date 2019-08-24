package br.edu.uepb.nutes.sdn;

public enum Category {
	MONITORING_CENTRAL(1, null, null),
	MEDICAL_DEVICES(2, 5, "192.168.3.2"),
	RMI_IMAGES(2, 8, "192.168.3.2"),
	DICOM_IMAGES(3, 8, "192.168.3.3"),
	CALL_VOIP(3, 6, "192.168.3.3"),
	DICOM_IMAGES_SECOND(4, 8, "192.168.3.4"),
	VIDEO_AUDIO(4, 6, "192.168.3.4"),
	PESSONAL_MEDICAL_DEVICES(4, 7, "192.168.3.4"),
	BROWSER(4, 8, "192.168.3.4"), 
	UDP_TRAFFIC(4, 8, "192.168.3.4"); 

	
	private final String switchId = "00:00:c0:25:e9:01:28:2a";
	private final String interfacePort;
	private final int portNumber;
	private Integer priority;
	private final Integer defaultPriority;
	private final int alarmValue = 4;
	private final String ip;
	
	Category(int portNumber, Integer priority, String ip) {
		this.interfacePort = SwitchController.getInterfacePort(switchId, portNumber);
		this.portNumber = portNumber;
		this.priority = priority;
		this.defaultPriority = priority;
		this.ip = ip;
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
	
	public String getIp() {
		return ip;
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
