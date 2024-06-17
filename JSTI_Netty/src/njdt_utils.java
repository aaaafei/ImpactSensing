import java.text.SimpleDateFormat;

public class njdt_utils {

    //   imei=862205058915408  ,  mac=EC:5D:17:46:49:10  ,  qrcode=1689326503   open  lock  sim=898604d41623d0980305

    //   imei=862205058920614  ,  mac=D0:3B:61:77:AF:1F  ,  qrcode=1689326504   close  lock sim=898604d41623d0980306

    //   imei=860537062671425  ,  mac=8C:59:DC:F7:20:35  ,  qrcode=66155010026    sim=898604d41623d0980304
    public static byte[] reply_S6_To_Clinet(){
        String temp="*SCOS,OM,862205058915408,S6#<LF>" ;
        System.out.println("**"+getCurrentTime()+" ,服务器发送:"+temp+"**");
        byte[] order =temp.getBytes();
        return addByte(new byte[]{(byte) 0xFF,(byte) 0xFF},order);
    }

    public static  String getCurrentTime()
    {    //将当前时间转换成规定格式的字符串
        long now=System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strReturn;
        strReturn=formatter.format(now);
        return strReturn;
    }

    public static byte[] addByte(byte[] b1,byte[] b2){
        byte[] b = new byte[b1.length+b2.length];
        System.arraycopy(b1, 0, b, 0, b1.length);
        System.arraycopy(b2, 0, b, b1.length, b2.length);
        return b;
    }

}

