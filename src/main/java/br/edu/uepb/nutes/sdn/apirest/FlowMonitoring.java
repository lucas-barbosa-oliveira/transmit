package br.edu.uepb.nutes.sdn.apirest;

//import org.json.JSONArray;
import org.json.JSONObject;

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
		try {
			if (port.getCategory().getPriority() != null)
				SwitchController.createQueue(port);
			
			SwitchController.insertPolitic(switchId, port.getCategory().toString(), port);
			port.setEntryFlow(true);

			while (true) {
				int actualBandwidth = bandwidthMonitoring();

				if (port.getCategory().getPriority() != null) {
					if (port.getQueue().getMinRate() < actualBandwidth) {
						port.getQueue().setMinRate(actualBandwidth);
						port.getQueue().setBurst(actualBandwidth);
						SwitchController.updateQueue(port);
						if (!port.isEntryFlow()) {
							SwitchController.insertPolitic(switchId, port.getCategory().toString(), port);
							port.setEntryFlow(true);
						}
					}

					if (actualBandwidth == 0 && port.isEntryFlow()) {
						SwitchController.removePolitic(port.getCategory().toString());
						port.getQueue().setMinRate(actualBandwidth);
						port.getQueue().setBurst(actualBandwidth);
						SwitchController.updateQueue(port);
						port.setEntryFlow(false);
					}
				} else {
					if (port.getQueue().getMinRate() < actualBandwidth) {
						port.getQueue().setMinRate(actualBandwidth);
						port.getQueue().setBurst(actualBandwidth);
						if (!port.isEntryFlow()) {
							SwitchController.insertPolitic(switchId, port.getCategory().toString(), port);
							port.setEntryFlow(true);
						}
					}

					if (actualBandwidth == 0 && port.isEntryFlow()) {
						SwitchController.removePolitic(port.getCategory().toString());
						port.getQueue().setMinRate(actualBandwidth);
						port.getQueue().setBurst(actualBandwidth);
						port.setEntryFlow(false);
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//		while (true) {
//			try {
//				JSONArray firstMeasurement = SwitchController
//						.getStatistics(switchId, port.getCategory().getPortNumber()).getArray();
//				Thread.sleep(this.port.getTime() * 1000);
//				JSONArray secondMeasurement = SwitchController
//						.getStatistics(switchId, port.getCategory().getPortNumber()).getArray();
//				System.out.println("Port: " + port.getCategory().getPortNumber() + " Status: " + firstMeasurement);
//				if (!firstMeasurement.isNull(0)) {
////					System.out.println(firstMeasurement.getJSONObject(0));
//
//					long firstThroughput = firstMeasurement.getJSONObject(0).getLong("bits-per-second-tx")
//							+ firstMeasurement.getJSONObject(0).getLong("bits-per-second-rx");
//					long secondThroughput = secondMeasurement.getJSONObject(0).getLong("bits-per-second-tx")
//							+ secondMeasurement.getJSONObject(0).getLong("bits-per-second-rx");
//
//					if (port.getCategory().getPriority() != null) {
//
//						if (secondThroughput < firstThroughput) {
//
//							PortManager.updatePortMonitoring(port.getCategory().getPortNumber(), firstThroughput, true);
//
//							System.out.println(" Port: " + port.getCategory().getPortNumber() + " FirstThroughput: "
//									+ firstThroughput + " secondThroughput: " + secondThroughput);
//
//							if (port.getQueue().getUuidQueueMonitoringCentral() == null) {
//								if (SwitchController.createQueue(port)) {
//									SwitchController.insertPolitic(switchId, port.getCategory().toString(), port);
//									port.setEntryFlow(true);
//								}
//
//								System.out.println(PortManager.getPortInformation());
//							} else {
//								if (SwitchController.updateQueue(port)) {
//									SwitchController.insertPolitic(switchId, port.getCategory().toString(), port);
//									port.setEntryFlow(true);
//								}
//								System.out.println(PortManager.getPortInformation());
//							}
//						} else if (firstThroughput == secondThroughput && port.isActived()) {
//							if (firstThroughput == 0) {
//								PortManager.updatePortMonitoring(port.getCategory().getPortNumber(), false);
//								SwitchController.removePolitic(port.getCategory().toString());
//								port.setEntryFlow(false);
//							} else if (port.getMinRateQueueMonitoringCentral() < firstThroughput) {
////								Caso o Tx1 e Tx2 sejam igual estão constantes sem necessáriamente usar toda largura de banda disponivel na fila							
//								PortManager.updatePortMonitoring(port.getCategory().getPortNumber(), firstThroughput);
//								SwitchController.updateQueue(port);
//								System.out.println(PortManager.getPortInformation());
//							}
//						}
//
//					} else if (!port.isEntryFlow()) {
//						SwitchController.insertPolitic(switchId, port.getCategory().toString(), port);
//						port.setEntryFlow(true);
//					} else if (firstThroughput == 0 && secondThroughput == 0) {
//						SwitchController.removePolitic(port.getCategory().toString());
//					}
//
//				}
//
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	}

	public boolean initializeMonitoring() {
		flowManager = new Thread(this);
		flowManager.start();

		return false;
	}

	private int bandwidthMonitoring() {
		// TODO Auto-generated method stub
		JSONObject queueValue;
		
		if(port.getCategory().getPriority() != null)
			queueValue = SwitchController.getPortStatistics(switchId, port.getCategory().getPortNumber());
		else
			queueValue = SwitchController.getPortStatistics(switchId, 0);
		
		long first_duration_sec = queueValue.getLong("duration_sec");
		long first_tx_bytes = queueValue.getLong("tx_bytes");

		System.out.println("first_duration_sec : " + first_duration_sec + " first_tx_bytes: " + first_tx_bytes);

		try {
			Thread.sleep(this.port.getTime() * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		queueValue = SwitchController.getPortStatistics(switchId, port.getCategory().getPortNumber());

		long second_duration_sec = queueValue.getLong("duration_sec");
		long second_tx_bytes = queueValue.getLong("tx_bytes");

		System.out.println("second_duration_sec : " + second_duration_sec + " second_tx_bytes: " + second_tx_bytes);

		int bandwidth = (int) (((second_tx_bytes - first_tx_bytes) * 8) / (second_duration_sec - first_duration_sec));

		System.out.println(bandwidth + "bps");

		return bandwidth;
	}

	public boolean stopMonitoring() {
		flowManager = null;
		System.out.println("Break the Queue");
//		SwitchController.deleteQueue(port.getUuidQueueMonitoringCentral());
		return false;
	}

}
