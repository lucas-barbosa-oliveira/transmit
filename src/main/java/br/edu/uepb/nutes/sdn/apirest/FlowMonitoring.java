package br.edu.uepb.nutes.sdn.apirest;

import java.util.ArrayList;
//import java.util.Arrays;
//
//import org.json.JSONArray;
//import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

public class FlowMonitoring extends ServerCommunication implements Runnable {

	private Thread flowManager;
	private String switchId;
	private Port port;

	private ArrayList<Long> bandwidthValues = new ArrayList<Long>();

	private long actualBandwidth;
	private long actualStandardDeviation;
	private long actualAverageBandwidth;
	private long actualTopBandwidth;
	private long downLimit = 0;

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
					port.setAddress(SwitchController.getIp(switchId, port.getCategory().getPortNumber()));

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

				if (!strategyOnlyPriority) {

					this.actualStandardDeviation = standardDeviation();

					this.actualAverageBandwidth = averageBandwidth();

					this.actualTopBandwidth = topBandwidth();

					if (port.getCategory().getPriority() != null)
						manageDeviceWithPriority();
					else
						manageDeviceWithoutPriority();
				} else {
					
					System.out.println("Port: " + port.getCategory().getPortNumber()
							+ " ActualBandwidth: " + this.actualBandwidth + " Priority: "
							+ port.getCategory().getPriority() + " IP: " + port.getAddress() + " Interface: "
							+ port.getCategory().getInterfacePort());
					
					if (this.actualBandwidth == 0 && port.isEntryFlow()) {
						SwitchController.removePolitic(port.getCategory().toString());
						port.setEntryFlow(false);
					}

					if (this.actualBandwidth > 0 && !port.isEntryFlow()) {
						SwitchController.insertPolitic(switchId, port.getCategory().toString(), port);
						port.setEntryFlow(true);
					}
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

	private long standardDeviation() {
		// TODO Auto-generated method stub

		double median = 0;

		for (Long bandwidth : bandwidthValues) {
			median += bandwidth;
		}
		median = median / bandwidthValues.size();

		double variance = 0;

		for (Long bandwidth : bandwidthValues) {
			variance += Math.pow((bandwidth - median), 2);
		}

		variance = variance / bandwidthValues.size();

		return (long) (Math.ceil(Math.sqrt(variance)));
	}

	private long topBandwidth() {
		// TODO Auto-generated method stub
		long high = 0;

		for (Long bandwidth : bandwidthValues) {
			if (bandwidth > high) {
				high = bandwidth;
			}
		}

		return high;
	}

	private long averageBandwidth() {
		// TODO Auto-generated method stub
		double median = 0;
//		int sum = 0;

		for (Long bandwidth : bandwidthValues)
			median += bandwidth;

		if (bandwidthValues.size() != 0)
			median = median / bandwidthValues.size();

		return (long) (Math.ceil(median));
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
				SwitchController.updateQueue(port, strategyOnlyPriority);
			}
		}

	}

	private void manageDeviceWithPriority() throws UnirestException {
		// TODO Auto-generated method stub
		System.out.println("Port: " + port.getCategory().getPortNumber() + " MinRate: " + port.getQueue().getMinRate()
				+ " ActualBandwidth: " + this.actualBandwidth + " DownLimit: " + this.downLimit + " Medium: "
				+ this.actualAverageBandwidth + " DP: " + this.actualStandardDeviation + " Priority: "
				+ port.getCategory().getPriority() + " IP: " + port.getAddress() + " Interface: "
				+ port.getCategory().getInterfacePort());

		if (port.getQueue().getMinRate() < this.actualBandwidth) {
			System.out.println("Aumentar");
			port.getQueue().setBurst(this.actualBandwidth);
			port.getQueue().setMinRate(this.actualBandwidth);
			this.downLimit = this.actualAverageBandwidth - standardDeviation();

			SwitchController.updateQueue(port, strategyOnlyPriority);
			if (!port.isEntryFlow()) {
				SwitchController.insertPolitic(switchId, port.getCategory().toString(), port);
				port.setEntryFlow(true);
			}
		}

		if ((this.actualAverageBandwidth < this.downLimit
				|| port.getQueue().getMinRate() > this.actualTopBandwidth + this.actualStandardDeviation)
				&& bandwidthValues.size() == 10 && this.actualBandwidth != 0) {
			System.out.println("Diminuir");
			port.getQueue().setMinRate(this.actualTopBandwidth);
			this.downLimit = this.actualAverageBandwidth - this.actualStandardDeviation;
			SwitchController.updateQueue(port, strategyOnlyPriority);
		}

		if (this.actualBandwidth == 0 && port.isEntryFlow()) {
			port.getQueue().setBurst(this.actualBandwidth);
			port.getQueue().setMinRate(this.actualBandwidth);
			SwitchController.updateQueue(port, strategyOnlyPriority);
			bandwidthValues.clear();
			SwitchController.removePolitic(port.getCategory().toString());
			port.setEntryFlow(false);
		}
	}

	private void manageDeviceWithoutPriority() throws UnirestException {
		// TODO Auto-generated method stub
		if (port.getQueue().getMinRate() < this.actualBandwidth) {
			port.getQueue().setBurst(this.actualBandwidth);
			port.getQueue().setMinRate(this.actualBandwidth);
			this.downLimit = this.actualAverageBandwidth - standardDeviation();
			if (!port.isEntryFlow()) {
				SwitchController.insertPolitic(switchId, port.getCategory().toString(), port);
				port.setEntryFlow(true);
			}
		}

		if ((this.actualAverageBandwidth < this.downLimit
				|| port.getQueue().getMinRate() > this.actualTopBandwidth + this.actualStandardDeviation)
				&& bandwidthValues.size() == 10 && this.actualBandwidth != 0) {
			port.getQueue().setMinRate(this.actualTopBandwidth);
			this.downLimit = this.actualAverageBandwidth - this.actualStandardDeviation;
		}

		if (this.actualBandwidth == 0 && port.isEntryFlow()) {
			port.getQueue().setBurst(this.actualBandwidth);
			port.getQueue().setMinRate(this.actualBandwidth);
			bandwidthValues.clear();
			SwitchController.removePolitic(port.getCategory().toString());
			port.setEntryFlow(false);
		}
	}

	@SuppressWarnings("unused")

	private long queueBandwidthMonitoring() {
		// TODO Auto-generated method stub
		int queue_id;
		if (port.getCategory().getPriority() != null)
			queue_id = port.getCategory().getPortNumber();
		else
			queue_id = 0;
		JSONObject queueValue = SwitchController.getQueueStatistics(switchId, queue_id);
		long first_duration_sec = queueValue.getLong("duration_sec");
		long first_tx_bytes = queueValue.getLong("tx_bytes");

		System.out.println("first_duration_sec : " + first_duration_sec + " first_tx_bytes: " + first_tx_bytes);

		try {
			Thread.sleep(this.port.getTime() * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		queueValue = SwitchController.getQueueStatistics(switchId, queue_id);

		long second_duration_sec = queueValue.getLong("duration_sec");
		long second_tx_bytes = queueValue.getLong("tx_bytes");

		System.out.println("second_duration_sec : " + second_duration_sec + " second_tx_bytes: " + second_tx_bytes);
		System.out.println("Variação do tempo: " + (second_duration_sec - first_duration_sec));

		long bandwidth = (long) (((second_tx_bytes - first_tx_bytes) * 8) / (second_duration_sec - first_duration_sec));

		System.out.println("Port: " + port.getCategory().getPortNumber() + " " + bandwidth + "bps");

		addBandwidthValues(bandwidth);

		return bandwidth;
	}

	@SuppressWarnings("unused")

	private long mediumUpStandardDeviation(long standardDeviation) {
		// TODO Auto-generated method stub
		double median = 0;
		int sum = 0;

		for (Long bandwidth : bandwidthValues) {
			if (bandwidth > (averageBandwidth() + standardDeviation)) {
				median += bandwidth;
				sum++;
			}
		}

		if (sum != 0) {
			median = median / sum;
			return (long) (Math.ceil(median));
		} else
			return (averageBandwidth() + standardDeviation);
	}

	@SuppressWarnings("unused")

	private long mediumDownStandardDeviation(long standardDeviation) {
		// TODO Auto-generated method stub
		double median = 0.0;
		int sum = 0;

		for (Long bandwidth : bandwidthValues) {
			if (bandwidth < (averageBandwidth() - standardDeviation)) {
				median += bandwidth;
				sum++;
			}
		}

		if (sum != 0) {
			median = median / sum;
			return (long) (Math.ceil(median));
		} else
			return (averageBandwidth() - standardDeviation);
	}

}
