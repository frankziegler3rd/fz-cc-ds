import java.io.*;
import java.net.*;
import java.math.BigInteger;

public class Client {

	static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws IOException {
		Solver solver = new DistributedSolver();
		boolean quit = false;	
		System.out.println("Welcome to the distributed factorizer.");
		while (!quit) {
			System.out.print("Enter a number to factorize or type 2 to quit: ");
			String n = stdin.readLine().trim();
			if (n.equals("2")) {
				quit = true;
			} else {
				System.out.println(solver.solve(n));
			}
		}
	}
}
