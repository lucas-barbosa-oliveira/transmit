package br.edu.uepb.nutes.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import br.edu.uepb.nutes.sdn.Category;
import br.edu.uepb.nutes.sdn.Port;
import br.edu.uepb.nutes.sdn.PortManager;
import br.edu.uepb.nutes.sdn.SwitchController;

public class Main {
	public static void main(String[] args) throws URISyntaxException, Exception {
//		SwitchController.MaxRateQoS = "100000000";
		SwitchController.MaxRateQoS = "500000000";
		
//		Port port2 = new Port(Category.MEDICAL_DEVICES, 2);
//		Port port3 = new Port(Category.CALL_VOIP, 2);
//		Port port4 = new Port(Category.UDP_TRAFFIC, 2);
//		
		Port port2 = new Port(Category.MEDICAL_DEVICES, 2);
		Port port3 = new Port(Category.DICOM_IMAGES, 2);
		Port port4 = new Port(Category.UDP_TRAFFIC, 2);
		
//		Port port2 = new Port(Category.RMI_IMAGES, 2);
//		Port port3 = new Port(Category.DICOM_IMAGES, 2);
		
//		Port port2 = new Port(Category.MEDICAL_DEVICES, 2);
//		Port port3 = new Port(Category.DICOM_IMAGES, 2);
//		Port port4 = new Port(Category.DICOM_IMAGES_SECOND, 2);
		
		ArrayList<Port> ports = new ArrayList<Port>(Arrays.asList(port2, port3,port4));
//		ArrayList<Port> ports = new ArrayList<Port>(Arrays.asList(port2, port3));
		try {
			@SuppressWarnings("unused")
			PortManager manager = new PortManager("00:00:c0:25:e9:01:28:2a", ports);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println();
		}


//		long start = System.currentTimeMillis();
//		
//		// Requisitando informações ao servidor		
//		SuperUser rootUser = new SuperUser("Root", 1, 46244230000L, 177, "root@mail.com","1234",1);
//				
//		System.out.println(rootUser.getAllUsers());
//		System.out.println(rootUser.getUserId("5"));
//		
//		System.out.println(rootUser.getAllDevices());
//		System.out.println(rootUser.getUserDevices("1"));
//		System.out.println(rootUser.getDataDevice("1"));
//		System.out.println(rootUser.getDataDeviceOfUser("1","1"));
//		
//		System.out.println(rootUser.getAllMeasurements());
//		System.out.println(rootUser.getDeviceMeasurements("1"));
//		System.out.println(rootUser.getMeasurementOfType("1"));
//		System.out.println(rootUser.getTypeMeasurementOfUser("1","1"));
//		
//		long elapsed = System.currentTimeMillis() - start;
//		
//		System.out.println();
//		System.out.println("Tempo de execução: " + elapsed);


	}
}
