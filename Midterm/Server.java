/*
 * Frank Ziegler, Calen Cuesta -- Midterm
 */

import java.io.*;
import java.net.*;
import java.util.concurrent.Executor;
import java.math.BigInteger;
import java.util.Arrays;

public class Server {

    // part 1
    static class ThreadPerTaskExecutor implements Executor {
        public void execute(Runnable r) {
            new Thread(r).start();
        }
    }
    public static final Executor exec = new ThreadPerTaskExecutor();

    /*
     * Handles request based on user-selected menu option which correlates to a given Midterm problem. 
     */ 
    public static void handleRequest(Socket s) throws IOException {
        try {
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            int menuSelection = ois.readInt();
            Object o = ois.readObject();
            
            if (menuSelection == 1 || menuSelection == 2) { // parts 1, 2
                BigInteger average;
                BigInteger[] clientData = (BigInteger[]) o;
                average = average(clientData);
                oos.writeObject(average);
                oos.flush();
            }
            else if (menuSelection == 3) { // part 3 
                BigInteger[] clientData = (BigInteger[]) o;
                BigInteger sum = BigInteger.ZERO;
                sum = sumArr(clientData);
                oos.writeObject(sum);
                oos.flush();
            }
            else if (menuSelection == 4) { // part 4
                BigInteger[] clientData = (BigInteger[]) o;
                BigInteger max = BigInteger.ZERO;
                for (BigInteger bi : clientData) {
                    max = bi.max(max);
                }
                oos.writeObject(max);
                oos.flush();
            }
            else { // part 5
                BigInteger[] clientData = (BigInteger[]) o;
                Arrays.sort(clientData);
                oos.writeObject(clientData);
                oos.flush();
            }

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    /*
     * Helper function to calculate the average of a BigInteger array.
     */ 
    public static BigInteger average(BigInteger[] biarr) {
        BigInteger sum = BigInteger.ZERO;
        for (BigInteger bi : biarr) {
            sum = sum.add(bi);
        }
        return sum.divide(new BigInteger(""+biarr.length));
    }

    /*
     * Helper function to calculate the sum of a BigInteger array.
     */ 
    public static BigInteger sumArr(BigInteger[] biarr){
        BigInteger sum = BigInteger.ZERO;
        for (BigInteger bi : biarr){
            sum = sum.add(bi);
        }
        return sum;
    }

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(10000);
            while (true) { // while client still wants to connect
                final Socket s = ss.accept();
                Runnable task = new Runnable() {
                    public void run() {
                        try {
                            handleRequest(s);
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                    }
                };
                exec.execute(task);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}