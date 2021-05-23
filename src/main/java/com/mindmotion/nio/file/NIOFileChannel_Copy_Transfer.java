package com.mindmotion.nio.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class NIOFileChannel_Copy_Transfer {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("g:\\web\\tmp\\file01.txt");

        FileOutputStream fileOutStreamStream = new FileOutputStream("g:\\web\\tmp\\file01.bakT");

        FileChannel sourceChannel = fileInputStream.getChannel();

        FileChannel targetChannel = fileOutStreamStream.getChannel();

        //都可以，搞清楚源和目标
        targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
//        sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);

        sourceChannel.close();
        fileInputStream.close();

        targetChannel.close();
        fileOutStreamStream.close();

//        sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
    }
}
