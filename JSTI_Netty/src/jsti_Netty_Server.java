import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Long;
public class jsti_Netty_Server {
    private ServerSocket  server;
    private Socket socket;
    private int svrPort=27116;
    private String IMEI="862205058915408";

    public static void main(String[] args) {

        jsti_Netty_Server jns=new jsti_Netty_Server();

        ServerSocket listener = null;
        String line;
        BufferedReader is;
        BufferedWriter os;
        Socket socketOfServer = null;


        try {
            listener = new ServerSocket(jns.svrPort);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        try {
            System.out.println("服务已启动，等待客户端连接......");

            // Accept client connection request
            // Get new Socket at Server.
            socketOfServer = listener.accept();
            System.out.println(jns.getCurrentTime()+" 客户端已连接,来自:"+socketOfServer.getRemoteSocketAddress());

            // Open input and output streams
            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));


            while (true) {
                // Read data to the server (sent from client).
                line = is.readLine();
               // System.out.println("**"+j001.getCurrentTime()+" , 收到无需处理的指令，内容:"+line+"**");
                if(line.indexOf("Q0")!=-1){
                    //Q0（签到）指令，服务器不需要回应
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：Q0（签到），内容:"+line+"**");
                }else if(line.indexOf("H0")!=-1){
                    // H0（心跳）指令，服务器不需要回应
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：H0（心跳），内容:"+line+"**");
                    socketOfServer.getOutputStream().write(jns.reply_S6_Re_To_Clinet());
                }else if(line.indexOf("L0")!=-1){
                    // L0（解锁指令），服务器需要回应
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：L0（解锁指令），内容:"+line+"**");
                   // socketOfServer.getOutputStream().write(jns.send_L0_To_Clinet(jns.getCurDateTimeStr()));
                }else if(line.indexOf("L1")!=-1){
                    //L1（关锁指令，锁主动上报）,服务器需要回应
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：L1（关锁指令，锁主动上报），内容:"+line+"**");
                  //  socketOfServer.getOutputStream().write(jns.reply_L1_Re_To_Clinet(jns.getCurDateTimeStr()));
                }else if(line.indexOf("D0")!=-1){
                    //D0（获取定位指令，单次）,服务器需要回应
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：D0（获取定位指令，单次），内容:"+line+"**");
                }else if(line.indexOf("D1")!=-1){
                    //D1（定位追踪指令,定位使用 D0 指令上传）
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：D1（定位追踪指令,定位使用 D0 指令上传），内容:"+line+"**");
                }else if(line.indexOf("S6")!=-1){
                    //S5（获取锁信息）
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：S6（获取锁信息），内容:"+line+"**");
                }else if(line.indexOf("S8")!=-1){
                    //S8（找车指令）
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：S8（找车指令），内容:"+line+"**");
                }else if(line.indexOf("G0")!=-1){
                    //G0（获取锁固件信息）
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：G0（获取锁固件信息），内容:"+line+"**");
                } else if(line.indexOf("W0")!=-1) {
                    //W0（报警指令）,服务器需要回应
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：W0（报警指令），内容:"+line+"**");
                 //   socketOfServer.getOutputStream().write(jns.reply_W0_Re_To_Clinet(jns.getCurDateTimeStr()));
                }else if(line.indexOf("U0")!=-1){
                    //U0（检测升级/启动升级）
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：U0（检测升级/启动升级），内容:"+line+"**");
                }else if(line.indexOf("U1")!=-1){
                    //U1（获取升级数据）
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：U1（获取升级数据），内容:"+line+"**");
                }else if(line.indexOf("U2")!=-1){
                    //U2（升级结果通知）
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：U2（升级结果通知），内容:"+line+"**");
                }else if(line.indexOf("K0")!=-1){
                    //K0（设置/获取 BLE 8 字节通信 KEY）
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：K0（设置/获取 BLE 8 字节通信 KEY），内容:"+line+"**");
                }else if(line.indexOf("I0")!=-1){
                    //I0（获取 SIM 卡 ICCID 号）
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：I0（获取 SIM 卡 ICCID 号），内容:"+line+"**");
                }else if(line.indexOf("M0")!=-1){
                    //M0（获取锁蓝牙 MAC 地址）
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：M0（获取锁蓝牙 MAC 地址），内容:"+line+"**");
                }else if(line.indexOf("S0")!=-1){
                    //S0（关机指令）
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：S0（关机指令），内容:"+line+"**");
                }else if(line.indexOf("S1")!=-1){
                    //S1（重启指令）
                    System.out.println("**"+jns.getCurrentTime()+" ，收到客户端指令：S1（重启指令），内容:"+line+"**");
                } else{
                    System.out.println("**===============================================**");
                }
                // End of line
                //  os.newLine();
                // Flush data.
                 socketOfServer.getOutputStream().flush();


                // If users send QUIT (To end conversation).
                if (line.equals("QUIT")) {
                    os.write(">> OK");
                    os.newLine();
                    os.flush();
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        System.out.println("Sever stopped!");
    }

    public boolean InitSocketServer()
    {
        boolean isOK=true;
        try {
            server = new ServerSocket(svrPort);
            System.out.println(getCurrentTime()+" 正常启动Socket，监听端口:"+svrPort);
            socket = server.accept();
        } catch (IOException e) {
            e.printStackTrace();
            isOK=false;
        }
        return isOK;
    }

    public boolean readAndWrite()
    {
        boolean willQuit=false;
        try{
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            BufferedReader    br = new BufferedReader(in);
            BufferedWriter    bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String content;
            if((content = br.readLine()) != null && !"".equals("quit")){
                throw new Exception("quit");
            }
            while ((content = br.readLine()) != null && !"".equals(content)){
                System.out.println(content);
                bw.write("thank you !");
                bw.flush();
            }

        }catch(Exception e){
            willQuit=true;
        }
        return willQuit;
    }
    public void closeServer()
    {
        try{
            socket.close();
            server.close();
        }catch( Exception ee){
            System.out.println("服务器关闭异常!");
        }
    }

    public int getSocketPort()
    {
        return svrPort;
    }
    public  String getCurDateTimeStr()
    {    //将当前时间转换成规定格式的字符串
        long now=System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
        String strReturn;
        strReturn=formatter.format(now);
        return strReturn;

    }
    // 收到H0时，发送S6指令
    private byte[] reply_S6_Re_To_Clinet(){
        String temp="*SCOS,OM,862205058920614,S6#<LF>" ;
        System.out.println("**"+getCurrentTime()+" ,服务器发送:"+temp+"**");
        byte[] order =temp.getBytes();
        return addByte(new byte[]{(byte) 0xFF,(byte) 0xFF},order);
    }
    // 出现L1时服务器回应Re指令
    private byte[] reply_L1_Re_To_Clinet(String str){
        String temp="*CMDS,OM,862205058915408,"+str+",Re,L1#<LF>\n" ;
        System.out.println("**"+getCurrentTime()+" ,服务器发送:"+temp+"**");
        byte[] order =temp.getBytes();
        return addByte(new byte[]{(byte) 0xFF,(byte) 0xFF},order);
    }
    // 出现L0时服务器回应Re指令
    private byte[] reply_L0_Re_To_Clinet(String str){
        String temp="*CMDS,OM,862205058915408,"+str+",Re,L0#<LF>\n" ;
        System.out.println("**"+getCurrentTime()+" ,服务器发送:"+temp+"**");
        byte[] order =temp.getBytes();
        return addByte(new byte[]{(byte) 0xFF,(byte) 0xFF},order);
    }
    // 发送L0指令给客户端
    private byte[] send_L0_To_Clinet(String str){
        String temp="*CMDS,OM,862205052953876,"+str+",L0,0,000000000000,"+ String.valueOf(System.currentTimeMillis())+"#<LF>\n" ;
        System.out.println("$$"+getCurrentTime()+" ,服务器发送:"+temp+"$$");
        byte[] order =temp.getBytes();
        return addByte(new byte[]{(byte) 0xFF,(byte) 0xFF},order);
    }
    // 出现W0时服务器回应Re指令
    private byte[] reply_W0_Re_To_Clinet(String str){
        String temp="*CMDS,OM,862205058915408,"+str+",W0#<LF>\n" ;
        System.out.println("$$"+getCurrentTime()+" ,服务器发送:"+temp+"$$");
        byte[] order =temp.getBytes();
        return addByte(new byte[]{(byte) 0xFF,(byte) 0xFF},order);
    }
    //  *CMDS,OM,123456789123456,200318123020,S5#<LF>      获取锁信息
    private byte[] resetOrder(String str){
        // command
        String temp="*CMDS,OM,862205052953876,"+str+",L0,0,000000000000,"+ String.valueOf(System.currentTimeMillis())+"#<LF>\n" ;
        temp="*CMDS,OM,862205052953876,"+str+",Re,L1#<LF>\n" ;
        System.out.println("$$"+getCurrentTime()+" ,服务器发送:"+temp+"$$");
        byte[] order =temp.getBytes();
        // add 0xFF,0xFF
        return addByte(new byte[]{(byte) 0xFF,(byte) 0xFF},order);
    }
    public  String getCurrentTime()
    {    //将当前时间转换成规定格式的字符串
        long now=System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strReturn;
        strReturn=formatter.format(now);
        return strReturn;
    }
    private byte[] getSendOrder(String str){
        // command
        String temp="*CMDS,OM,862205052953876,"+str+",L0,0,000000000000,"+ String.valueOf(System.currentTimeMillis())+"#<LF>\n" ;
        System.out.println("$$"+getCurrentTime()+" ,服务器发送:"+temp+"$$");
        byte[] order =temp.getBytes();
        // add 0xFF,0xFF
        return addByte(new byte[]{(byte) 0xFF,(byte) 0xFF},order);
    }
    private byte[] addByte(byte[] b1,byte[] b2){
        byte[] b = new byte[b1.length+b2.length];
        System.arraycopy(b1, 0, b, 0, b1.length);
        System.arraycopy(b2, 0, b, b1.length, b2.length);
        return b;
    }
}
