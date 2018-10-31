package br.edu.uepb.nutes.sdn.apirest;

public class Policy {
	
	private String name;
	private String switchId;
//	private String entry_type;
	private String actions;
	private boolean active = true;
	private String in_port;
	
	public Policy(String switchId) {
		// TODO Auto-generated constructor stub
		this.switchId = switchId;
	}
	
	public Policy(String name, String switchId) {//, String entry_type) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.switchId = switchId;
//		this.entry_type = entry_type;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSwitchId() {
		return switchId;
	}

	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}

//	public String getPolicyType() {
//		return entry_type;
//	}
//
//	public void setPolicyType(String entry_type) {
//		this.entry_type = entry_type;
//	}


	public String getActions() {
		return actions;
	}


	public void setActions(Actions actions) {
		this.actions = actions.toString();
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public String getInPort() {
		return in_port;
	}


	public void setInPort(String in_port) {
		this.in_port = in_port;
	}
	
 
}