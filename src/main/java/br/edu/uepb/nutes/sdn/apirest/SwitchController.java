package br.edu.uepb.nutes.sdn.apirest;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SwitchController extends ServerCommunication{

	Policy policy = new Policy("00:00:c0:25:e9:01:28:2a");

	public JsonNode highPriorityPolitic(int inPort, int outPort) throws UnirestException {	

		//		 "{'switch':'00:00:c0:25:e9:01:28:2a', 'name':'flow-mod-2', 'cookie':'0', 'priority':'32768', 'in_port':'"+inPort+"','active':'true', 'actions':'set_queue=123,output='"+outPort+"'}";

		Actions actions = new Actions();
		actions.setOutput(String.valueOf(outPort));
		actions.setSet_queue("123");

		policy.setName("queue20Mbps");
		policy.setInPort(String.valueOf(inPort));
		policy.setActions(actions);

		Gson gson = new Gson();

		String policyJson = gson.toJson(policy).replaceFirst("switchId", "switch");

		System.out.println(policyJson);

		return postServerData("/wm/staticentrypusher/json", policyJson);

	}
	
	public JsonNode highPriorityPolitic(String ipSource, String ipDestiny) throws UnirestException {	

		Actions actions = new Actions();
		actions.setSet_queue("123");

		policy.setName("queue20Mbps");
		policy.setIpv4_src(ipSource);
		policy.setIpv4_dst(ipDestiny);
		policy.setActions(actions);

		Gson gson = new Gson();

		String policyJson = gson.toJson(policy).replaceFirst("switchId", "switch");

		System.out.println(policyJson);

		return postServerData("/wm/staticentrypusher/json", policyJson);

	}

	public JsonNode mediumPriorityPolitic(int inPort, int outPort) throws UnirestException {	

		Actions actions = new Actions();
		actions.setOutput(String.valueOf(outPort));
		actions.setSet_queue("123");

		policy.setName("queue10Mbps");
		policy.setInPort(String.valueOf(inPort));
		policy.setActions(actions);

		Gson gson = new Gson();

		String policyJson = gson.toJson(policy).replaceFirst("switchId", "switch");

		System.out.println(policyJson);

		return postServerData("/wm/staticentrypusher/json", policyJson);

	}
	
	public JsonNode mediumPriorityPolitic(String ipSource, String ipDestiny) throws UnirestException {	

		Actions actions = new Actions();
		actions.setSet_queue("123");

		policy.setName("queue20Mbps");
		policy.setIpv4_src(ipSource);
		policy.setIpv4_dst(ipDestiny);
		policy.setActions(actions);

		Gson gson = new Gson();

		String policyJson = gson.toJson(policy).replaceFirst("switchId", "switch");

		System.out.println(policyJson);

		return postServerData("/wm/staticentrypusher/json", policyJson);

	}

	public JsonNode removeHighPriorityPolitic() throws UnirestException {	

		JSONObject body = new JSONObject();
		body.put("name", "queue20Mbps");

		return deleteServerData("/wm/staticentrypusher/json", body);

	}

	public JsonNode removeMediumPriorityPolitic() throws UnirestException {	

		JSONObject body = new JSONObject();
		body.put("name", "queue10Mbps");

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
