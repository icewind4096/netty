package com.mindmotion.nio.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        //创建ServerSockChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个selecor对象
        Selector selector = Selector.open();

        //服务器端绑定端口，进行监听
        InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
        serverSocketChannel.socket().bind(inetSocketAddress);

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel注册到selector,并且只关系事件 OP_Accept
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true){
            //个人理解是selector使用selectNow方法,监听所有的sockchannel
            if (selector.selectNow() == 0) {    //通道上没有事件发生,此处使用的是异步立即返回
                System.out.println("服务器查询，没有发现有客户端连接");
                continue;
            }

            //1. 此时有注册时关注的事件发生
            //2. 返回一个注册时关注的事件发生的通道列表
            //3. 通过selectionkey可以反向得到socketChannel通道

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){
                SelectionKey selectionKey = keyIterator.next();

            //获取到selectionKey对应的通道发生的事件是什么,进行对应的处理
                //此处判断，是否是一个新的客户端连接事件
                if (selectionKey.isAcceptable()){
                    //给改客户端生成一个对应的socketchannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功,产生了一个socketchannel HashCode:====>" + socketChannel.hashCode());

                    //把新产生的sockchannel注册到selector中, 并且给这个sockchannel关联一个buffer
                    ByteBuffer byteBuffer = ByteBuffer.allocate(2048);

                    //指定socketChannel必须为一个非阻塞的模式,否则在注册的时候会产生异常
                    socketChannel.configureBlocking(false);

                    //注册sockchannel到selector中
                    socketChannel.register(selector, SelectionKey.OP_READ, byteBuffer);
                }

                //此处判断，是否是一个通道读取事件
                if (selectionKey.isReadable()){
                    //根据selectionKey，获取到sockchannel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    //获取该sockchannel关联的buffer,这个buffer是在注册到selector时分配的
                    //使用attachment方法，强制转换为注册时绑定的bytebuffer
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();

                    //把当前socketchannel里面的数据，读入到bytebuffer中去
                    socketChannel.read(byteBuffer);
                    System.out.println("客户端发送的数据:" + new String(byteBuffer.array()));
                }

                //手动移除集合中的selectionKey
                keyIterator.remove();
            }
        }
    }
}
