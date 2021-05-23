package com.mindmotion.nio.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * 可以让文件直接在内存(堆外内存)中修改,操作系统不需要拷贝一次
 */
public class NIO_MapBuffer {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("g:\\web\\tmp\\file01.txt", "rw");

        FileChannel fileChannel = randomAccessFile.getChannel();

        //把文件做一个映射，模式为读写，开始地址为0， 长度为5， 也就是0-4字节映射到内存中修改
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'L');
        mappedByteBuffer.put(3, (byte) '9');

        fileChannel.close();

        randomAccessFile.close();

        randomAccessFile = new RandomAccessFile("g:\\web\\tmp\\file01.txt", "rw");
        randomAccessFile.seek(0);
        randomAccessFile.write("我爱北京天安门，天安门上红旗飘".getBytes(StandardCharsets.UTF_8));
        randomAccessFile.close();
    }
}
