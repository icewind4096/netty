package com.mindmotion.nio.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NIOClient {
    public static void main(String[] args) throws IOException {
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();

        //设置为非阻塞模式
        socketChannel.configureBlocking(false);

        //提供服务器端的IP地址和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9999);

        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)){     //异步操作，此处不会被阻塞，直接运行到到下面的while循环中
            while (!socketChannel.finishConnect()){
                System.out.println("连接需要时间，等待服务器响应");
            }
        }

        String content = "你好，世界";
        //ByteBuffer 使用wrap方法，会自动产生一个和数据大小一样的字节buffer， 等效于ByteBuffer.allocate,然后再put数据到bytebuffer中去
        ByteBuffer byteBuffer = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));

        //发送数据
        socketChannel.write(byteBuffer);

        //只是为了阻塞到这里看效果,没意义
        while (true){

        }
    }
}
