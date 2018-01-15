package br.edu.uepb.nutes.haniot;

import java.net.URISyntaxException;

import com.mashape.unirest.http.exceptions.UnirestException;

import br.edu.uepb.nutes.apirest.SuperUser;
import br.edu.uepb.nutes.jms.JmsMessageListener;

public class Main {
	public static void main(String[] args) throws URISyntaxException, Exception {
		
		// Requisitando informações ao servidor		
		SuperUser rootUser = new SuperUser("Josefa", 1, 46244230000L, 177, "lucas@gmail.com","1234",1);
		
		System.out.println(rootUser.getAllDevices());
		
		// Escutando alarmes emitidos pelos dispositivos simulados no OpenICE
		JmsMessageListener.initializingAlarmListening();
				
	}
}
