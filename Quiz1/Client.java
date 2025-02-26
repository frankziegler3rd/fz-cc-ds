import java.io.*;
import java.net.*;

public class Client {
	
	public static void main(String[] args) {
		Socket s = null;
		try {
			s = new Socket("127.0.0.1", 10000);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
	       		dos.writeUTF("This shit worked!");
		}
		catch (IOException e) {
			System.out.println(e);
		}	
	}
}
