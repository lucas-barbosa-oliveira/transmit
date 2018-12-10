package br.edu.uepb.nutes.sdn.apirest;

import java.util.Iterator;

import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

public class ServerCommunication {

	private static String hostIp = "http://127.0.0.1:8080";

	static JsonNode getServerData(String url, JSONObject params) throws UnirestException {

		GetRequest response = Unirest.get(hostIp + url);

		for (Iterator<String> iterator = params.keys(); iterator.hasNext();) {
			String key = (String) iterator.next();
			response.routeParam(key, params.get(key).toString());
		}

		return response.asJson().getBody();
	}

	static JsonNode getServerData(String url) throws UnirestException {

		return Unirest.get(hostIp + url).asJson().getBody();
	}

	static JsonNode postServerData(String url, Object body) throws UnirestException {

		return Unirest.post(hostIp + url).header("Content-Type", "application/json").body(body.toString()).asJson()
				.getBody();
	}

	static JsonNode deleteServerData(String url, Object body) throws UnirestException {

		return Unirest.delete(hostIp + url).header("Content-Type", "application/json").body(body.toString()).asJson()
				.getBody();
	}

}