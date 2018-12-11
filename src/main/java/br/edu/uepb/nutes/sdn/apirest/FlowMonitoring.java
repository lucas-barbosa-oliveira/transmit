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
				JSONArray firstMeasurement = SwitchController
						.getStatistics(switchId, port.getCategory().getPortNumber()).getArray();
				Thread.sleep(this.port.getTime() * 1000);
				JSONArray secondMeasurement = SwitchController
						.getStatistics(switchId, port.getCategory().getPortNumber()).getArray();
				System.out.println("Port: " + port.getCategory().getPortNumber() + " Status: " + firstMeasurement);
				if (!firstMeasurement.isNull(0)) {
//					System.out.println(firstMeasurement.getJSONObject(0));

					long firstThroughput = firstMeasurement.getJSONObject(0).getLong("bits-per-second-tx")
							+ firstMeasurement.getJSONObject(0).getLong("bits-per-second-rx");
					long secondThroughput = secondMeasurement.getJSONObject(0).getLong("bits-per-second-tx")
							+ secondMeasurement.getJSONObject(0).getLong("bits-per-second-rx");

					if (port.getCategory().getPriority() != null) {

						if (secondThroughput < firstThroughput) {

							PortManager.updatePortMonitoring(port.getCategory().getPortNumber(), firstThroughput, true);

							System.out.println(" Port: " + port.getCategory().getPortNumber() + " FirstThroughput: "
									+ firstThroughput + " secondThroughput: " + secondThroughput);

							if (port.getUuidQueueMonitoringCentral() == null) {
								if (SwitchController.createQueue(port))
									SwitchController.insertPolitic(switchId, port.getCategory().toString(),
											port);
								System.out.println(PortManager.getPortInformation());
							} else {
								if (SwitchController.updateQueue(port))
									SwitchController.insertPolitic(switchId, port.getCategory().toString(),
										port);
								System.out.println(PortManager.getPortInformation());
							}
						}else if (firstThroughput == secondThroughput && port.isActived()) {
							if (firstThroughput == 0) {
								PortManager.updatePortMonitoring(port.getCategory().getPortNumber(), false);
								SwitchController.removePolitic(port.getCategory().toString());
							}else if(port.getMinRateQueueMonitoringCentral() < firstThroughput){
//								Caso o Tx1 e Tx2 sejam igual estão constantes sem necessáriamente usar toda largura de banda disponivel na fila							
								PortManager.updatePortMonitoring(port.getCategory().getPortNumber(), firstThroughput);
								SwitchController.updateQueue(port);
								System.out.println(PortManager.getPortInformation());
							}
						}

					}

				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
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
