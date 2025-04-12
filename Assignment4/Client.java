/*
 * Frank Ziegler, Calen Cuesta -- Assignment 4
 */
import java.io.*;
import java.net.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Client {

	static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

	/*
	 * Given a string of serialized data, parse it into the expected data type (by the given 
	 * menu option we call method) and return it.
	 */
	public static Object deserializeByMethod(String serialized, String method) {
		Object deserialized = null;
		if (method.equals("collatz")) {
			String replace = serialized.replaceAll("^\\[|]$", "");
			List<String> temp = new ArrayList<String>(Arrays.asList(replace.replaceAll(", ", ",").split(",")));
			List<Integer> deserializedList = new ArrayList<Integer>();
			for (String s : temp) {
				deserializedList.add(Integer.parseInt(s));
			}
			deserialized = deserializedList;
		} else if (method.equals("map")){
			String replace = serialized.replaceAll("^\\{|}$", "").replaceAll(" ", "");
			String[] toDeserialize = replace.split(",");
			HashMap<String, String> deserializedMap = new HashMap<String, String>();
			for(String s : toDeserialize){
				String[] keyValue = s.split("=");
				deserializedMap.put(keyValue[0], keyValue[1]);
			}
			deserialized = deserializedMap;
		} else {
			String replace = serialized.replaceAll("^\\[|]$", "").replaceAll(" ", "");
			String[] toDeserialize = replace.split(",");
			Set<String> deserializedSet = new HashSet<String>();
			for (String s : toDeserialize){
				deserializedSet.add(s);
			}
			deserialized = deserializedSet;
			
		}
				
		return deserialized;
	}

	/*
	 * Establishes HTTP connection with server at localhost:8080/endpoint. 
	 *
	 * The endpoint string is built off 2 parameters passed to this method: method (String)
	 * and pathVar (Object). The idea is: the user interfacing with this client chooses some
	 * menu option, and the "method" (base of the endpoint on the server side) is based on 
	 * that menu option, i.e. "collatz" for http://localhost:8080/collatz/pathVar where 
	 * the pathVar object is inputted by the user after choosing a menu option. 
	 */
	public static String getSerializedDataFromServer(String method, Object pathVar) {
		URL url = null;
		try {
			if (pathVar != null) {
				url = new URL("http://localhost:8080/"+method+"/"+pathVar);
			} else {
				url = new URL("http://localhost:8080/"+method);
			}
		} catch (MalformedURLException e) {
			System.out.println(e);
			return "";
		}
		HttpURLConnection hurlc = null;
		try {
			hurlc = (HttpURLConnection) url.openConnection();
			hurlc.setRequestMethod("GET");
		} catch (IOException e) {
			System.out.println(e);
			return "";
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
			return "";
		}
		return sb.toString();
	}

	public static void main(String[] args) throws IOException {
		System.out.println("---- Welcome to Assignment 4 ----");
		System.out.println("MENU");
		System.out.println("1 - Input a number n to get the corresponding Collatz sequence");
		System.out.println("2 - Return a deserialized HashMap.");
		System.out.println("3 - Return a deserialized HashSet.");
		System.out.println();

		boolean quit = false;
		int menu = -1;

		while (!quit) {
			System.out.print("Type a menu option or 0 to quit: ");
			menu = Integer.parseInt(stdin.readLine().trim());
			System.out.println();
			String method = null;
			Object pathVar = null;
			String serialized = null;
			Object deserialized = null;
			switch (menu) {
				case 0: quit = true;
					System.out.print("Goodbye.");
					break;
				case 1: System.out.print("Input an integer: ");
					method = "collatz";
					pathVar = Integer.parseInt(stdin.readLine().trim());
					break;
				case 2:
					method = "map";
					pathVar = null;
					break;
				case 3:
					method = "set";
					pathVar = null;
					break;
			}
			if (menu > 0 && menu < 4) {
				serialized = getSerializedDataFromServer(method, pathVar);
				System.out.println(serialized);
				deserialized = deserializeByMethod(serialized, method);
				System.out.println(deserialized);
			}
			System.out.println();
		}	
	}
}
