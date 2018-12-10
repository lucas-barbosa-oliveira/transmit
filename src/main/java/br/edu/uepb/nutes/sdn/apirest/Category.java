package br.edu.uepb.nutes.sdn.apirest;

public enum Category {
	MONITORING_CENTRAL("eth0.3"),
	MEDICAL_DEVICES("eth0.4"),
	CALL_VOIP("eth0.5"),
	PESSONAL_MEDICAL_DEVICES("br-lan"),
	RMI_IMAGES("br-lan"),
	DICOM_IMAGES("br-lan"),
	BROWSER("br-lan");
	
	private final String interfacePort;
	
	Category(String category) {
		interfacePort = category;
	}
	
	public String getInterfacePort() {
		return interfacePort;
	}
}
//
