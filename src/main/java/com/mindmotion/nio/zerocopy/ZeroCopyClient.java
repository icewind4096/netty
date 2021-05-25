package com.mindmotion.nio.zerocopy;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class ZeroCopyClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9999);

        socketChannel.configureBlocking(false);

        if (!socketChannel.connect(inetSocketAddress)){     //异步操作，此处不会被阻塞，直接运行到到下面的while循环中
            while (!socketChannel.finishConnect()){
                System.out.println("连接需要时间，等待服务器响应");
            }
        }

        String fileName = "c:\\tmp\\fastboot.exe";

        FileInputStream fileInputStream = new FileInputStream(fileName);

        FileChannel fileChannel = fileInputStream.getChannel();

        long index = 0;
        long count = 1024 * 1024;
        while (true){
            long sendCount = fileChannel.transferTo(index, count, socketChannel);
            if (sendCount > 0){
                index = index + sendCount;
            } else {
                break;
            }
        }

        System.out.println("发送总字节数：" + index);

        fileChannel.close();

        socketChannel.close();
    }
}
