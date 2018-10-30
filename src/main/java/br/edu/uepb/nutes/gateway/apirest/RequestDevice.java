package br.edu.uepb.nutes.gateway.apirest;

import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface RequestDevice extends ServerCommunication{
	
	default public JsonNode getAllDevices() throws UnirestException {
		// TODO Auto-generated method stub
		return getServerData("/devices/");
	}
	
	default public JsonNode getUserDevices(String userId) throws UnirestException {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		params.put("userId", userId);
				
		return getServerData("/devices/users/{userId}", params);
	}
	
	default public JsonNode getDataDevice(String deviceId) throws UnirestException {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		params.put("deviceId", deviceId);
				
		return getServerData("/devices/{deviceId}", params);
	}
	
	default public JsonNode getDataDeviceOfUser(String deviceId, String userId) throws UnirestException {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		params.put("deviceId", deviceId);
		params.put("userId", userId);
				
		return getServerData("/devices/{deviceId}/users/{userId}", params);
	}

}
