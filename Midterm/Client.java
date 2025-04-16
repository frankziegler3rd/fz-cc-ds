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
import java.util.List;
import java.util.ArrayList;

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
                        part1(generateA1DBigIntegerArray());
                        break;
                    case 2:
                        part2(generateA2DBigIntegerArray());
                        break;
                    case 3:
                        part3(generateA2DBigIntegerArray());
                        break;
                    case 4:
                        part4(generateA2DBigIntegerArray());
                        break;
                    case 5:
                        part5(generateA2DBigIntegerArray());
                    case 0:
                        quit = true;
                        System.out.print("Goodbye.");
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void part1(BigInteger[] biarr) {
        
        try {
            Socket s = new Socket("127.0.0.1", 10000);
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            oos.writeInt(1); oos.flush();
            oos.writeObject(biarr); oos.flush();
            BigInteger average = (BigInteger) ois.readObject();
            System.out.println("Average of " + Arrays.toString(biarr) + " is " + average + ".");
        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e);
        }
    }

    public static BigInteger[] generateA1DBigIntegerArray() throws IOException
    {
        System.out.print("Enter 1 to randomly generate a BigInt array or 2 to input it yourself: ");
        int choice = Integer.parseInt(br.readLine().trim());
        System.out.println();
        
        BigInteger[] biarr = null;

        if (choice == 1) // generate it randomly
        {
            Random rand = new Random(System.currentTimeMillis());
            int minLen = 2;
            int len = minLen + rand.nextInt((100-minLen)+1);
            biarr = new BigInteger[len];
            for (int i = 0; i < biarr.length; i++) 
            {
                biarr[i] = new BigInteger(16 + rand.nextInt(49), rand);
            }
        } 
        else if (choice == 2) // input it yourself
        {
            System.out.print("Length of array (up to 100): ");
            int len = Integer.parseInt(br.readLine().trim());
            System.out.println();

            biarr = new BigInteger[len];
            for (int i = 0; i < biarr.length; i++) {
                System.out.print("Index " + i + ": ");
                BigInteger val = new BigInteger(br.readLine().trim());
                biarr[i] = val;
            }
        }
        return biarr;
    }

    public static void part2(BigInteger[][] biarr) throws IOException {

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
                        oos.writeInt(2); oos.flush();
                        oos.writeObject(row); oos.flush();
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

    public static BigInteger[][] generateA2DBigIntegerArray() throws IOException
    {
        System.out.print("Enter 1 to randomly generate a BigInt array or 2 to input it yourself: ");
        int choice = Integer.parseInt(br.readLine().trim());
        System.out.println();
        
        BigInteger[][] biarr = null;

        if (choice == 1) // generate it randomly
        {
            Random rand = new Random(System.currentTimeMillis());
            int minRows = 2, minCols = 2;
            int rows = minRows + rand.nextInt((100-minRows)+1);
            int cols = minCols + rand.nextInt((100-minCols)+1);
            biarr = new BigInteger[rows][cols];
            for (int i = 0; i < biarr.length; i++) {
                for (int j = 0; j < biarr[0].length; j++) {
                    biarr[i][j] = new BigInteger(16 + rand.nextInt(49), rand);
                }
            }
        } 
        else if (choice == 2) // input it yourself
        {
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
        return biarr;
    }

    public static void part3(BigInteger[][] biarr) {

        AtomicReference<BigInteger> totalSum = new AtomicReference<BigInteger>(BigInteger.ZERO);
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
                        oos.writeInt(3);
                        oos.flush();            
                        oos.writeObject(row);
                        oos.flush();
                        BigInteger sum = (BigInteger) ois.readObject();
                        totalSum.getAndUpdate(prev -> prev.add(sum));
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
        System.out.println("The average of the total data set is " + totalSum.get().divide(new BigInteger(Integer.toString(biarr.length * biarr[0].length)) ) );
    }

    public static BigInteger part4(BigInteger[][] biarr) 
    {
        List<BigInteger> biggest = new ArrayList<BigInteger>();
        ExecutorService es = Executors.newFixedThreadPool(biarr.length);
        for (int i = 0; i < biarr.length; i++)
        {
            final BigInteger[] row = biarr[i];
            Runnable task = new Runnable() 
            {
                public void run() {
                    try {
                        Socket s = new Socket("127.0.0.1", 10000);
                        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());            
                        oos.writeInt(4); oos.flush();
                        oos.writeObject(row); oos.flush();
                        BigInteger biggestGuyInTheRow = (BigInteger) ois.readObject();
                        biggest.add(biggestGuyInTheRow);
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
        // find the biggest of the biggest
        BigInteger maxBiggest = BigInteger.ZERO;
        for (BigInteger bigGuy : biggest) 
        {
            maxBiggest = bigGuy.max(maxBiggest); // max(bigGuy, maxBiggest)
        }
        return maxBiggest;
    }
    
    public static BigInteger[][] part5(BigInteger[][] ubiarr)
    {
        BigInteger[][] sbiarr = new BigInteger[ubiarr.length][ubiarr[0].length];
        ExecutorService es = Executors.newFixedThreadPool(ubiarr.length);
        for (int i = 0; i < ubiarr.length; i++) 
        {
            final BigInteger[] urow = ubiarr[i];
            final int idx = i;
            Runnable task = new Runnable() 
            {
                public void run() 
                {
                    try {
                        Socket s = new Socket("127.0.0.1", 10000); 
                        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                        oos.writeInt(5); oos.flush();
                        oos.writeObject(urow); oos.flush();
                        BigInteger[] srow = (BigInteger[]) ois.readObject();
                        sbiarr[idx] = srow;
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
        
        return sbiarr;
    }
}