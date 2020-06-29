import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class ChannelTest {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
//        test1();
//        test2();
//        test3();
        long end = System.currentTimeMillis();
        System.out.println("test1耗时："+(end-start));
//        test4();
        test5();
    }
    //1. 利用通道完成文件的复制(非直接缓冲区)
    public static void test1(){
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            fileInputStream = new FileInputStream("1.jpg");
            fileOutputStream = new FileOutputStream("2.jpg");
            //① 获取通道
            inChannel = fileInputStream.getChannel();
            outChannel = fileOutputStream.getChannel();
            //② 分配指定大小的缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);
            //③ 将通道中的数据存入缓冲区中
            while (inChannel.read(buf) != -1){
                buf.flip(); // 切换成读取数据的模式
                //④将缓冲区的数据写入通道中
                outChannel.write(buf);
                buf.clear(); // 清空缓冲区
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeChannel(outChannel);
            closeChannel(inChannel);
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //2. 利用直接缓冲区完成文件的复制(内存映射文件)
    public static void test2() {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
            outChannel = FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.WRITE,StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);

            //内存映射文件
            MappedByteBuffer inMappedBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            MappedByteBuffer outMappedBuf = outChannel.map(FileChannel.MapMode.READ_WRITE,0, inChannel.size());
            //直接对缓冲区进行数据的读写操作
            byte[] dst = new byte[inMappedBuf.limit()];
            inMappedBuf.get(dst);
            outMappedBuf.put(dst);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeChannel(inChannel);
            closeChannel(outChannel);
        }
    }

    //3.通道之间的数据传递(直接缓冲区)
    public static void test3() {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
            outChannel = FileChannel.open(Paths.get("4.jpg"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);

//            inChannel.transferTo(0, inChannel.size(), outChannel);
            outChannel.transferFrom(inChannel, 0, inChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeChannel(inChannel);
            closeChannel(outChannel);
        }
    }

    // 分散于聚集
    public static void test4() throws IOException {
        RandomAccessFile raf1 = new RandomAccessFile("1.txt", "rw");
        //1.获取通道
        FileChannel channel1 = raf1.getChannel();
        //2.分配指定大小的缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(100);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);

        //3.分散读取
        ByteBuffer[] bufs = {buf1, buf2};
        channel1.read(bufs);
        for (ByteBuffer byteBuffer : bufs){
            byteBuffer.flip();
        }
        System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
        System.out.println("---------------------");
        System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));

        //4.聚集写入
        RandomAccessFile raf2 = new RandomAccessFile("2.txt", "rw");
        FileChannel channel2 = raf2.getChannel();
        channel2.write(bufs);
    }

    // 字符集
    public static void test5() throws IOException {
        Charset cs1 = Charset.forName("GBK");
        //获取编码器与解码器
        CharsetEncoder encoder = cs1.newEncoder();
        CharsetDecoder decoder = cs1.newDecoder();

        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("吕颂是个武痴。");
        charBuffer.flip();

        //编码
        ByteBuffer byteBuffer = encoder.encode(charBuffer);
        for(int i = 0; i < 14; i++){
            System.out.println(byteBuffer.get());
        }
        //解码
        byteBuffer.flip();
        CharBuffer charBuffer2 = decoder.decode(byteBuffer);
        System.out.println(charBuffer2);
        System.out.println("-----------------------");

        Charset cs2 = Charset.forName("UTF-8");
        byteBuffer.flip();
        CharBuffer charBuffer3 = cs2.decode(byteBuffer);
        System.out.println(charBuffer3.toString());
    }



    public static void closeChannel(Channel channel) {
        if (channel != null){
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
