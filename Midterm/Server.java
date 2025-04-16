import java.io.*;
import java.net.*;
import java.util.concurrent.Executor;
import java.math.BigInteger;
import java.util.Arrays;

public class Server {

    static class ThreadPerTaskExecutor implements Executor {
        public void execute(Runnable r) {
            new Thread(r).start();
        }
    }
    public static final Executor exec = new ThreadPerTaskExecutor();

    public static void handleRequest(Socket s) throws IOException {
        try {
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            int menuSelection = ois.readInt();
            Object o = ois.readObject();
            
            if (menuSelection == 1 || menuSelection == 2) {
                BigInteger average;
                BigInteger[] clientData = (BigInteger[]) o;
                average = average(clientData);
                oos.writeObject(average);
                oos.flush();
            }
            else if (menuSelection == 3) {
                BigInteger[] clientData = (BigInteger[]) o;
                BigInteger sum = BigInteger.ZERO;
                sum = sumArr(clientData);
                oos.writeObject(sum);
                oos.flush();
            }
            else if (menuSelection == 4) {
                BigInteger[] clientData = (BigInteger[]) o;
                BigInteger max = BigInteger.ZERO;
                for (BigInteger bi : clientData) 
                {
                    max = bi.max(max);
                }
                oos.writeObject(max);
                oos.flush();
            }
            else { // menuSelection == 5
                BigInteger[] clientData = (BigInteger[]) o;
                Arrays.sort(clientData);
                oos.writeObject(clientData);
                oos.flush();
            }

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    public static BigInteger average(BigInteger[] biarr) {
        BigInteger sum = BigInteger.ZERO;
        for (BigInteger bi : biarr) {
            sum = sum.add(bi);
        }
        return sum.divide(new BigInteger(""+biarr.length));
    }

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
            while (true) {
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