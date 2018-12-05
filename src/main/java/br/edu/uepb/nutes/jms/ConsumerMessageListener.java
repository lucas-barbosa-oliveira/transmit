package br.edu.uepb.nutes.jms;

import java.util.Iterator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

import br.edu.uepb.nutes.sdn.apirest.SwitchController;

public class ConsumerMessageListener implements MessageListener {
	@SuppressWarnings("unused")
	private String consumerName;
	private static String hostIp = "192.168.3.1";

	public ConsumerMessageListener(String consumerName) {
		this.consumerName = consumerName;
	}

	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			
			JSONObject alarm= new JSONObject(textMessage.getText());
			
			System.out.println(alarm);
			
			String deviceId=null;
			for (Iterator<String> iterator = alarm.keys(); iterator.hasNext();) {
				deviceId = (String) iterator.next();
			}
			
			if (deviceId!=null) {
				SwitchController controller = new SwitchController();
				System.out.println(controller.showAllSwitchesPolitics());
				
				if(!alarm.getJSONObject(deviceId).get("Text").toString().equals("NORMAL")) {
					controller.mediumPriorityPolitic(alarm.getJSONObject(deviceId).get("Address").toString().replace(" ", ""), hostIp);
				}else {
					controller.removeMediumPriorityPolitic(alarm.getJSONObject(deviceId).get("Address").toString().replace(" ", ""));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}  catch (JMSException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			System.out.println("Problema na conexão com o Controlador");
//			e.printStackTrace();
		}
	}

}
