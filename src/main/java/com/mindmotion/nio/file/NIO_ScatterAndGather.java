package com.mindmotion.nio.file;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class NIO_ScatterAndGather {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);

        //绑定端口并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建一个buff数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(4);
        byteBuffers[1] = ByteBuffer.allocate(8);

        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();

        //从客户端最多接受内容长度
        int contextLength = 12;

        while (true){
            long totalReadLength = 0;
            while (totalReadLength < contextLength){
                long readLength = socketChannel.read(byteBuffers);
                if (readLength == -1) break;
                totalReadLength = totalReadLength + readLength; //累计读取字节数

                System.out.println("读取字节数=======》" + readLength);

                //打印bytebuffers的limit和position
                System.out.println("读取");
                Arrays.asList(byteBuffers).stream().map(buffer ->
                        "position => " + buffer.position() + " limit => " + buffer.limit()).forEach(
                        System.out::println
                );

                //翻转全部buffer
                Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());
                System.out.println("翻转");
                Arrays.asList(byteBuffers).stream().map(buffer ->
                        "position => " + buffer.position() + " limit => " + buffer.limit()).forEach(
                        System.out::println);

                //直接把收到的数据，回送到客户端
                long totalWriteLength = 0;
                while (totalWriteLength < contextLength){
                    long writeLength = socketChannel.write(byteBuffers);
                    if (writeLength == 0) break;
                    totalWriteLength = totalWriteLength + writeLength;
                }

                //清除全部buffer
                System.out.println("清除");
                Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());
                Arrays.asList(byteBuffers).stream().map(buffer ->
                        "position => " + buffer.position() + " limit => " + buffer.limit()).forEach(
                        System.out::println);

                System.out.println("Byte read: == >" + totalReadLength);
                System.out.println("Byte write: == >" + totalWriteLength);

                break;
            }
        }
    }
}
