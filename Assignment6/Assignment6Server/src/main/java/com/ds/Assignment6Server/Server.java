/*
 * Frank Ziegler, Calen Cuesta -- Assignment 6
 */
package com.ds.Assignment6Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import java.time.Duration;
import java.io.*;

@RestController
@SpringBootApplication
public class Server {

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}

	@GetMapping("/machine")
	public String getMachineState() {
		// connect and retrieve from elvis server
		HttpClient client = HttpClient.newBuilder()
									.build();
		HttpRequest req = HttpRequest.newBuilder()
									.uri(URI.create("http://elvis.rowan.edu/~mckeep82/ds/machine_states/xN3b82lmTM.json"))
									.timeout(Duration.ofMinutes(1))
									.build();
		HttpResponse<String> response = null;
		try {
			response = client.send(req, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
		}
		// construct machine
		ObjectMapper om = new ObjectMapper();
		om.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		Machine m = null;
		try {
			m = om.readValue(response.body(), Machine.class);
		} catch (JsonProcessingException e) {
			System.out.println(e);
		}
		return m.toString();
	}
}	
