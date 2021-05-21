package com.mindmotion.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        //创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        //建立ServerSocket
        ServerSocket serverSocket = new ServerSocket(9999);

        System.out.println("服务器启动，端口9999");

        while(true){
            //监听，等待客户端连接
            System.out.println("服务器等待客户端连接");
            final Socket socket = serverSocket.accept();
            System.out.println("服务器已连接一个客户端");

            //创建一个线程与客户端通讯
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //此处与客户端通讯
                    handler(socket);
                }
            });
        }
    }

    //handle方法与客户端通讯
    public static void handler(Socket socket){
        try {
            System.out.println("Enter Handle ===> 线程ID:=>" + Thread.currentThread().getId() + " 线程名称:=>" + Thread.currentThread().getName());
            InputStream inputStream = socket.getInputStream();

            byte[] bytes = new byte[4096];

            while (true){
                System.out.println("等待读取 线程ID:=>" + Thread.currentThread().getId() + " 线程名称:=>" + Thread.currentThread().getName());
                int count = inputStream.read(bytes);
                System.out.println("读取到数据 线程ID:=>" + Thread.currentThread().getId() + " 线程名称:=>" + Thread.currentThread().getName());
                if (count != -1){
                    System.out.println(new String(bytes, 0, count));
                } else {
                    System.out.println("数据读取完毕");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("准备关闭与客户端的连接");
            if (socket != null){
                try {
                    socket.close();
                    System.out.println("关闭与客户端的连接");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
