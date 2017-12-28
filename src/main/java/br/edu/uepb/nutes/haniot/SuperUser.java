package br.edu.uepb.nutes.haniot;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SuperUser extends User implements RequestUser, RequestMeasurement, RequestDevice{
	
	private String token;
	
	public SuperUser(String name, int gender, long dateOfBirth, double height, String email, String password,
			int group) throws UnirestException {
		super(name, gender, dateOfBirth, height, email, password, group);
		// TODO Auto-generated constructor stub
		
		superUserRegistration();
		this.token = superUserAuthentication();
	}
	
	public SuperUser() {
		// TODO Auto-generated constructor stub
	}
	
	public String getToken() {
		return token;
	}

	private HttpResponse<String> superUserRegistration() throws UnirestException {
		// TODO Auto-generated method stub
		Gson gson = new Gson();

		String rootUserJson = gson.toJson(this);

		return sendServerData("/users/signup", rootUserJson).asString();
	}
	
	private String superUserAuthentication() throws JSONException, UnirestException {
		// TODO Auto-generated method stub
		JSONObject body= new JSONObject();
		
		body.put("email", getEmail());
		body.put("password",getPassword());
		
		return sendServerData("/users/authenticate", body).asJson().getBody().getObject().getString("token");
	}

}