import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
public class JstiNettyServer {

    public static void main(String[] args) throws InterruptedException {

        //创建一个线程组，接收客户端的连接
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        //创建一个线程组，处理网络操作
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        //创建启动对象来配置参数
        ServerBootstrap b=new ServerBootstrap();
        b.group(bossGroup,workerGroup) //设置两个线程组
                .channel(NioServerSocketChannel.class)  //使用NioServerSocketChannel作为服务器通道的实现
                .option(ChannelOption.SO_BACKLOG,128)  //设置线程队列中等待连接的个数
                .childOption(ChannelOption.SO_KEEPALIVE,true) //保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个初始化通道的对象
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new JstiNettyServerHandle(0));  //往pipeline链中添加自定义的handle类
                    }
                });
        ChannelFuture cf=b.bind(27116).sync();//绑定端口，设置非阻塞。
        System.out.println("服务已启动，监听端口:27116");

        //关闭通道，关闭线程组
        cf.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

    }
}
