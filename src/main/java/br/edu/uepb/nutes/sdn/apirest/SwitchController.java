package br.edu.uepb.nutes.sdn.apirest;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SwitchController extends ServerCommunication{

	public JsonNode highPriorityPolitic(String ipSource, int inPort, int outPort) throws UnirestException {	

		//		 "{'switch':'00:00:c0:25:e9:01:28:2a', 'name':'flow-mod-2', 'cookie':'0', 'priority':'32768', 'in_port':'"+inPort+"','active':'true', 'actions':'set_queue=123,output='"+outPort+"'}";

		Policy policy = new Policy("00:00:c0:25:e9:01:28:2a");
		
		Actions actions = new Actions();
		actions.setOutput(String.valueOf(outPort));
		actions.setSet_queue("234");

		policy.setName(ipSource);
		policy.setInPort(String.valueOf(inPort));
		policy.setActions(actions);

		Gson gson = new Gson();

		String policyJson = gson.toJson(policy).replaceFirst("switchId", "switch");

		System.out.println(policyJson);

		return postServerData("/wm/staticentrypusher/json", policyJson);

	}
	
	public JsonNode highPriorityPolitic(String ipSource, String ipDestiny) throws UnirestException {	

		Policy policy = new Policy("00:00:c0:25:e9:01:28:2a");
		
		Actions actions = new Actions();
		actions.setSet_queue("234");
		actions.setOutput(String.valueOf(1));

		policy.setName(ipSource);
		policy.setIpv4_src(ipSource);
		policy.setIpv4_dst(ipDestiny);
		policy.setEth_type("0x0800");
		policy.setActions(actions);

		Gson gson = new Gson();

		String policyJson = gson.toJson(policy).replaceFirst("switchId", "switch");

		System.out.println(policyJson);

		return postServerData("/wm/staticentrypusher/json", policyJson);

	}

	public JsonNode mediumPriorityPolitic(String ipSource, int inPort, int outPort) throws UnirestException {	

		Policy policy = new Policy("00:00:c0:25:e9:01:28:2a");
		
		Actions actions = new Actions();
		actions.setOutput(String.valueOf(outPort));
		actions.setSet_queue("123");

		policy.setName(ipSource);
		policy.setInPort(String.valueOf(inPort));
		policy.setActions(actions);

		Gson gson = new Gson();

		String policyJson = gson.toJson(policy).replaceFirst("switchId", "switch");

		System.out.println(policyJson);

		return postServerData("/wm/staticentrypusher/json", policyJson);

	}
	
	public JsonNode mediumPriorityPolitic(String ipSource, String ipDestiny) throws UnirestException {	

		Policy policy = new Policy("00:00:c0:25:e9:01:28:2a");
		
		Actions actions = new Actions();
		actions.setSet_queue("123");
		actions.setOutput(String.valueOf(1));

		policy.setName(ipSource);
		policy.setIpv4_src(ipSource);
		policy.setIpv4_dst(ipDestiny);
		policy.setEth_type("0x0800");
		policy.setActions(actions);

		Gson gson = new Gson();

		String policyJson = gson.toJson(policy).replaceFirst("switchId", "switch");

		System.out.println(policyJson);

		return postServerData("/wm/staticentrypusher/json", policyJson);

	}

	public JsonNode removeHighPriorityPolitic(String ipSource) throws UnirestException {	

		JSONObject body = new JSONObject();
		body.put("name", ipSource);

		return deleteServerData("/wm/staticentrypusher/json", body);

	}

	public JsonNode removeMediumPriorityPolitic(String ipSource) throws UnirestException {	

		JSONObject body = new JSONObject();
		body.put("name", ipSource);

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
