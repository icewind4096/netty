package com.mindmotion.nio.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel_Read {
    public static void main(String[] args) throws IOException {
        //建立输出流
        FileInputStream fileInputStream = new FileInputStream("g:\\web\\tmp\\file01.txt");

        //建立通道
        FileChannel fileChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        fileChannel.read(byteBuffer);

        byteBuffer.flip();

        String data = new String(byteBuffer.array());

        System.out.println(data);

        fileInputStream.close();
    }
}
