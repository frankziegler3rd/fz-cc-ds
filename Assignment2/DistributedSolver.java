/*
 * Frank Ziegler, Calen Cuesta -- Assignment 2
 */
import java.io.*;
import java.net.*;

/*
 * The "object" that the client communicates to the server through per part 2 of 
 * assignment instructions. Writes the client input to the server, and sends the
 * server output back to the client to print.  
 */
public class DistributedSolver implements Solver {
	
	public String solve(String n) {
		Socket s = null;
		try {
			s = new Socket("127.0.0.1", 10000);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			DataInputStream dis = new DataInputStream(s.getInputStream());
	       		dos.writeUTF(n);
			return dis.readUTF();
		}
		catch (IOException e) {
			System.out.println(e);
		}
		return "";	
	}
}
