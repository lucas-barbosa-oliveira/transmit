package br.edu.uepb.nutes.haniot;

import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface RequestUser extends Token{

	default public JsonNode getAllUsers() throws UnirestException {
		// TODO Auto-generated method stub
		return getServerData("/users/");
	}
	
	default public JsonNode getUserId(String userId) throws UnirestException {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		params.put("userId", userId);
				
		return getServerData("/users/{userId}", params);
	}
	
}


