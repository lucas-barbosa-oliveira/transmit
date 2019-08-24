package br.edu.uepb.nutes.sdn;

import java.util.ArrayList;

//import java.util.Arrays;
//
//import org.json.JSONArray;
//import org.json.JSONArray;
import org.json.JSONObject;

public class FlowMonitoring extends ServerCommunication implements Runnable {

	private Thread flowManager;
	private String switchId;
	private Port port;

	private ArrayList<Long> bandwidthValues = new ArrayList<Long>();

	private long actualBandwidth;


	private static boolean strategyOnlyPriority = true;

	public FlowMonitoring(String switchId, Port port) {
		// TODO Auto-generated constructor stub
		this.switchId = switchId;
		this.port = port;
	}

	public Port getPort() {
		return port;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {

			if (port.getCategory().getPriority() != null)
				SwitchController.createQueue(port, strategyOnlyPriority);

			SwitchController.insertPolitic(switchId, port.getCategory().toString(), port);
			port.setEntryFlow(true);

			while (true) {

				if (port.getAddress() == null)
					port.setAddress(port.getCategory().getIp());

				if (port.getCategory().getDefaultPriority() == null
						&& port.getCategory().getPriority() != port.getCategory().getDefaultPriority()
						&& port.getQueue().getUuidQueueMonitoringCentral() == null) {
					SwitchController.removePolitic(port.getCategory().toString());
					SwitchController.createQueue(port, strategyOnlyPriority);
					SwitchController.insertPolitic(switchId, port.getCategory().toString(), port);
				} else if (port.getCategory().getDefaultPriority() == null
						&& port.getCategory().getPriority() == port.getCategory().getDefaultPriority()
						&& port.getQueue().getUuidQueueMonitoringCentral() != null) {
					SwitchController.removePolitic(port.getCategory().toString());
					SwitchController.deleteQueue(port);
					SwitchController.insertPolitic(switchId, port.getCategory().toString(), port);
				}

				this.actualBandwidth = portBandwidthMonitoring();

				System.out.println("Port: " + port.getCategory().getPortNumber() + " ActualBandwidth: "
						+ this.actualBandwidth + " Priority: " + port.getCategory().getPriority() + " IP: "
						+ port.getAddress() + " Interface: " + port.getCategory().getInterfacePort());

				if (this.actualBandwidth == 0 && port.isEntryFlow()) {
					SwitchController.removePolitic(port.getCategory().toString());
					port.setEntryFlow(false);
				}

				if (this.actualBandwidth > 0 && !port.isEntryFlow()) {
					SwitchController.insertPolitic(switchId, port.getCategory().toString(), port);
					port.setEntryFlow(true);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public FlowMonitoring initializeMonitoring() {
		flowManager = new Thread(this);
		flowManager.start();

		return this;
	}

	public boolean stopMonitoring() {
		flowManager = null;
		System.out.println("Break the Queue");
//		SwitchController.deleteQueue(port.getUuidQueueMonitoringCentral());
		return false;
	}

	private long portBandwidthMonitoring() {
		// TODO Auto-generated method stub

		JSONObject portValue = SwitchController.getPortStatistics(switchId, port.getCategory().getPortNumber());

		long first_receive_bytes = Long.parseLong(portValue.getString("receive_bytes"));
		long first_duration_sec = Long.parseLong(portValue.getString("duration_sec"));

		try {
			Thread.sleep(this.port.getTime() * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		portValue = SwitchController.getPortStatistics(switchId, port.getCategory().getPortNumber());

		long second_receive_bytes = Long.parseLong(portValue.getString("receive_bytes"));
		long second_duration_sec = Long.parseLong(portValue.getString("duration_sec"));

		long bandwidth = (long) (((second_receive_bytes - first_receive_bytes) * 8)
				/ (second_duration_sec - first_duration_sec));

//		System.out.println("Port: " + port.getCategory().getPortNumber() + " " + bandwidth + "bps");

		addBandwidthValues(bandwidth);

		return bandwidth;
	}


	private void addBandwidthValues(long bandwidth) {
		// TODO Auto-generated method stub
		if (bandwidthValues.size() == 10) {
			bandwidthValues.remove(0);
			bandwidthValues.add(bandwidth);
		} else
			bandwidthValues.add(bandwidth);
	}

	public void changePriority(boolean isAlarming) {
		// TODO Auto-generated method stub

		if (port.getCategory().activeAlarm(isAlarming)) {
			if (port.getCategory().getDefaultPriority() != null) {
				System.out.println("Atualizar Prioridade");
				SwitchController.deleteQueue(port);
				SwitchController.createQueue(port, strategyOnlyPriority);
			}
		}

	}


}
