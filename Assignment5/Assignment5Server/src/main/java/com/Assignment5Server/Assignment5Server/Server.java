/*
 * Frank Ziegler, Calen Cuesta -- Assignment 5
 */
package com.Assignment5Server.Assignment5Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

@RestController
@SpringBootApplication
public class Server {

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}

	/* part 2 endpoint 1 */
	@RequestMapping("/part2/arraylist")
	public String serializeArrayList() {
		ArrayList<Integer> al = new ArrayList<Integer>(Arrays.asList(1,1,2,3,5,8,13,21));
		ObjectMapper om = new ObjectMapper();
		String json = "";
		try { 
			json = om.writeValueAsString(al); 
		} catch (JsonProcessingException e) { 
			System.out.println(e); 
		}
		return json;
	}

	/* part 2 endpoint 2 */
	@RequestMapping("/part2/treeset")
	public String serializeTreeSet() {
		TreeSet<Character> ts = new TreeSet<Character>();
		ts.add('d'); ts.add('i'); ts.add('s'); ts.add('t'); ts.add('r'); ts.add('i');
		ts.add('b'); ts.add('u'); ts.add('t'); ts.add('e'); ts.add('d'); ts.add('s');
		ts.add('y'); ts.add('s'); ts.add('t'); ts.add('e'); ts.add('m'); ts.add('s');
		ObjectMapper om = new ObjectMapper();
		String json = "";
		try {
			json = om.writeValueAsString(ts);
		} catch (JsonProcessingException e) {
			System.out.println(e);
		}
		return json;
	}

	/* part 2 endpoint 3 */
	@RequestMapping("/part2/prioqueue")
	public String serializePriorityQueue() {
		PriorityQueue<String> pq = new PriorityQueue();
		pq.add("haha"); pq.add("hehe"); pq.add("hoho"); pq.add("hihi"); pq.add("huhu");
		pq.add("yaya"); pq.add("yeye"); pq.add("yoyo"); pq.add("yiyi"); pq.add("yuyu");
		pq.add("dada"); pq.add("dede"); pq.add("dodo"); pq.add("didi"); pq.add("dudu");
		ObjectMapper om = new ObjectMapper();
		String json = "";
		try {
			json = om.writeValueAsString(pq);
		} catch (JsonProcessingException e) {
			System.out.println(e);
		}
		return json;
	}

	/* part 3 */
	@PostMapping("/part3/treeset")
	public String postTreeSet(@RequestBody String msg) {
		ObjectMapper om = new ObjectMapper();
		om.findAndRegisterModules();
		TreeSet<Character> ts = null;
		try {
			ts = om.readValue(msg, TreeSet.class);
		} catch (JsonProcessingException e) {
			System.out.println(e);
		}
		return ts.toString();
	} 
}
