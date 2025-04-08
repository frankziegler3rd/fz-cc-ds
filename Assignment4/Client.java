import java.io.*;
import java.net.*;

public class Client {

	static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

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
