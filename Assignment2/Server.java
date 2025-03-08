/*
 * Frank Ziegler, Calen Cuesta -- Assignment 2
 */
import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.util.Set;
import java.util.TreeSet;
import java.util.Collections;

/*
 * Server side code -- handles factorization logic and communicating back with the distributed intermediary. 
 */
public class Server {
	
	/*
	 * Stores factors of a given BigInt in a BigInt set and returns it.
	 * Basic number theory states: all numbers n (prime and composite) have factors 1 and n. If n is prime,
	 * those are n's only factors, however, if n is composite, n has prime factors not exceeding the square
	 * root of n. Thus we only need to add factors i = 2, 3, 5, 7, 11, ..., sqrt{n} and n / i to the array,
	 * as well as 1 and n itself as a default. 
	 *
	 * Non-prime i as a loop-control variable may introduce duplicates thus we use a Set.
	 *
	 * Also note, BigInteger.sqrt() was introduced in Java 9, so this won't compile if you have < Java 9. 
	 */
	public static Set<BigInteger> factorize(BigInteger n) {
		System.out.println(n);
		BigInteger sqrtOfN = n.sqrt();
		Set<BigInteger> factors = new TreeSet<BigInteger>(); // Sets, but TreeSets maintain sorted order
		factors.add(BigInteger.ONE);
		factors.add(n);
		for (BigInteger i = BigInteger.TWO; i.compareTo(sqrtOfN) <= 0; i = i.add(BigInteger.ONE)) {
			if (n.mod(i).equals(BigInteger.ZERO)) { // if n is divisible by i
				factors.add(i);
				factors.add(n.divide(i));
			}
		}
		return factors;
	}

	/*
	 * Retrieves client connection through input stream and performs factorization upon request, returns string
	 * representation of the factors set to the distributed intermediary object to send back to client.  
	 */
	public static void main(String[] args) {
		while (true) { // To continue receiving connections and not closing after the first one. 
			ServerSocket ss = null;
			Socket s = null;
		
			try {
				System.out.println("Connection successful.");
				ss = new ServerSocket(10000);
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				BigInteger n = new BigInteger(dis.readUTF());
				Set<BigInteger> factors = factorize(n);
				StringBuilder sb = new StringBuilder();
				for (BigInteger factor : factors) {
					sb.append(factor + ", ");
				}
				if (sb.length() > 0) {
					sb.setLength(sb.length() - 2); // chop off the last ", "
				}
				dos.writeUTF(sb.toString());
			}
			catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}
