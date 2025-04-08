/*
 * Frank Ziegler, Calen Cuesta -- Assignment 4
 */
import java.io.*;
import java.net.*;

public class Client {

	static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

	/*
	 * Establishes HTTP connection with server at localhost:8080/endpoint. 
	 *
	 * The endpoint string is built off 2 parameters passed to this method: method (String)
	 * and pathVar (Object). The idea is: the user interfacing with this client chooses some
	 * menu option, and the "method" (base of the endpoint on the server side) is based on 
	 * that menu option, i.e. "collatz" for http://localhost:8080/collatz/pathVar where 
	 * the pathVar object is inputted by the user after choosing a menu option. 
	 */
	public static void getDataStructuresFromServer(String method, Object pathVar) {
		URL url = null;
		try {
			url = new URL("http://localhost:8080/"+method+"/"+pathVar);
		} catch (MalformedURLException e) {
			System.out.println(e);
			return;
		}
		HttpURLConnection hurlc = null;
		try {
			hurlc = (HttpURLConnection) url.openConnection();
			hurlc.setRequestMethod("GET");
		} catch (IOException e) {
			System.out.println(e);
			return;
		}
		StringBuilder sb = new StringBuilder();
		String inputLine;
		try {
			BufferedReader tbr = new BufferedReader(new InputStreamReader(hurlc.getInputStream()));
			while ((inputLine = tbr.readLine()) != null) {
				sb.append(inputLine);
			}
		} catch (IOException e) {
			System.out.println(e);
			return;
		}
		System.out.println(sb.toString());
	}

	public static void main(String[] args) throws IOException {
		System.out.println("---- Welcome to Assignment 4 ----");
		System.out.println("MENU");
		System.out.println("1 - Input a number n to get the corresponding Collatz sequence");
		System.out.println("2 - Null");
		System.out.println("3 - Null");
		System.out.println();

		boolean quit = false;
		int menu = -1;

		while (!quit) {
			System.out.print("Type a menu option or 0 to quit: ");
			menu = Integer.parseInt(stdin.readLine().trim());
			System.out.println();
			String method = null;
			Object pathVar = null;
			switch (menu) {
				case 0: quit = true;
					System.out.print("Goodbye");
					break;
				case 1: System.out.print("Input an integer: ");
					method = "collatz";
					pathVar = Integer.parseInt(stdin.readLine().trim());
					getDataStructuresFromServer(method, pathVar);
					break;
				case 2:
					break;
				case 3:
					break;
			}
			System.out.println();
		}	
	}
}
