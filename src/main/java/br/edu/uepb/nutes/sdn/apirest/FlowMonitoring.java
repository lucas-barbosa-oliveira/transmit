package br.edu.uepb.nutes.sdn.apirest;

import org.json.JSONArray;

public class FlowMonitoring extends ServerCommunication implements Runnable {

	private Thread flowManager;
	private String switchId;
	private Port port;

	public FlowMonitoring(String switchId, Port port) {
		// TODO Auto-generated constructor stub
		this.switchId = switchId;
		this.port = port;
	}

	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				JSONArray firstMeasurement = SwitchController.getStatistics(switchId, port.getNumber()).getArray();
				Thread.sleep(this.port.getTime() * 1000);
				JSONArray secondMeasurement = SwitchController.getStatistics(switchId, port.getNumber()).getArray();
				System.out.println("Port: " + port.getNumber() + " Status: " + firstMeasurement);
				if (!firstMeasurement.isNull(0)) {
					System.out.println(firstMeasurement.getJSONObject(0));

					long firstThroughput = firstMeasurement.getJSONObject(0).getLong("bits-per-second-tx")
							+ firstMeasurement.getJSONObject(0).getLong("bits-per-second-rx");
					long secondThroughput = secondMeasurement.getJSONObject(0).getLong("bits-per-second-tx")
							+ secondMeasurement.getJSONObject(0).getLong("bits-per-second-rx");
					System.out.println("Port: " + port.getNumber() + " FirstThroughput: " + firstThroughput
							+ " secondThroughput: " + secondThroughput);

					if (secondThroughput < firstThroughput) {
						boolean update = PortManager.updatePortMonitoring(port.getNumber(), firstThroughput, true);

						if (update && port.getUuidQueueMonitoringCentral() == null) {
							SwitchController.createQueue(port);	
							System.out.println(PortManager.getPortInformation());
						}
						else if (update) {
							SwitchController.updateQueue(port);
							System.out.println(PortManager.getPortInformation());
						}
					}

				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean initializeMonitoring() {
		flowManager = new Thread(this);
		flowManager.start();

		return false;
	}

	public boolean stopMonitoring() {
		flowManager = null;
		System.out.println("Break the Queue");
//		SwitchController.deleteQueue(port.getUuidQueueMonitoringCentral());
		return false;
	}

}
