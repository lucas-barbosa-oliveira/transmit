package br.edu.uepb.nutes.gateway.apirest;

import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface RequestMeasurement extends ServerCommunication{
	
	default public JsonNode getAllMeasurements() throws UnirestException {
		// TODO Auto-generated method stub			
		return getServerData("/measurements/");
	}
	
	default public JsonNode getDeviceMeasurements(String deviceId) throws UnirestException {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		params.put("deviceId", deviceId);
				
		return getServerData("/measurements/devices/{deviceId}", params);
	}
	
	default public JsonNode getMeasurementOfType(String typeId) throws UnirestException {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		params.put("typeId", typeId);
				
		return getServerData("/measurements/types/{typeId}", params);
	}
	
	default public JsonNode getTypeMeasurementOfUser(String userId, String typeId) throws UnirestException {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		params.put("userId", userId);
		params.put("typeId", typeId);
				
		return getServerData("/measurements/types/{typeId}/users/{userId}", params);
	}
}
