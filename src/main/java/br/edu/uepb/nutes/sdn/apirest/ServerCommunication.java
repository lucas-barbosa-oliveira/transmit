package br.edu.uepb.nutes.sdn.apirest;

import java.util.Iterator;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;

public abstract class ServerCommunication {

	String hostIp = "http://127.0.0.1:8080";

	JsonNode getServerData(String url, JSONObject params) throws UnirestException {

		GetRequest response = Unirest.get(hostIp + url);

		for (Iterator<String> iterator = params.keys(); iterator.hasNext();) {
			String key = (String) iterator.next();
			response.routeParam(key, params.get(key).toString());
		}

		return response.asJson().getBody();
	}
	
	JsonNode getServerData(String url) throws UnirestException {

		return Unirest.get(hostIp + url)
				.asJson()
				.getBody();
	}

	JsonNode postServerData(String url, Object body) throws UnirestException {

		return Unirest.post(hostIp + url)
				.header("Content-Type", "application/json")
				.body(body.toString())
				.asJson()
				.getBody();
	}
	
	JsonNode deleteServerData(String url, Object body) throws UnirestException {

		return Unirest.delete(hostIp + url)
				.header("Content-Type", "application/json")
				.body(body.toString())
				.asJson()
				.getBody();
	}


}