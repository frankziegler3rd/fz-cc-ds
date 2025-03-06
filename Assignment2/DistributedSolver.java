import java.io.*;
import java.net.*;

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
