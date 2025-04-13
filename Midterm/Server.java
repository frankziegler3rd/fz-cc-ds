import java.io.*;
import java.net.*;
import java.util.concurrent.Executor;
import java.math.BigInteger;

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
            Object o = ois.readObject();
            BigInteger average;
            if (o instanceof BigInteger[]) {
                BigInteger[] clientData = (BigInteger[]) o;
                average = average(clientData);
                oos.writeObject(average);
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