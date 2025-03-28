import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {

	static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

	public static String stringifyData(Object[] data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			sb.append(data[i]);
		}
		return sb.toString();
	}


	public static void main(String[] args) {
		
		Socket s = null;
		
		try {
			s = new Socket("127.0.0.1", 10000);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
	       		ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			System.out.println("ASSIGNMENT 3 MENU:");
			System.out.println("1 - Get an array of integers");
			System.out.println("2 - Get an ArrayList of Strings");
			System.out.println("3 - Enter a number to factor"); 
			boolean quit = false;

			while (!quit) {
				System.out.print("Enter a menu option or 0 to quit: ");
				int menu = Integer.parseInt(stdin.readLine().trim());
				System.out.println();
				if (menu == 0) {
					quit = true;
					System.out.println("Goodbye.");
				}
				Object o = null;
				dos.writeInt(menu);
				switch(menu) {
					case 1:
                                		o = ois.readObject();
						if (o instanceof int[]) {
							int[] intArr = (int[]) o;
							System.out.println("The elements of the array you received are " + Arrays.toString(intArr));
						}
						break;
					case 2:
                                		o = ois.readObject();
						if (o instanceof ArrayList) {
							ArrayList<String> strAL = (ArrayList<String>) o;	
							System.out.println("The elements of the ArrayList you received are " + Arrays.toString(strAL.toArray()));
						}
					       	break;	
					case 3:
						System.out.print("Enter a number to factor: ");
						String numToFact = stdin.readLine().trim();
						System.out.println();
						dos.writeUTF(numToFact);
						o = ois.readObject();
						if (o instanceof BigInteger[]) {
							BigInteger[] bigIntArr = (BigInteger[]) o;
							System.out.println("The elem of the array you received are " + Arrays.toString(bigIntArr));
						}
						break;
				}
				System.out.print(".");
				System.out.println();
			}
		}
		catch (IOException e) {
			System.out.println(e);
		}
		catch (ClassNotFoundException e) {
			System.out.println(e);
		}	
	}
}
