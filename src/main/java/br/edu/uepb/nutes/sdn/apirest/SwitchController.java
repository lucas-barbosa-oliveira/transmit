package br.edu.uepb.nutes.sdn.apirest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SwitchController extends ServerCommunication {
	
	final static String OVS = "ovs-vsctl --db=ptcp:6640";
	
//	final static String OVS = "ovs-vsctl --db=tcp:192.168.2.1:6640";

	static public JsonNode insertPolitic(String switchId, String politicName, Port inPort) throws UnirestException {

		// "{'switch':'00:00:c0:25:e9:01:28:2a', 'name':'flow-mod-2', 'cookie':'0',
		// 'priority':'32768', 'in_port':'"+inPort+"','active':'true',
		// 'actions':'set_queue=123,output='"+outPort+"'}";

		Policy policy = new Policy(switchId);

		Actions actions = new Actions();
		actions.setOutput(String.valueOf(Category.MONITORING_CENTRAL.getPortNumber()));
		
		if(inPort.getCategory().getPriority() == null)
			actions.setSet_queue("0");
		else
			actions.setSet_queue(Integer.toString(inPort.getCategory().getPortNumber()));

		policy.setName(politicName);
		policy.setInPort(String.valueOf(inPort.getCategory().getPortNumber()));
		policy.setActions(actions);

		Gson gson = new Gson();

		String policyJson = gson.toJson(policy).replaceFirst("switchId", "switch");

		System.out.println(policyJson);

		return postServerData("/wm/staticentrypusher/json", policyJson);

	}
	
	static public JsonNode removePolitic(String politicName) throws UnirestException {

		JSONObject body = new JSONObject();
		body.put("name", politicName);

		System.out.println(body);

		return deleteServerData("/wm/staticentrypusher/json", body);

	}

	public JsonNode highPriorityPolitic(String ipSource, int inPort, int outPort) throws UnirestException {

		// "{'switch':'00:00:c0:25:e9:01:28:2a', 'name':'flow-mod-2', 'cookie':'0',
		// 'priority':'32768', 'in_port':'"+inPort+"','active':'true',
		// 'actions':'set_queue=123,output='"+outPort+"'}";

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

		System.out.println(body);

		return deleteServerData("/wm/staticentrypusher/json", body);

	}

	public JsonNode removeMediumPriorityPolitic(String ipSource) throws UnirestException {

		JSONObject body = new JSONObject();
		body.put("name", ipSource);

		System.out.println(body);

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

	static public JsonNode removeAllSwitchesPolitics() throws UnirestException {

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

	static public boolean initializeStatistiCollection() throws UnirestException {

		JsonNode firstResponse, secondResponse, thirdResponse;

		firstResponse = ServerCommunication.postServerData("/wm/statistics/config/disable/", "");

		secondResponse = ServerCommunication.postServerData("/wm/statistics/config/port/2/", "");

		thirdResponse = ServerCommunication.postServerData("/wm/statistics/config/enable/", "");

		if (firstResponse.getObject().get("statistics-collection").toString().equals("disabled")
				&& secondResponse.getObject().get("status").toString().endsWith("2")
				&& thirdResponse.getObject().get("statistics-collection").toString().equals("enabled")) {
			return true;
		}

		else {
			return false;
		}

	}

	static public JsonNode getStatistics(String switchId, int port) {

		try {
			return getServerData("/wm/statistics/bandwidth/" + switchId + "/" + port + "/");
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	static public boolean initConfigurationSwitch() {
		
		try {
			removeAllSwitchesPolitics();
		} catch (UnirestException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Runtime rt = Runtime.getRuntime();
		Process proc = null;
		String response;
		try {
			proc = rt.exec(OVS +" list qos");
			proc.waitFor();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			response = stdInput.readLine();
			System.out.println(response);
			if (response != null && (proc.exitValue() == 0)) {
				proc = rt.exec(OVS +" clear port " + Category.MONITORING_CENTRAL.getInterfacePort()
						+ " qos -- --all destroy qos -- --all destroy queue");
				proc.waitFor();
			}

			if (proc.exitValue() == 0) {
				proc = rt.exec(OVS +" set port " + Category.MONITORING_CENTRAL.getInterfacePort()
						+ " qos=@newqos -- --id=@newqos create qos type=linux-htb other-config:max-rate=50000000 queues:0=@newqueue"
						+ " -- --id=@newqueue create queue other-config:min-rate=0 other-config:priority=10");
				proc.waitFor();
				return true;
			}

			System.out.println(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	static public boolean createQueue(Port port) {

		Runtime rt = Runtime.getRuntime();
		Process proc = null;
		String response;
		try {
			if (port.getQueue().getUuidQosMonitoringCentral() == null) {
				proc = rt.exec(OVS +" get port " + Category.MONITORING_CENTRAL.getInterfacePort()
						+ " qos ");
				proc.waitFor();

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

				response = stdInput.readLine();

				System.out.println(response);

				port.getQueue().setUuidQosMonitoringCentral(response);
			}

			String command = OVS +" --id=@queue create queue";

			command += " other-config:min-rate=" + port.getQueue().getMinRate();
			
			command += " other-config:priority=" + port.getCategory().getPriority();
			
			command += " other-config:burst=" + port.getQueue().getBurst();

			command += " -- set qos " + port.getQueue().getUuidQosMonitoringCentral() + " queue:" + port.getCategory().getPortNumber() + "=@queue ";

			proc = rt.exec(command);
			proc.waitFor();

			response = new BufferedReader(new InputStreamReader(proc.getInputStream())).readLine();

			boolean result = proc.exitValue() == 0;

			if (result) {
//				port.getQueue().setMinRate(port.getThroughputRate();
				port.getQueue().setUuidQueueMonitoringCentral(response);
				System.out.println("Porta: " + port.getCategory().getPortNumber() + " UUID: " + response
						+ " Criação de Fila Realizada COM SUCESSO!!!");
				return true;
			} else {
				System.out.println(" Criação de Fila Realizada SEM SUCESSO!!!");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	static public boolean updateQueue(Port port) {

		Runtime rt = Runtime.getRuntime();
		Process proc = null;
		try {
			if (port.getQueue().getUuidQosMonitoringCentral() != null && port.getQueue().getUuidQueueMonitoringCentral() != null) {
				proc = rt.exec(OVS + " set queue " + port.getQueue().getUuidQueueMonitoringCentral()
						+ " other-config:min-rate=" + port.getQueue().getMinRate()
						+ " other-config:priority=" +  port.getCategory().getPriority()
						+ " other-config:burst=" + port.getQueue().getBurst());
				proc.waitFor();

				if (proc.exitValue() == 0) {
//					port.setMinRateQueueMonitoringCentral(port.getPeakDataRate());
					System.out.println("Porta: " + port.getCategory().getPortNumber() + " ATUALIZAÇÃO de Fila Realizada COM SUCESSO!!!");
					return true;
				} else {
					System.out.println(" ATUALIZAÇÃO de Fila Realizada SEM SUCESSO!!!");
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	static public JSONObject getPortStatistics(String switchId, int queue_id) {
		try {
			
			String result = getsServerData("/wm/core/switch/" + switchId + "/queue/json").replaceAll("\"\"","\",\"").replaceAll("]}","");
			
			result = result.split(Pattern.quote("["))[1].replaceAll(",\"duration_nsec\"", "-\"duration_nsec\"").replaceAll("\"", "");
			
			String [] queues = result.split("-");
			
			ArrayList<JSONObject> queueJson = new ArrayList<JSONObject>();
			
			for (int i = 0; i < queues.length; i++) {
				JSONObject pacialQueue = new JSONObject();
				String [] queueValues = queues[i].split(",");
				for(int j = 0; j < queueValues.length; j=j+2) {
					if(queueValues[j].equals("tx_errors"))
						pacialQueue.append(queueValues[j], Long.parseLong(queueValues[j+1]));
					else
						pacialQueue.put(queueValues[j], Long.parseLong(queueValues[j+1]));
				}
				queueJson.add(pacialQueue);
			}
			
			for (JSONObject queue : queueJson) {
				if(queue.getInt("queue_id") == queue_id)
					return queue;
			}
			return null;
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
