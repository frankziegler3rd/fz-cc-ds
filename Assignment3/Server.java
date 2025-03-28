/*
 * Frank Ziegler, Calen Cuesta -- Assignment 3
 */
import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.util.Set;
import java.util.TreeSet;
import java.util.Collections;
import java.util.ArrayList;

/*
 * Server side code. 
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
	 * Receives socket connection, writes objects relative to user chosen menu option.
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
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				int menu = dis.readInt();
				switch (menu) { // User-selected menu option
					case 1:	
						int[] intArr = {1,1,2,3,5,8,13};
						oos.writeObject(intArr);
						break;
					case 2:
						ArrayList<String> al = new ArrayList<String>();
						al.add("I");
						al.add("shall");
						al.add("not");
						al.add("tell");
						al.add("lies");
						oos.writeObject(al);
						break;
					case 3:
						BigInteger n = new BigInteger(dis.readUTF());
						Set<BigInteger> factors = factorize(n);
						oos.writeObject(factors);
						break;
				}
			}
			catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}
