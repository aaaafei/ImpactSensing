import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class JstiNettyServerHandle_01 extends ChannelHandlerAdapter {
    public void channelRead(ChannelHandlerContext ctx,Object msg)  throws Exception {

    }
    public void channelreadcomplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
