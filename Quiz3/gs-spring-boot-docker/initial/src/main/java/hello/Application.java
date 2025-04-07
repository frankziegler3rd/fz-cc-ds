package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.lang.Math;
import java.util.ArrayList;

@SpringBootApplication
@RestController
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public static boolean isPrime(int n) {
		int nsqrt = (int)Math.sqrt(n);
		for (int i = 2; i<=nsqrt; i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}


	public static ArrayList<Integer> getPrimes(int n) {
		ArrayList<Integer> primes = new ArrayList<Integer>();
		for (int i = 2; i <= n; i++) {
			if (isPrime(i)) {
				primes.add(i);
			}
		}
		return primes;
	}

	@RequestMapping("/prime/{number}")
	public String printPrimes(@PathVariable int number) {
		return getPrimes(number).toString() + "\n";
	}
}
