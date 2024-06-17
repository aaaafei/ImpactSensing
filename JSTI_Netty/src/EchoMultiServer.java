import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class EchoMultiServer {
    private static ServerSocket server = null;
    private static ExecutorService threadPool;

    public static void main(String[] args) {
        threadPool = Executors.newCachedThreadPool();
        threadPool.submit(new Monitor());

        try {
            server = new ServerSocket(27116);

            System.out.println("Server listening on port 27116 ....");


            while (true) {
                Socket socket = server.accept();
                System.out.println(getCurrentTime()+" , Thread created  by :"+socket.getRemoteSocketAddress());
                threadPool.submit(new EchoThread(socket));
            }
        } catch (SocketException e) {
            System.out.println("Server is down");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void shutdownServer() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        threadPool.shutdown();
        System.exit(0);
    }

    private static class Monitor implements Runnable {

        @Override
        public void run() {
            try {
                while (System.in.read() != '\n') {}
            } catch (IOException e) {
            }

            shutdownServer();
        }
    }
    public static String getCurrentTime()
    {    //将当前时间转换成规定格式的字符串
        long now=System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strReturn;
        strReturn=formatter.format(now);
        return strReturn;
    }
}

class EchoThread implements Runnable {
    private Socket socket = null;
    private byte[] buffer = new byte[512];

    public EchoThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            BufferedReader bis=new BufferedReader(new InputStreamReader(is));
            while (true) {
                String line=bis.readLine();
                if(line!=null){
                System.out.println(EchoMultiServer.getCurrentTime()+", from :"+socket.getRemoteSocketAddress()+" is:"+line);
                }

            }

          //  is.read(buffer);

          //  os.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            try {
//                socket.close();
//                System.out.println(socket.getRemoteSocketAddress()+" closed");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
}
