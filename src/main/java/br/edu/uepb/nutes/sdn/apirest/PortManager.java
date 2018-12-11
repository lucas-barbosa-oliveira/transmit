package br.edu.uepb.nutes.sdn.apirest;

import java.util.ArrayList;
import java.util.Iterator;

import com.mashape.unirest.http.exceptions.UnirestException;

public class PortManager {

	private static ArrayList<Port> portInformation = new ArrayList<Port>();

	public PortManager(String switchId, ArrayList<Port> ports) {
		// TODO Auto-generated constructor stub

		try {
			if (SwitchController.initConfigurationSwitch() && SwitchController.initializeStatistiCollection()) {
				portInformation = ports;

				for (Port port : portInformation) {
					System.out.println("Port:" + port.getCategory().getPortNumber());
					port.initFlowMonitoring(switchId);
				}
			}
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			System.out.println("Não Habilitou a Coleta das Estatísticas");
		}

	}

	public static boolean updatePortMonitoring(int number, long peakDataRte) {

		for (Iterator<Port> iterator = portInformation.iterator(); iterator.hasNext();) {
			Port port = (Port) iterator.next();
			if (port.getCategory().getPortNumber() == number) {
				port.setPeakDataRate(peakDataRte*30/70);
				return true;
			}
		}
		return false;
	}

	public static boolean updatePortMonitoring(int number, boolean actived) {

		for (Iterator<Port> iterator = portInformation.iterator(); iterator.hasNext();) {
			Port port = (Port) iterator.next();
			if (port.getCategory().getPortNumber() == number) {
				port.setActived(actived);
				return true;
			}
		}
		return false;
	}

	public static boolean updatePortMonitoring(int number, long peakDataRte, boolean actived) {

		for (Iterator<Port> iterator = portInformation.iterator(); iterator.hasNext();) {
			Port port = (Port) iterator.next();
			if (port.getCategory().getPortNumber() == number) {
				long weightedAverage = (port.getPeakDataRate() * 3 + peakDataRte * 7) / 10;
				port.setPeakDataRate(weightedAverage);
				port.setActived(actived);
				return true;
			}
		}
		return false;
//		(6*3+6*7)/10 = 6
//		(((4+8+6)/2))/2 = 4,4
	}

	public static String getPortInformation() {

		String response = "";
		for (Iterator<Port> iterator = portInformation.iterator(); iterator.hasNext();) {
			Port port = (Port) iterator.next();
			response = response + "Port: " + port.getCategory().getPortNumber() + " Peak Data Rate: "
					+ port.getPeakDataRate() + " Actived: " + port.isActived() + "\n";
		}

		response = response + "\n\n\n";

		return response;
	}
}
