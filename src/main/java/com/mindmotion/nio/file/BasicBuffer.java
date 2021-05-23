package com.mindmotion.nio.file;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        //创建Buffer，大小为10，可以存放10个int
        IntBuffer intBuffer = IntBuffer.allocate(10);

        //向buffer中存放数据
        intBuffer.put(11);
        intBuffer.put(12);
        intBuffer.put(13);
        intBuffer.put(14);
        intBuffer.put(15);

        intBuffer.put(16);

        //切换读写模式
        intBuffer.flip();

//        intBuffer.position(2);
//        intBuffer.limit(4);

        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }

    }
}
