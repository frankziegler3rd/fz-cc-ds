/*
 * Frank Ziegler, Calen Cuesta -- Assignment 3
 */

import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/*
 * Menu-style client side.
 */
public class Client {

	static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

	/*
	 * Establishes connection with server, 
	 * While user has not quit program: allows them to choose menu option corresponding to parts 1-3 of Assignment 3.
	 * Part 1: Receives int[] from server side and prints it.
	 * Part 2: Receives ArrayList<String> from server side and prints it.
	 * Part 3: Receives Set<BigInteger> from server side and prints it. 
	 */
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
					break;
				}
				Object o = null;
				dos.writeInt(menu); // write menu option so server knows what to do
				switch(menu) {
					case 1: // Assignment 3 Part 1
                                		o = ois.readObject();
						if (o instanceof int[]) {
							int[] intArr = (int[]) o;
							System.out.print("Your integer array: " + Arrays.toString(intArr));
						}
						break;
					case 2: // Assignment 3 Part 2
                                		o = ois.readObject();
						if (o instanceof ArrayList) {
							ArrayList<String> strAL = (ArrayList<String>) o;	
							System.out.print("Your string ArrayList: " + Arrays.toString(strAL.toArray()));
						}
					       	break;	
					case 3: // Assignment 3 Part 3
						System.out.print("Enter a number to factor: ");
						String numToFact = stdin.readLine().trim();
						System.out.println();
						dos.writeUTF(numToFact);
						o = ois.readObject();
						if (o instanceof Set) {
							Set<BigInteger> bigIntSet = (Set<BigInteger>) o;
							System.out.print("The factors of " + numToFact + " are " + Arrays.toString(bigIntSet.toArray()));
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
