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
					System.out.println("Port:" + port.getNumber());
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
			if (port.getNumber()  == number) {
				port.setPeakDataRate(peakDataRte);
				return true;
			}
		}
		return false;
	}

	public static boolean updatePortMonitoring(int number, boolean actived) {

		for (Iterator<Port> iterator = portInformation.iterator(); iterator.hasNext();) {
			Port port = (Port) iterator.next();
			if (port.getNumber()  == number) {
				port.setActived(actived);
				return true;
			}
		}
		return false;
	}

	public static boolean updatePortMonitoring(int number, long peakDataRte, boolean actived) {

		for (Iterator<Port> iterator = portInformation.iterator(); iterator.hasNext();) {
			Port port = (Port) iterator.next();
			if (port.getNumber() == number && port.getPeakDataRate() < peakDataRte) {
				port.setPeakDataRate(peakDataRte);
				port.setActived(actived);
				return true;
			}
		}
		return false;
	}

	public static String getPortInformation() {

		String response = "";
		for (Iterator<Port> iterator = portInformation.iterator(); iterator.hasNext();) {
			Port port = (Port) iterator.next();
			response = response + "Port: " + port.getNumber() + " Peak Data Rate: " + port.getPeakDataRate()
					+ " Actived: " + port.isActived() + "\n";
		}

		response = response + "\n\n\n";

		return response;
	}
}
