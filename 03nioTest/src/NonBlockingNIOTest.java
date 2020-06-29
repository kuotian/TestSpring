import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;

public class NonBlockingNIOTest {
    @Test
    public void client() throws IOException {
        //1.获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        //2.切换成非阻塞模式
        socketChannel.configureBlocking(false);
        //3.分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //4.发送数据给服务端
        byteBuffer.put(new Date().toString().getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        byteBuffer.clear();

        //5.关闭通道
        socketChannel.close();
    }

    @Test
    public void server() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2.切换成非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //3.绑定连接
        serverSocketChannel.bind(new InetSocketAddress(9898));
        //4.获取选择器
        Selector selector = Selector.open();
        //5.将通道注册到选择器,并且制定“监听接收事件”
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //6.轮询式的获取选择器上已经“准备就绪”事件
        while (selector.select() > 0){
            //7.获取当前选择器其中所有注册的“选择键”（已就绪的监听事件）
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while(it.hasNext()){
                //8.获取准备就绪的是事件
                SelectionKey sk = it.next();
                //9.判断具体是什么事件准备就绪
                if(sk.isAcceptable()){
                    //10.若“接收就绪”，获取客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //11.切换非阻塞模式
                    socketChannel.configureBlocking(false);
                    //12.非该通道注册到选择器上
                    socketChannel.register(selector, SelectionKey.OP_READ);

                }else if(sk.isReadable()){
                    //13.获取当前选择器上“读就绪”状态的通道
                    SocketChannel socketChannel = (SocketChannel)sk.channel();
                    //14.读取数据
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int len = 0;
                    while((len = socketChannel.read(byteBuffer)) > 0){
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(), 0, len));
                        byteBuffer.clear();
                    }
                }
                //15.取消选择键SelectionKey
                it.remove();
            }
        }
    }
}