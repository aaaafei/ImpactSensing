import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class njdt_iot_server {
    private Connection conn = null;
    private Statement stmt = null;
    public boolean initDataBase(){
        boolean isOK=true;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/njdt?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC","root","123456");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/njdt?useUnicode=true&characterEncoding=utf8&serverTimezone=Hongkong&useSSL=false","root","fangxun@mysql.2022");
            stmt = conn.createStatement();
        }catch(SQLException se){
            // 处理 JDBC 错误
            isOK=false;
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            isOK=false;
            e.printStackTrace();
        }
        return isOK;
    }
    public void closeDataBase(){
        try{
            if(stmt!=null) stmt.close();
        }catch(SQLException se2){
        }// 什么都不做
        try{
            if(conn!=null) conn.close();
        }catch(SQLException se){
            se.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        njdt_iot_server nis=new njdt_iot_server();
        // 设置boss线程的名称为"BOSS"
        Thread.currentThread().setName("BOSS");
        // 创建ServerSocketChannel
        ServerSocketChannel ssc = ServerSocketChannel.open();
        /**
         * 1. ServerSocketChannel配置为非阻塞模式
         * 2. selector通常都是和非阻塞channel进行搭配
         * 非阻塞channel一旦感兴趣的事件，则可以通过selector.select()方法将事件追加到selector的selectedKeys中
         */
        ssc.configureBlocking(false);
        // 创建boss线程的selector，该selector专门监听accept事件
        Selector boss = Selector.open();
        // 将创建好的ServerSocketChannel和关注的accept事件注册到该selector
        SelectionKey bossKey = ssc.register(boss, 0, null);
        bossKey.interestOps(SelectionKey.OP_ACCEPT);
        // ServerSocketChannel和端口进行绑定
        ssc.bind(new InetSocketAddress(29118));  //监听29118 端口
        // 创建一定数量的worker，默认数量为当前内核数量的10倍
        int cpuNum = Runtime.getRuntime().availableProcessors();
        System.out.println("当前机器内核数量:" + cpuNum);
        Worker[] workers = new Worker[cpuNum*10];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("WORKER-" + i);
            workers[i].initWorker();
        }

        if(nis.initDataBase()){
            System.out.println("数据库连接成功，已实例化Statement对象...");
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    nis.closeDataBase();
                }
            });
        }

        AtomicInteger count = new AtomicInteger();
        while (true) {
            /**
             * 轮训check查询的时候是否就绪，如果未就绪则select不会返回，只有监听的事件发生select()方法才返回
             * 每个selector中有两个集合：
             * 集合1: interested keys集合，存储的是当前selector感兴趣的channel + 事件类型(accept, connect, read, write)
             * 集合2：selected keys集合，存储的是selector.select()方法调用之后扫描到的新发生的事件
             * 注意每次select()方法调用时，可以理解为是往"selected keys集合"中追加本次select()新扫描的事件，上次select()并且加入的事件不会自动清除。
             * 因此后面迭代器迭代"selected keys集合"时需要及时remove掉，否则下次循环会重复消费处理
             */
            boss.select();
            Iterator<SelectionKey> bossSelectedKeysIter = boss.selectedKeys().iterator();
            while (bossSelectedKeysIter.hasNext()) {
                SelectionKey key = bossSelectedKeysIter.next();
                /**
                 * 注意及时remove掉已经监听到并且马上就要处理的事件
                 * 因为selector的selectedKeys集合不会自动将key清除掉，这会导致下次循环重复处理
                 */
                bossSelectedKeysIter.remove();
                if (key.isAcceptable()) {
                    SelectableChannel keyChannel = key.channel();
                    ServerSocketChannel sscFromSelector = (ServerSocketChannel) keyChannel;
                   // System.out.println("sscFromSelector == ssc?" + (sscFromSelector == ssc));

                    SocketChannel sc = sscFromSelector.accept();
                    sc.configureBlocking(false);
                    // 建立链接打印日志
                    System.out.println("线程:" + Thread.currentThread().getName() + ":" + "已连接，地址: " + sc.getRemoteAddress());
                    // 分配worker
                  //  System.out.println("线程:" + Thread.currentThread().getName() + ":" + "before register --- " + sc.getRemoteAddress());
                    workers[count.incrementAndGet() % workers.length].registerChannel(sc);
                  //  System.out.println("线程:" + Thread.currentThread().getName() + ":" + "after register --- " + sc.getRemoteAddress());
                }
            }
        }
    }

    static class Worker implements Runnable {
        private Thread thread;
        private Selector workerSelector;
        private String name;
        ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque<>();

        public Worker(String name) {
            this.name = name;
        }

        public void initWorker() throws IOException {
            thread = new Thread(this, name);
            workerSelector = Selector.open();
            thread.start();
        }

        /**
         * 将一个SocketChannel分配给当前worker
         */
        public void registerChannel(SocketChannel sc) {
            queue.add(() -> {
                try {
                    sc.register(workerSelector, SelectionKey.OP_READ, null);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            // 注册到任务队列之后唤醒一下
            workerSelector.wakeup();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        workerSelector.select();
                        /**
                         * 注册分配的channel
                         */
                        Runnable scRegisterTask = queue.poll();
                        if (scRegisterTask != null) {
                            scRegisterTask.run();
                        }

                        Iterator<SelectionKey> workerSelectedKeysIter = workerSelector.selectedKeys().iterator();
                        while (workerSelectedKeysIter.hasNext()) {
                            SelectionKey key = workerSelectedKeysIter.next();
                            workerSelectedKeysIter.remove();
                            if (key.isReadable()) {
                                SocketChannel channel = (SocketChannel) key.channel();
                                ByteBuffer byteBuffer = ByteBuffer.allocate(300);  //默认采用300字节缓冲区
                                String sql_cancel = null;
                                String sql_insert = null;
                                /**
                                 * 将channel中的数据写入buffer
                                 * 注意：两种情况需要做好处理：
                                 * 1. 如果客户端正常关闭了socket，则read返回的是-1，这时需要将客户端对应的channel从boss selector中cancel
                                 * 2. 如果客户端非正常关闭了socket，则需要捕获read方法，并将客户端对应的channel从boss selector中cancel
                                 */
                                int readCnt = channel.read(byteBuffer);
                                if (readCnt == -1) {
                                    System.out.println("线程:" + Thread.currentThread().getName() + ":" + "客户端链接已关闭，取消监听...");
                                    key.cancel();
                                    sql_cancel = "insert into rawdata(clientip,contents) values('" + channel.getRemoteAddress().toString() + "','--Connection Canceled--')";
                                }

                                // 切换buffer为读模式
                                byteBuffer.flip();
                                // 读取
                                CharBuffer cbf = Charset.forName("UTF-8").decode(byteBuffer);
                                String strbuf = cbf.toString();
                                // 将收到的客户端信息 拆分写入类结构
                                njdt_Raw_Data NRD = new njdt_Raw_Data(channel.getRemoteAddress().toString(), strbuf);
                                // 写入数据库
                                Connection conn = null;
                                Statement stmt = null;

                                try {
                                    // 注册 JDBC 驱动
                                    Class.forName("com.mysql.cj.jdbc.Driver");
                                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/njdt?useUnicode=true&characterEncoding=utf8&serverTimezone=Hongkong&useSSL=false","root","fangxun@mysql.2022");
                                    System.out.println("数据库连接成功，已实例化Statement对象...");
                                    stmt = conn.createStatement();
                                    if (sql_cancel != null && sql_cancel.length() > 8) {
                                        stmt.executeUpdate(sql_cancel);
                                    }

                                    if (NRD.isValid() && sql_cancel == null) {
                                        sql_insert = "insert into rawdata(clientip,clientport,clientimei,catalog,catalogval,voltage,signalval,contents)values('";
                                        sql_insert += NRD.getClientIP() + "','";
                                        sql_insert += NRD.getClientPort() + "','";
                                        sql_insert += NRD.getClientIMEI() + "','";
                                        sql_insert += NRD.getCatalog() + "','";
                                        sql_insert += NRD.getCatalogVal() + "','";
                                        sql_insert += NRD.getVoltage() + "','";
                                        sql_insert += NRD.getSignal() + "','";
                                        sql_insert += NRD.getContents() + "')";
                                        //   System.out.println(sql_insert);
                                        stmt.executeUpdate(sql_insert);
                                    }
                                    stmt.close();
                                    conn.close();
                                } catch (SQLException se) {
                                    // 处理 JDBC 错误
                                    se.printStackTrace();
                                } catch (Exception e) {
                                    // 处理 Class.forName 错误
                                    e.printStackTrace();
                                } finally {
                                    // 关闭资源
                                    try {
                                        if (stmt != null) stmt.close();
                                        stmt = null;
                                        System.out.println("数据库Statement对象已关闭...");
                                    } catch (SQLException se2) {
                                    }// 什么都不做
                                    try {
                                        if (conn != null) conn.close();
                                        conn = null;
                                        System.out.println("数据库Connection对象已关闭...");
                                    } catch (SQLException se) {
                                        se.printStackTrace();
                                    }
                                }

                                //   database  end;

                                if (strbuf.indexOf("S6") != -1 && strbuf.indexOf("862205058920614")!=-1) {
                                    System.out.print("\n862205058920614==>" + njdt_utils.getCurrentTime() + ",线程:" + Thread.currentThread().getName() + ",收到客户端:" + channel.getRemoteAddress());
                                    System.out.print(",内容:" + cbf + "\n");
                                }
                                if (strbuf.indexOf("S6") != -1 && strbuf.indexOf("862205058915408")!=-1) {
                                      System.out.print("\n862205058915408==>" + njdt_utils.getCurrentTime() + ",线程:" + Thread.currentThread().getName() + ",收到客户端:" + channel.getRemoteAddress());
                                      System.out.print(",内容:" + cbf + "\n");
                                }
                                if (strbuf.indexOf("S6") != -1 && strbuf.indexOf("860537062671425")!=-1) {
                                      System.out.print("\n860537062671425==>" + njdt_utils.getCurrentTime() + ",线程:" + Thread.currentThread().getName() + ",收到客户端:" + channel.getRemoteAddress());
                                      System.out.print(",内容:" + cbf + "\n");
                                }
                                //--------------------------------
                                if (strbuf.indexOf("H0") != -1 && strbuf.indexOf("862205058920614")!=-1) {
                                    System.out.print("\n862205058920614-->" + njdt_utils.getCurrentTime() + ",线程:" + Thread.currentThread().getName() + ",收到客户端:" + channel.getRemoteAddress());
                                    System.out.print(",内容:" + cbf + "\n");
                                    String temp = "0xFFFF*SCOS,OM,862205058920614,S6#<LF>";
                                    ByteBuffer buf = ByteBuffer.allocate(temp.length());
                                    buf.clear();
                                    buf.put(temp.getBytes());
                                    buf.flip();
                                    while (buf.hasRemaining()) {
                                        channel.write(buf);
                                    }
                                    buf = null;
                                }

                                    //*****************************************************************
                                if (strbuf.indexOf("H0") != -1 && strbuf.indexOf("862205058915408")!=-1) {
                                    System.out.print("\n862205058915408-->" + njdt_utils.getCurrentTime() + ",线程:" + Thread.currentThread().getName() + ",收到客户端:" + channel.getRemoteAddress());
                                    System.out.print(",内容:" + cbf + "\n");
                                    String temp = "0xFFFF*SCOS,OM,862205058915408,S6#<LF>";
                                    ByteBuffer buf = ByteBuffer.allocate(temp.length());
                                    buf.clear();
                                    buf.put(temp.getBytes());
                                    buf.flip();
                                    while (buf.hasRemaining()) {
                                        channel.write(buf);
                                    }
                                    buf = null;
                                }
                                    //*****************************************************************
                                if (strbuf.indexOf("H0") != -1 && strbuf.indexOf("860537062671425")!=-1) {
                                    System.out.print("\n860537062671425-->" + njdt_utils.getCurrentTime() + ",线程:" + Thread.currentThread().getName() + ",收到客户端:" + channel.getRemoteAddress());
                                    System.out.print(",内容:" + cbf + "\n");
                                    String temp = "0xFFFF*SCOS,OM,860537062671425,S6#<LF>";
                                    ByteBuffer buf = ByteBuffer.allocate(temp.length());
                                    buf.clear();
                                    buf.put(temp.getBytes());
                                    buf.flip();
                                    while (buf.hasRemaining()) {
                                        channel.write(buf);
                                    }
                                    buf = null;

                                }

                            }
                        }
                    } catch (IOException e) {
                         thread.interrupt();
                        e.printStackTrace();
                    }
                }
            }finally {
                thread.interrupt();
            }
        }
    }

}
