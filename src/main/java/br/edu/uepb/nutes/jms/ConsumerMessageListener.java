package br.edu.uepb.nutes.jms;

import java.util.Iterator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.json.JSONObject;

import com.google.gson.Gson;

public class ConsumerMessageListener implements MessageListener {
	private String consumerName;

	public ConsumerMessageListener(String consumerName) {
		this.consumerName = consumerName;
	}

	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			
			JSONObject alarm= new JSONObject(textMessage.getText());
			
			System.out.println(alarm.toString());
			String deviceId=null;
			for (Iterator<String> iterator = alarm.keys(); iterator.hasNext();) {
				deviceId = (String) iterator.next();
			}
			
			if (deviceId!=null) {
				System.out.println(alarm.getJSONObject(deviceId).get("Text").toString().equals("LOW")?"BAIXO":"NAO SEI");
			}
			

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
