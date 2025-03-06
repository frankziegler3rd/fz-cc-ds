import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Server {
	
	public static List<BigInteger> factorize(BigInteger n) {
		System.out.println(n);
		BigInteger sqrtOfN = n.sqrt();
		List<BigInteger> factors = new ArrayList<BigInteger>();
		for (BigInteger i = BigInteger.TWO; i.compareTo(sqrtOfN) <= 0; i = i.add(BigInteger.ONE)) {
			if (n.mod(i).equals(BigInteger.ZERO)) {
				factors.add(i);
				factors.add(n.divide(i));
			}
		}
		Collections.sort(factors);
		return factors;
	}

	public static void main(String[] args) {
		while (true) {
			ServerSocket ss = null;
			Socket s = null;
		
			try {
				System.out.println("Connection successful.");
				ss = new ServerSocket(10000);
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				BigInteger n = new BigInteger(dis.readUTF());
				System.out.println("Read " + n);
				List<BigInteger> factors = factorize(n);
				StringBuilder sb = new StringBuilder();
				for (BigInteger num : factors) {
					sb.append(" " + num);
				}
				dos.writeUTF(sb.toString());
			}
			catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}
