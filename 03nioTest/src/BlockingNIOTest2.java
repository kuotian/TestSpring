import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BlockingNIOTest2 {
    @Test
    public void client() throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (inChannel.read(byteBuffer) != -1){
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        socketChannel.shutdownOutput(); //不添加则阻塞

        //接收服务端的反馈
        int len = 0;
        while((len = socketChannel.read(byteBuffer)) != -1){
            byteBuffer.flip();
            System.out.println(new String(byteBuffer.array(),0, len));
            byteBuffer.clear();
        }

        inChannel.close();
        socketChannel.close();
    }

    @Test
    public void server() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        FileChannel outChannel = FileChannel.open(Paths.get("7.jpg"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
        serverSocketChannel.bind(new InetSocketAddress(9898));

        SocketChannel socketChannel = serverSocketChannel.accept();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (socketChannel.read(byteBuffer) != -1){
            byteBuffer.flip();
            outChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        //发送反馈给客户端
        byteBuffer.put("server accept".getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);

        socketChannel.close();
        serverSocketChannel.close();
        outChannel.close();
    }
}
