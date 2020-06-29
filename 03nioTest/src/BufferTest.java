import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.ShortBuffer;

public class BufferTest {
    static final String str = "abcde";
    public static void main(String[] args) {
        test1();
        System.out.println("================");
        test2();
        System.out.println("================");
        test3();


    }

    public static void test1(){
        //1.分配一个指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        printInfo("allocate", buffer);  // position:0  limit:1024  capacity:1024
        //2.利用put()存入数据到缓冲区
        buffer.put(str.getBytes());
        printInfo("put", buffer);   // position:5  limit:1024  capacity:1024
        //3.切换读取数据的模式
        buffer.flip();
        printInfo("flip", buffer); // position:0  limit:5  capacity:1024
        //4.利用get()读取缓冲区的数据
        byte[] dst = new byte[buffer.limit()];
        buffer.get(dst);
        System.out.println(new String(dst)); // abcde
        printInfo("get", buffer);   // position:5  limit:5  capacity:1024
        //5.rewind()
        buffer.rewind();
        printInfo("rewind", buffer);    // position:0  limit:5  capacity:1024
        //6.clear():清空缓冲区 只是恢复position、limit的值，并不真的清空其中数据，下次操作后覆盖
        buffer.clear();
        printInfo("clear", buffer);     // position:0  limit:1024  capacity:1024
    }

    public static void test2(){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(str.getBytes());
        buffer.flip();
        byte[] dst = new byte[buffer.limit()];
        buffer.get(dst, 0 ,2);
        System.out.println(new String(dst, 0 ,2)); //ab

        System.out.println(buffer.position()); // 2

        //mark():标记
        buffer.mark();
        buffer.get(dst, 2, 2);
        System.out.println(buffer.position()); // 4

        //reset():恢复到mark的位置
        buffer.reset();
        System.out.println(buffer.position()); // 2

        //判断缓冲区中是否还有剩余数据
        if(buffer.hasRemaining()){
            // 获取缓冲区中可以操作的数量
            System.out.println(buffer.remaining()); // 3
        }
    }
    public static void test3(){
        // 分配直接缓冲区
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        System.out.println(buffer.isDirect()); // true
    }



    public static void printInfo(String info, Buffer buffer){
        System.out.println("------"+info+"------");
        System.out.println("position:"+buffer.position());
        System.out.println("limit:"+buffer.limit());
        System.out.println("capacity:"+buffer.capacity());
    }

}






