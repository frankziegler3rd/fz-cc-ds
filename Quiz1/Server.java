import java.io.*;
import java.net.*;

public class Server {
	

	public static void main(String[] args) {

		ServerSocket ss = null;
		Socket s = null;
		
		try {
			System.out.println("Connection successful.");
			ss = new ServerSocket(10000);
			s = ss.accept();
			DataInputStream dis = new DataInputStream(s.getInputStream());
			System.out.println(dis.readUTF());

		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
}
