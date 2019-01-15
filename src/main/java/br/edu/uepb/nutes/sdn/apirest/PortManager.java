package br.edu.uepb.nutes.sdn.apirest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
//import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

//import br.edu.uepb.nutes.jms.ConsumerMessageListener;

public class PortManager implements MessageListener {

	private static ArrayList<Port> portInformation = new ArrayList<Port>();

	private static ArrayList<FlowMonitoring> flowMonitoring = new ArrayList<FlowMonitoring>();
//	private static String hostIp = "192.168.3.1";

	public PortManager(String switchId, ArrayList<Port> ports) {
		// TODO Auto-generated constructor stub
		try {
			initializingAlarmListening();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("Não Inicializou o monitoramento de alarmes");
		}
		try {
			if (SwitchController.initConfigurationSwitch() && SwitchController.initializeStatistiCollection()) {
				portInformation = ports;

				for (Port port : portInformation) {
					System.out.println("Port:" + port.getCategory().getPortNumber());
//					port.initFlowMonitoring(switchId);

					flowMonitoring.add(new FlowMonitoring(switchId, port).initializeMonitoring());
				}
			}
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			System.out.println("Não Habilitou a Coleta das Estatísticas");
		}
	}

	private void initializingAlarmListening() throws URISyntaxException, Exception {
		BrokerService broker = BrokerFactory.createBroker(new URI("broker:(tcp://192.168.3.1:61616)"));
		broker.start();
		Connection connection = null;
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.3.1:61616");
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("alarms");

			// Consumer
			MessageConsumer consumer = session.createConsumer(queue);
			consumer.setMessageListener(this);
			connection.start();
			Thread.sleep(1000);
		} catch (Exception e) {
			System.out.println();
			System.out.println(e);
			System.out.println();
			System.out.println("Erro na conexão!");
		}
	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		TextMessage textMessage = (TextMessage) message;
		try {

			JSONObject alarm = new JSONObject(textMessage.getText());

			System.out.println(alarm);

			String deviceId = null;
			for (Iterator<String> iterator = alarm.keys(); iterator.hasNext();) {
				deviceId = (String) iterator.next();
			}

			if (deviceId != null) {
				String address = alarm.getJSONObject(deviceId).get("Address").toString().replace(" ", "");
				for (FlowMonitoring portMonitor : flowMonitoring)
					if (portMonitor.getPort().getAddress() != null)
						if (portMonitor.getPort().getAddress().equals(address))
							if (!alarm.getJSONObject(deviceId).get("Alarm").toString().equals("NORMAL"))
								portMonitor.changePriority(true);
							else
								portMonitor.changePriority(false);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	private void changePriority(String address, boolean isAlarming) {
//		// TODO Auto-generated method stub
//		for (Port port : portInformation) {
//			System.out.println(port.getAddress());
//			if (port.getAddress().equals(address)) {
//				if (port.getCategory().activeAlarm(isAlarming)) {
//					System.out.println("Atualizar Prioridade");
//					SwitchController.updateQueue(port);
//				}
//			}
//		}
//	}

//	private static void jmsCommunication(JSONObject device) throws JMSException   {
//		// TODO Auto-generated method stub
//		Connection connection = null;
//		try {
//			// Producer
//			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
//					"tcp://localhost:61616");
//			connection = connectionFactory.createConnection();
//			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//			Queue queue = session.createQueue("openiceAlarms");
//
//			Message msg = session.createTextMessage(device.toString());
//			MessageProducer producer = session.createProducer(queue);
//			System.out.println("Sending text '" + device.toString() + "'");
//			producer.send(msg);
//
//
//		} finally {
//			if (connection != null) {
//				connection.close();
//			}
////			broker.stop();
//		}
//
//	}
}
