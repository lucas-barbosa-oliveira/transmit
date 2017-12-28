package br.edu.uepb.nutes.haniot;

import java.util.Iterator;

import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;

public interface Token {

	String hostIp = "http://localhost:3000/api/v1";

	public String getToken();

	default JsonNode getServerData(String url, JSONObject params) throws UnirestException {

		GetRequest response = Unirest.get(hostIp + url)
				.header("Authorization", "JWT " + getToken());

		for (Iterator<String> iterator = params.keys(); iterator.hasNext();) {
			String key = (String) iterator.next();
			response.routeParam(key, params.get(key).toString());
		}

		return response.asJson().getBody();
	}

	default JsonNode getServerData(String url) throws UnirestException {

		return Unirest.get(hostIp + url)
				.header("Authorization", "JWT " + getToken())
				.asJson()
				.getBody();
	}

	default RequestBodyEntity sendServerData(String url, Object body) {

		return Unirest.post(hostIp + url)
				.header("Content-Type", "application/json")
				.body(body.toString());
	}

}