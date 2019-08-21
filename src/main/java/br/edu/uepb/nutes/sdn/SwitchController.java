package br.edu.uepb.nutes.sdn;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SwitchController extends ServerCommunication {

	static public String MaxRateQoS;

	final static String OVS = "ovs-vsctl --db=tcp:192.168.2.1:6640";
	
	public static String getMaxrateqos() {
		return MaxRateQoS;
	}

	static public JsonNode insertPolitic(String switchId, String politicName, Port inPort) throws UnirestException {

		// "{'switch':'00:00:c0:25:e9:01:28:2a', 'name':'flow-mod-2', 'cookie':'0',
		// 'priority':'32768', 'in_port':'"+inPort+"','active':'true',
		// 'actions':'set_queue=123,output='"+outPort+"'}";

		Policy policy = new Policy(switchId);

		Actions actions = new Actions();
		actions.setOutput(String.valueOf(Category.MONITORING_CENTRAL.getPortNumber()));

		if (inPort.getCategory().getPriority() == null)
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

	static public JsonNode removeAllSwitchesPolitics() throws UnirestException {

		return getServerData("/wm/staticentrypusher/clear/all/json");

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
			proc = rt.exec(OVS + " list qos");
			proc.waitFor();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			response = stdInput.readLine();
			System.out.println(response);
			if (response != null && (proc.exitValue() == 0)) {
				proc = rt.exec(OVS + " clear port " + Category.MONITORING_CENTRAL.getInterfacePort()
						+ " qos -- --all destroy qos -- --all destroy queue");
				proc.waitFor();
			}

			if (proc.exitValue() == 0) {
				proc = rt.exec(OVS + " set port " + Category.MONITORING_CENTRAL.getInterfacePort()
						+ " qos=@newqos -- --id=@newqos create qos type=linux-htb other-config:max-rate=" + MaxRateQoS
						+ " queues:0=@newqueue"
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

	static public boolean createQueue(Port port, boolean onlyPriority) {

		Runtime rt = Runtime.getRuntime();
		Process proc = null;
		String response;
		try {
			if (port.getQueue().getUuidQosMonitoringCentral() == null) {
				proc = rt.exec(OVS + " get port " + Category.MONITORING_CENTRAL.getInterfacePort() + " qos ");
				proc.waitFor();

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

				response = stdInput.readLine();

				System.out.println(response);

				port.getQueue().setUuidQosMonitoringCentral(response);
			}

			int maxBandwidth = Integer.parseInt(MaxRateQoS);
			String command = OVS + " --id=@queue create queue";
			
			command += " other-config:priority=" + port.getCategory().getPriority();
			command += " other-config:max-rate=" + (maxBandwidth - (maxBandwidth/PortManager.getNumberPortWork()));

			if(!onlyPriority) {
				command += " other-config:min-rate=" + port.getQueue().getMinRate();
				
				command += " other-config:burst=" + port.getQueue().getBurst();
			}
			command += " -- set qos " + port.getQueue().getUuidQosMonitoringCentral() + " queue:"
					+ port.getCategory().getPortNumber() + "=@queue ";

			System.out.println(command);

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

	static public boolean deleteQueue(Port port) {

		Runtime rt = Runtime.getRuntime();
		Process proc = null;

		try {
			if (port.getQueue().getUuidQosMonitoringCentral() != null) {
				proc = rt.exec(OVS + " remove qos " + port.getQueue().getUuidQosMonitoringCentral() + " queue "
						+ port.getCategory().getPortNumber() + "=" + port.getQueue().getUuidQueueMonitoringCentral()
						+ " -- destroy queue " + port.getQueue().getUuidQueueMonitoringCentral());
				proc.waitFor();

				if (proc.exitValue() == 0) {
					port.getQueue().setUuidQosMonitoringCentral(null);
					port.getQueue().setUuidQueueMonitoringCentral(null);

					System.out.println(" Remoção de Fila Realizada COM SUCESSO!!!");

					return true;
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	static public boolean updateQueue(Port port, boolean onlyPriority) {

		Runtime rt = Runtime.getRuntime();
		Process proc = null;
		try {
			if (port.getQueue().getUuidQosMonitoringCentral() != null
					&& port.getQueue().getUuidQueueMonitoringCentral() != null) {
				
				String command = OVS + " set queue " + port.getQueue().getUuidQueueMonitoringCentral()
						+ " other-config:priority="	+ port.getCategory().getPriority();

				if(!onlyPriority)
					command += " other-config:min-rate=" + port.getQueue().getMinRate() 
							+ " other-config:burst=" + port.getQueue().getBurst();
				
				proc = rt.exec(command);
				proc.waitFor();

				if (proc.exitValue() == 0) {
//					port.setMinRateQueueMonitoringCentral(port.getPeakDataRate());
					System.out.println("Porta: " + port.getCategory().getPortNumber()
							+ " ATUALIZAÇÃO de Fila Realizada COM SUCESSO!!!");
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

	static public JSONObject getPortStatistics(String switchId, int port_id) {
		// TODO Auto-generated method stub
		JSONObject result = null;
		try {
			result = getServerData("/wm/core/switch/" + switchId + "/port/json").getObject();

			JSONArray portas = result.optJSONArray("port_reply").getJSONObject(0).optJSONArray("port");

			for (int i = 0; i < portas.length(); i++) {
				if (portas.getJSONObject(i).getString("port_number").equals(Integer.toString(port_id))) {
					return portas.getJSONObject(i);
				}
			}

		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	static public String getIp(String switchId, int port) {

		try {
			JSONObject result = getServerData("/wm/device/").getObject();

			for (Object iterable_element : result.getJSONArray("devices")) {
				JSONObject device = (JSONObject) iterable_element;
				if (device.getJSONArray("attachmentPoint").getJSONObject(0).getString("switch").equals(switchId)
						&& device.getJSONArray("attachmentPoint").getJSONObject(0).getString("port")
								.equals(String.valueOf(port)))
					return device.getJSONArray("ipv4").get(0).toString();
			}

		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	static public String getInterfacePort(String switchId, int port) {
		try {
			JSONObject result = getServerData("/wm/core/switch/" + switchId + "/port-desc/json").getObject();
			
			System.out.println(result);
			
			for (Object iterable_element : result.getJSONArray("port_desc")) {
				JSONObject actualPort = (JSONObject) iterable_element;
				
				if(actualPort.getString("port_number").equals(String.valueOf(port)))
					return actualPort.getString("name");
			}
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
