package br.edu.uepb.nutes.jms;

import java.util.Iterator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;

import br.edu.uepb.nutes.sdn.apirest.SwitchController;

public class ConsumerMessageListener implements MessageListener {
	private String consumerName;
	private static String hostIp = "127.0.0.1";

	public ConsumerMessageListener(String consumerName) {
		this.consumerName = consumerName;
	}

	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			
			JSONObject alarm= new JSONObject(textMessage.getText());
			
			String deviceId=null;
			for (Iterator<String> iterator = alarm.keys(); iterator.hasNext();) {
				deviceId = (String) iterator.next();
			}
			
			if (deviceId!=null) {
				SwitchController controller = new SwitchController();
				
				if(!alarm.getJSONObject(deviceId).get("Text").toString().equals("NORMAL")) {
					controller.mediumPriorityPolitic(alarm.getJSONObject(deviceId).get("Address").toString(), hostIp);
				}else {
					controller.removeMediumPriorityPolitic(alarm.getJSONObject(deviceId).get("Address").toString());
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
