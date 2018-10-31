package br.edu.uepb.nutes.sdn.apirest;

public class Actions {
	
	private String output;
	private String set_queue;

	public Actions() {
		// TODO Auto-generated constructor stub
	}
			
	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getSet_queue() {
		return set_queue;
	}

	public void setSet_queue(String set_queue) {
		this.set_queue = set_queue;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "output="+output+",set_queue="+set_queue;
	}


}
