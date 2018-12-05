package br.edu.uepb.nutes.haniot;

import java.net.URISyntaxException;
import java.util.Scanner;
//
//import com.mashape.unirest.http.exceptions.UnirestException;
//
//import br.edu.uepb.nutes.gateway.apirest.ServerCommunication;
//import br.edu.uepb.nutes.gateway.apirest.SuperUser;
import br.edu.uepb.nutes.jms.JmsMessageListener;
import br.edu.uepb.nutes.sdn.apirest.SwitchController;

public class Main {
	public static void main(String[] args) throws URISyntaxException, Exception {
		
		// Escutando alarmes emitidos pelos dispositivos simulados no OpenICE
		JmsMessageListener.initializingAlarmListening();
		
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
		
		SwitchController controller = new SwitchController();
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			System.out.println("Digite o número da Operação:\n"
					+ "1- Adicionar uma Política - 20 Mbps - Por Porta\n"
					+ "2- Adicionar uma Política - 10 Mbps - Por Porta\n"
					+ "3- Adicionar uma Política - 20 Mbps - Por IPv4\n"
					+ "4- Adicionar uma Política - 10 Mbps - Por IPv4\n"
					+ "5- Remover  uma Política - 20 Mbps\n"
					+ "6- Remover  uma Política - 10 Mbps\n"
					+ "7- Mostrar todas as Políticas de todos os Switches\n"
					+ "8- Mostra todas as Políticas de uim Switch\n"
					+ "9- Remover todas as Políticas de todos os Switches\n"
					+ "10- Remover todas as Políticas de um Switch\n"
					+ "11- Mostrar todos os dispositivos conectados\n"
					+ "12- Mostrar todos os Switches conectados");
			
			int option = scanner.nextInt();
			
			switch (option) {
			case 1:
				System.out.println(controller.highPriorityPolitic("192.168.3.3", 3, 1));
				break;
			case 2:
				System.out.println(controller.mediumPriorityPolitic("192.168.3.2", 2, 1));
				break;
			case 3:
				System.out.println(controller.highPriorityPolitic("192.168.3.3", "192.168.3.1"));
				break;
			case 4:
				System.out.println(controller.mediumPriorityPolitic("192.168.3.2", "192.168.3.1"));
				break;
			case 5:
//				System.out.println(controller.removeHighPriorityPolitic());
				break;
			case 6:
//				System.out.println(controller.removeMediumPriorityPolitic());
				break;
			case 7:
				System.out.println(controller.showAllSwitchesPolitics());
				break;
			case 8:
				System.out.println(controller.showAllSwitchPolitcs("00:00:c0:25:e9:01:28:2a"));
				break;
			case 9:
				System.out.println(controller.removeAllSwitchesPolitics());
				break;
			case 10:
				System.out.println(controller.removeAllSwitchPolitcs("00:00:c0:25:e9:01:28:2a"));
				break;
			case 11:
				System.out.println(controller.showAllDevicesOfSwitch());
				break;				
			case 12:
				System.out.println(controller.showAllSwitches());
				break;

			default:
				System.out.println("Nenhuma Opção Válida!!!");
				break;
			}
		}
		
	}
}
