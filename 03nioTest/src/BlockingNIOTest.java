import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BlockingNIOTest {
    @Test
    public void client() throws IOException {
        //1.获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        //2.分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //3.读取本地文件，并发送到服务器
        while (inChannel.read(byteBuffer) != -1){
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        //4.关闭通道
        inChannel.close();
        socketChannel.close();
    }

    @Test
    public void server() throws IOException {
        //1.获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        FileChannel outChannel = FileChannel.open(Paths.get("6.jpg"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
        //2.绑定连接
        serverSocketChannel.bind(new InetSocketAddress(9898));
        //3.获取客户端连接的通道
        SocketChannel socketChannel = serverSocketChannel.accept();
        //4.分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //5.接收客户端的数据，并保存到本地
        while (socketChannel.read(byteBuffer) != -1){
            byteBuffer.flip();
            outChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        //6.关闭通道
        socketChannel.close();
        serverSocketChannel.close();
        outChannel.close();
    }
}
