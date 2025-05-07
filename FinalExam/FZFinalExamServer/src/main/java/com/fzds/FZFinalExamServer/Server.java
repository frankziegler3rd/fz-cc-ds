/*
 * Frank Ziegler - DS Final Exam
 */ 

package com.fzds.FZFinalExamServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.*;
import java.time.Duration;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Date;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

@RestController
@SpringBootApplication
public class Server {

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}

	/* ----------------------------------------------------------------------------------------------------- * 
	 *                                               Problem 6                                               *
	 * ----------------------------------------------------------------------------------------------------- * 
	 */

	@GetMapping("/deadlock")
	public boolean detectDeadlock() {
		List<Machine> machines = getMachines();
		int[][] waitForGraph = constructMachineWaitForGraph(machines);
		for (int vertex = 0; vertex < waitForGraph.length; vertex++) {
			boolean[] visited = new boolean[waitForGraph.length];
			if (DFS(waitForGraph, visited, vertex)) { return true; }
		}
		return false;
	}

	/*
	 * recursive helper function to perform DFS on the waitForGraph
	 */
	public boolean DFS(int[][] waitForGraph, boolean[] visited, int vertex) {
		if (visited[vertex]) { return true; }
		visited[vertex] = true;
		for (int neighbor = 0; neighbor < waitForGraph.length; neighbor++) {
			if (waitForGraph[vertex][neighbor] == 1) {
				if (DFS(waitForGraph, visited, neighbor)) { return true; }
			}
		}
		return false;
	}

	/*
	 * helper function for Problem 6
	 * constructs an adjacency matrix of machines and resources
	 */
	public int[][] constructMachineWaitForGraph(List<Machine> machines) {	
		HashSet<String> machineAndResourceIDs = new HashSet<String>(); // NODES/VERTICES
		// construct an adjacency matrix 
		// consider n x n matrix where n = #OfMachines + #OfResources
		// let m be a machine and r be a resource: 
		// 		if m has r then matrix[m][r] = 0, matrix[r][m] = 1
		// 		if m waits on r, then matrix[r][m] = 1, matrix[m][r] = 0
		for (Machine machine : machines) {
			machineAndResourceIDs.add(machine.getMachineID());
			List<String> machResources = machine.getResourcesHeld();
			if (machResources != null) {	
				for (String resource : machResources) {
					machineAndResourceIDs.add(resource);
				}
			}
			String waitingOn = machine.getWaitingOn();
			if (!waitingOn.equals("")) {
				machineAndResourceIDs.add(waitingOn);
			}
		}
		
		int n = machineAndResourceIDs.size();
		int[][] adjacencyMatrix = new int[n][n]; // EDGES!!!!
		String[] machinesAndResources = machineAndResourceIDs.toArray(new String[n]);

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				Machine machine = getMachineByID(machinesAndResources[i], machines);
				if(machine != null) {
					if (machine.getResourcesHeld() != null) {
						if (machine.getResourcesHeld().contains(machinesAndResources[j])) {
							adjacencyMatrix[j][i] = 1;
						}
					}
					if(machine.getWaitingOn().equals(machinesAndResources[j])) {
						adjacencyMatrix[i][j] = 1;
					}
				}
			}
		}
		return adjacencyMatrix;
	}
	
	/*
	 * helper function for Problem 6
	 * this serves the purpose of verifying if a given ID does represent a machine. 
	 * returns the machine in machines matching the given machineID
	 */ 
	public Machine getMachineByID(String id, List<Machine> machines) {
		for (Machine machine : machines) {
			if (id.equals(machine.getMachineID())) {
				return machine;
			}
		}
		return null; // no machine has this id
	}

	/*
	 * helper function for Problem 6
	 * retrieves the machines on the elvis server
	 * note that the machine links are hardcoded and this can be scaled to scrape the server. not sure if that is necessary
	 */
	public List<Machine> getMachines() {
		ArrayList<String> machineLinks = new ArrayList(Arrays.asList( "5RSqq1AA2l.json", "8kh6MOH5Ea.json", "qKgug1Afsb.json", "vTnGy8QQAS.json", "xN3b82lmTM.json" ));
		List<Machine> machines = new ArrayList<Machine>();
		HttpClient client = HttpClient.newBuilder()
								.build();
		for (String machine : machineLinks) {
			HttpRequest req = HttpRequest.newBuilder()
								.uri(URI.create("http://elvis.rowan.edu/~mckeep82/ds/machine_states/"+machine))
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
				machines.add(m);
			} catch (JsonProcessingException e) {
				System.out.println(e);
			}
		}
		return machines;
	}

	/* ----------------------------------------------------------------------------------------------------- * 
	 *                                               Problem 7                                               *
	 * ----------------------------------------------------------------------------------------------------- * 
	 */
	Queue<String> inbox = new ArrayDeque<>();

	@PostMapping("/send")
	public void addMessageToQ(@RequestBody String message) {
		inbox.add(message);
	}

	@GetMapping("/receive")
	public String getAndPopMessageFromQ() {
		String message = inbox.peek() == null ? "this inbox is empty :'( but thank you for checking! :)" : inbox.poll();
		return message+"\n";
	}

	/* ----------------------------------------------------------------------------------------------------- * 
	 *                                               Problem 8                                               *
	 * ----------------------------------------------------------------------------------------------------- * 
	 */
	@GetMapping("/getTime")
	public String getTime() {
		NTPUDPClient ntpudpc = new NTPUDPClient();
		InetAddress inet = null;
		try {
			inet = InetAddress.getByName("time-a.nist.gov");
		} catch (UnknownHostException e) { 
			System.out.println(e); 
		}
		TimeInfo ti = null;
		Date now = null;
		try {
			ti = ntpudpc.getTime(inet);
			now = new Date(ti.getReturnTime());
		} catch (IOException e) {
			System.out.println(e);
		}
		return now.toString();
	}
}
