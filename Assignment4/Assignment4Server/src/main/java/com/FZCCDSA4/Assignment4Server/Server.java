package com.FZCCDSA4.Assignment4Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

@RestController
@SpringBootApplication
public class Server {

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}

	/*
	 * The Collatz Sequence can be modeled after the following recursive formula:
	 *
	 * 		f(n) = n/2 if n is even, 3n+1 if n is odd
	 *
	 * The Collatz Conjecture states that given an integer n, the sequence will collapse to 1.
	 * i.e. n = 6: [6, 3, 10, 5, 16, 8, 4, 2, 1]
	 */	
	public static List<Integer> collatz(int n) {
		List<Integer> seq = new ArrayList<Integer>();
		seq.add(n);
		while (n != 1) {
			if (n % 2 == 0) {
				n /= 2;
			} else {
				n = 3 * n + 1;
			}
			seq.add(n);
		}
		return seq;
	}
	
	@RequestMapping("/collatz/{n}")
	public String sendCollatzSequence(@PathVariable int n) {
		List<Integer> collatzSeq = collatz(n);
		return collatzSeq.toString();
	}
	
	
	@RequestMapping("/map")
	public String sendHashMap(){
		HashMap <String, String> map = new HashMap<String, String>();
		map.put("hello", "world");
		map.put("merry", "christmas");
		map.put("happy", "newyears");
		return map.toString();	
	}
	
	@RequestMapping("/set")
	public String sendSet(){
		Set<String> set = new HashSet<String>();
		set.add("Hello");
		set.add("my");
		set.add("king");
		return set.toString();
	}
}
