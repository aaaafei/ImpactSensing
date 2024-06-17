import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.text.SimpleDateFormat;

public class JstiNettyServerHandle extends ChannelInboundHandlerAdapter{
    private int icount=0;
    private boolean isIOTContent=false;

    public JstiNettyServerHandle(int ic){
        icount=ic;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client connect");
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client close");
    }


    //读取数据的事件
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("server:"+ctx);//输出上下文内容
        ByteBuf buf= (ByteBuf) msg;
        String strBuf=buf.toString(CharsetUtil.UTF_8);
        ctx.write(Unpooled.copiedBuffer("0xFFFF*SCOS,OM,862205058915408,S6#<\n>",CharsetUtil.UTF_8));
        //ctx.writeAndFlush("0xFFFF*SCOS,OM,862205058915408,S6#\n");
        System.out.println(sdf.format(new java.util.Date())+",客户端发来消息："+strBuf);
       // if(strBuf.indexOf("H0")!=-1){

            isIOTContent=true;

           // ctx.write(Unpooled.copiedBuffer("0xFFFF*SCOS,OM,862205058915408,S6#<LF>",CharsetUtil.UTF_8));
     //   }
    //    ReferenceCountUtil.release(msg);
    }

    //数据读取完毕事件
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //向客户端返回数据
        if(icount==0){
           // ctx.writeAndFlush(Unpooled.copiedBuffer("0xFFFF*SCOS,OM,862205058915408,S5,0,0,10,10#\n",CharsetUtil.UTF_8));
           // ctx.writeAndFlush(convertToHex("0xFFFF*SCOS,OM,862205058915408,S5,0,0,30,30#\n"));
          //  icount++;
        }
          //  ctx.writeAndFlush(Unpooled.copiedBuffer("0xFFFF*SCOS,OM,862205058915408,S6#<>",CharsetUtil.UTF_8));
         //   ctx.writeAndFlush(convertToHex("0xFFFF*SCOS,OM,862205058915408,S6#\n"));
        if(isIOTContent){
         //   ctx.writeAndFlush(Unpooled.copiedBuffer("0xFFFF*SCOS,OM,862205058915408,S6#<LF>",CharsetUtil.UTF_8));
         //   isIOTContent=false;
        }
         ctx.flush();
    }

    //处理异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    private byte[] getSendCommand(){
        byte[] order ="*SCOS,OM,862205058915408,S6#\n".getBytes();
// add 0xFF,0xFF
        return addByte(new byte[]{(byte) 0xFF,(byte) 0xFF},order);
    }
    private byte[] addByte(byte[] b1,byte[] b2){
        byte[] b = new byte[b1.length+b2.length];
        System.arraycopy(b1, 0, b, 0, b1.length);
        System.arraycopy(b2, 0, b, b1.length, b2.length);
        return b;
    }

        public  String convertToHex(String str) {
            byte[] bytes = str.getBytes();
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : bytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }
            return hexStringBuilder.toString();
        }
}
