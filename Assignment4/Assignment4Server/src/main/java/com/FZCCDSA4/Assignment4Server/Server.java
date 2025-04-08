package com.FZCCDSA4.Assignment4Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.ArrayList;

@RestController
@SpringBootApplication
public class Server {

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}
	
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
}
