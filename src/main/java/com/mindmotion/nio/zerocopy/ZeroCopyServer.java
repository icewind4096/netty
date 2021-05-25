package com.mindmotion.nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ZeroCopyServer {
    public static void main(String[] args) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        ServerSocket serverSocket = serverSocketChannel.socket();

        serverSocket.bind(inetSocketAddress);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);

        int count = 0;
        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();

            while (true){
                int length = 0;
                try {
                    length = socketChannel.read(byteBuffer);
                    if (length == -1) break;
                    byteBuffer.rewind();
                    count = count + length;
                    System.out.println("接受到字节数: " + count);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
