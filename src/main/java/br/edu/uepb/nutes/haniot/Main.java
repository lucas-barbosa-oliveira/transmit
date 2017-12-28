package br.edu.uepb.nutes.haniot;

import com.mashape.unirest.http.exceptions.UnirestException;

public class Main {
	public static void main(String[] args) throws UnirestException {
		
		SuperUser rootUser = new SuperUser("Josefa", 1, 46244230000L, 177, "lucas@gmail.com","1234",1);
		
		System.out.println(rootUser.getAllDevices());
				
	}
}
