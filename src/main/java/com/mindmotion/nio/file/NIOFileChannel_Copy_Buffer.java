package com.mindmotion.nio.file;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel_Copy_Buffer {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("g:\\web\\tmp\\file01.txt");

        FileOutputStream fileOutStreamStream = new FileOutputStream("g:\\web\\tmp\\file01.bak");

        FileChannel sourceChannel = fileInputStream.getChannel();

        FileChannel targetChannel = fileOutStreamStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        while (true){
            byteBuffer.clear();
            int readCount = sourceChannel.read(byteBuffer);
            if (readCount == -1){
                break;
            } else {
                byteBuffer.flip();
                targetChannel.write(byteBuffer);
            }
        }

        fileInputStream.close();

        fileOutStreamStream.close();
    }
}
