package br.edu.uepb.nutes.sdn.apirest;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.RequestBodyEntity;

public class SwitchController extends ServerCommunication{

	public JsonNode highPriorityPolitic(int inPort, int outPort) throws UnirestException {	

		String policy = "{'switch':'00:00:c0:25:e9:01:28:2a', 'name':'flow-mod-2', 'cookie':'0', 'priority':'32768', 'in_port':'"+inPort+"','active':'true', 'actions':'set_queue=123,output='"+outPort+"'}";
		return postServerData("/wm/staticentrypusher/json", policy);

	}
	
	public JsonNode mediumPriorityPolitic(int inPort, int outPort) throws UnirestException {	

		String policy = "{'switch':'00:00:c0:25:e9:01:28:2a', 'name':'flow-mod-2', 'cookie':'0', 'priority':'32768', 'in_port':'"+inPort+"','active':'true', 'actions':'set_queue=234,output='"+outPort+"'}";
		return postServerData("/wm/staticentrypusher/json", policy);

	}
	
	public JsonNode removePolitic(String name) throws UnirestException {	
		
		JSONObject body = new JSONObject();
		body.put("name", name);

		return deleteServerData("/wm/staticentrypusher/json", body);

	}

	public JsonNode showAllSwitchesPolitics() throws UnirestException {

		return getServerData("/wm/staticflowpusher/list/all/json");

	}

	public JsonNode showAllSwitchPolitcs(String switchAddress) throws UnirestException {

		JSONObject params = new JSONObject();
		params.put("switchAddress", switchAddress);

		return getServerData("/wm/staticflowpusher/list/{switchAddress}/json", params);

	}

	public JsonNode removeAllSwitchesPolitics() throws UnirestException {
		
		return getServerData("/wm/staticentrypusher/clear/all/json");

	}

	public JsonNode removeAllSwitchPolitcs(String switchAddress) throws UnirestException {
	
		JSONObject params = new JSONObject();
		params.put("switchAddress", switchAddress);

		return getServerData("/wm/staticflowpusher/clear/{switchAddress}/json", params);

	}

	public JsonNode showAllDevicesOfSwitch() throws UnirestException {

		return getServerData("/wm/device/");

	}

	public JsonNode showAllSwitches() throws UnirestException {
		
		return getServerData("/wm/core/controller/switches/json");
		
	}
}
