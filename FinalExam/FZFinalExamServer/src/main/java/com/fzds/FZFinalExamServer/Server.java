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
	 *
	 * whew this was a doozy. i feel the need to in-depth explain my thinking behind this one, cuz to be totally honest i'm not sure it would work for all test
	 * cases but it does work for this one. 
	 *
	 * the overall idea here is to create a digraph of machines and resources. to represent this digraph i chose an adjacency matrix.
	 * best way to explain this is through an example: say we have 2 machines, m1, m2, and 2 resources, r1, r2. then, suppose m1 has r1
	 * and waits on r2, and suppose m2 has r2 and waits on r1. our matrix would then be:
	 *
	 * 			m1	m2	r1	r2
	 *		m1	0	0	1	0	
	 *      m2	0	0	0	1
	 *		r1	0	1	0	0
	 *		r2	1	0	0	0
	 *
	 * in an undirected graph, the split of the matrix down the diagonal would net two identical triangular matrices, but since wait-for
	 * graphs are directed, it's important to denote what points to what. i.e. machine-has-resource means resource points to machine and
	 * machine-waitsOn-resource means machine points to resource. 
	 *
	 * one important thing to note is that machines will never have edges to other machines and the same is true for resources.
	 * another important thing to note is that we map all machines and resources to integers 0, 1, 2, ..., numMachines+numResources. which int
	 * they are mapped to is unclear because the resources are grabbed through iteration of the machines and stored in a set (to ensure they're 
	 * unique). however, it doesn't actually matter what integer (index in the adjacency matrix) they map to because we're just checking for a
	 * cycle in the most general sense. 
	 *
	 * on the DFS algorithm: this is a trick i picked up through DSA/DAA and leetcoding. in the case of this particular example, there are 10
	 * "nodes" or "vertices" -- 5 machines, 5 resources. we check each node, create a boolean visited array for each one, add it to the visited
	 * array and check all of the other nodes in the graph. if there is an edge between node and neighbor, i.e. waitForGraph[vertex][neighbor] == 1,
	 * we recurse on that neighbor. IF WE HAVE SEEN THAT NEIGHBOR BEFORE, we know we are in a cycle. 
	 *
	 * this problem covers like 65-75% of overall code in this class. it was tough to work through, but i have been obsessed with actually implementing
	 * the solution to it since we covered it in OS. thank you for giving me the chance to try my hand!
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
				if (DFS(waitForGraph, visited, neighbor)) { return true; } // first thing it does is check if neighbor has been visited. if so, CYCLE!
			}
		}
		return false;
	}

	/*
	 * helper function for Problem 6
	 * constructs an adjacency matrix of machines and resources
	 * consider n x n matrix where n = #OfMachines + #OfResources
	 * let m be a machine and r be a resource: 
	 *		if m has r then matrix[m][r] = 0, matrix[r][m] = 1
	 * 		if m waits on r, then matrix[r][m] = 1, matrix[m][r] = 0
	 * thank you OS!
	 */
	public int[][] constructMachineWaitForGraph(List<Machine> machines) {	
		HashSet<String> machinesAndResourcesSet = new HashSet<String>(); // UNIQUE NODES/VERTICES

		// NOTE ABOUT THIS: machines are unique, but resources are not, per machine. that is why i use Set
		for (Machine machine : machines) {
			machinesAndResourcesSet.add(machine.getMachineID());
			List<String> machineResources = machine.getResourcesHeld();
			if (machineResources != null) {	
				for (String resource : machineResources) {
					machinesAndResourcesSet.add(resource);
				}
			}
			String waitingOn = machine.getWaitingOn();
			if (!waitingOn.equals("")) {
				machinesAndResourcesSet.add(waitingOn);
			}
		}
		
		int n = machinesAndResourcesSet.size();
		int[][] adjacencyMatrix = new int[n][n]; // EDGES!!!!
		String[] machinesAndResourcesArr = machinesAndResourcesSet.toArray(new String[n]); // why do this? well for direct index accessing, of course

		// most of the above is just constructing a combined list of machines and resources. below is the actual logic for constructing the edge list.
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				Machine machine = getMachineByID(machinesAndResourcesArr[i], machines);
				if(machine != null) {
					if (machine.getResourcesHeld() != null) {
						if (machine.getResourcesHeld().contains(machinesAndResourcesArr[j])) {
							adjacencyMatrix[j][i] = 1;
						}
					}
					if(machine.getWaitingOn().equals(machinesAndResourcesArr[j])) {
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
	 * retrieves the machines on the elvis server and stores them in a list
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
	 *
	 * pretty self explanatory if you ask me
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
	 *
	 * also pretty self explanatory. your instructions were 1080p clear
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
