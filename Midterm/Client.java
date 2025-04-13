import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.Random;

public class Client {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        
        Socket s = null;
        boolean quit = false;
        int menu = -1;

        System.out.println("-{ &$~!@ WELCOME TO MIDTERM @!~$& }-");
        System.out.println("___________________MENU___________________");
        System.out.println("1. Average an array of BigIntegers");
        System.out.println("2. Average the averages of arrays of a 2D BigInteger array");
        System.out.println();

        try {
            while (!quit) {
                System.out.print("Enter a menu option or 0 to quit: ");
                menu = Integer.parseInt(br.readLine().trim());
                System.out.println();

                switch (menu) {
                    case 1:
                        part1();
                        break;
                    case 2:
                        part2();
                        break;
                    case 0:
                        quit = true;
                        System.out.print("Goodbye.");
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void part1() {
        BigInteger[] biarr = { new BigInteger("123456"), new BigInteger("789101"), new BigInteger("2500"), new BigInteger("333333"), new BigInteger("888888") };
        try {
            Socket s = new Socket("127.0.0.1", 10000);
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            oos.writeObject(biarr);
            BigInteger average = (BigInteger) ois.readObject();
            System.out.println("Average of " + Arrays.toString(biarr) + " is " + average + ".");
        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e);
        }
    }

    public static BigInteger[] generateA1DBigIntegerArray() {}

    public static void part2() throws IOException {

        BigInteger[][] biarr = null;

        System.out.print("Enter 1 to randomly generate a BigInt array or 2 to input it yourself: ");
        int choice = Integer.parseInt(br.readLine().trim());
        System.out.println();
        
        if (choice == 1) {
            biarr = generateA2DBigIntegerArray();
        } else if (choice == 2) {
            System.out.print("Number of rows (up to 100): ");
            System.out.println();
            int rows = Integer.parseInt(br.readLine().trim());
            System.out.print("Number of cols (up to 100): ");
            System.out.println();
            int cols = Integer.parseInt(br.readLine().trim());
            biarr = new BigInteger[rows][cols];
            for (int i = 0; i < biarr.length; i++) {
                for (int j = 0; j < biarr[0].length; j++) {
                    System.out.print("Row " + i + ", column " + j + ": ");
                    BigInteger val = new BigInteger(br.readLine().trim());
                    biarr[i][j] = val;
                }
            }
        }

        AtomicReference<BigInteger> sumAverages = new AtomicReference<BigInteger>(BigInteger.ZERO);
        int nthreads = biarr.length;
        ExecutorService es = Executors.newFixedThreadPool(nthreads);
        
        for (int i = 0; i < biarr.length; i++) {
            final BigInteger[] row = biarr[i];    
            Runnable task = new Runnable() {
                public void run() {
                    try {
                        Socket s = new Socket("127.0.0.1", 10000);
                        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());            
                        oos.writeObject(row);
                        BigInteger avg = (BigInteger) ois.readObject();
                        sumAverages.getAndUpdate(prev -> prev.add(avg));
                    } catch (ClassNotFoundException | IOException e) {
                        System.out.println(e);
                    }
                    
                }
            };
            es.execute(task);
        }
        es.shutdown();
        try {
            while (!es.awaitTermination(1, TimeUnit.MINUTES)); // Wait for threads to finish
        } catch (InterruptedException e) { System.out.println(e); }
        System.out.println("The average of the averages is " + sumAverages.get().divide(new BigInteger(""+biarr.length)));
    }

    public static BigInteger[][] generateA2DBigIntegerArray() 
    {
        Random rand = new Random();
        int minRows = 2, minCols = 2;
        int rows = minRows + rand.nextInt((100-minRows)+1);
        int cols = minCols + rand.nextInt((100-minCols)+1);
        BigInteger[][] biarr = new BigInteger[rows][cols];
        for (int i = 0; i < biarr.length; i++) {
            for (int j = 0; j < biarr[0].length; j++) {
                biarr[i][j] = new BigInteger(16 + rand.nextInt(49), rand);
            }
        }
        return biarr;
    }

    public static void part3() {}
    public static void part4() {}
    public static void part5() {}

}