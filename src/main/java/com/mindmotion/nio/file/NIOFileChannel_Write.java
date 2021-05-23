package com.mindmotion.nio.file;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class NIOFileChannel_Write {
    public static void main(String[] args) throws IOException {
        String data = "hello windvalley";

        //流程关系
        //用户数据 例如一个字符串
        //   |写入到byteBuffer
        //byteBuffer  <-> channel -> fileOutputStream -> file
        //                        <- fileInputStream  <-


        //创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("g:\\web\\tmp\\file01.txt");

        //获取输出流对应的文件channel
        FileChannel fileChannel = fileOutputStream.getChannel();

        //建立一个Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //把定义的字符串数据放入byteBuffer
        byteBuffer.put(data.getBytes(StandardCharsets.UTF_8));

        //翻转bufferByte
        byteBuffer.flip();

        //将byteBuffer写入到通道
        fileChannel.write(byteBuffer);

        //关闭输出流
        fileOutputStream.close();

    }
}
