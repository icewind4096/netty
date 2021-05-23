package com.mindmotion.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class ChatServer {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    final private static int port = 6667;

    public ChatServer() {
        try {
            //建立一个selector
            selector = Selector.open();

            //建立一个serversocketchannel
            serverSocketChannel = ServerSocketChannel.open();

            //绑定到9999端口
            serverSocketChannel.socket().bind(new InetSocketAddress(9999));

            //设置为非阻塞方式
            serverSocketChannel.configureBlocking(false);

            //注册到selector
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){
        System.out.println("[服务器] -> 等待客户端连接");
        while (true){
            try {
                if (selector.selectNow() > 0){
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator selectionKeyIterator = selectionKeys.iterator();
                    while (selectionKeyIterator.hasNext()){
                        SelectionKey selectionKey = (SelectionKey) selectionKeyIterator.next();

                        //通道监听到accept事件
                        if (selectionKey.isAcceptable()){
                            handleAccept(serverSocketChannel, selector);
                        }

                        //通道监听到数据到来，可以读取
                        if (selectionKey.isReadable()){
                            handleRead(selector, selectionKey);
                        }

                        if (selectionKey.isWritable()){

                        }

                        selectionKeyIterator.remove();
                    }
                } else {
                    //System.out.println("等待...");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    private void handleRead(Selector selector, SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) selectionKey.channel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(2048);

//            ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();

            int readLen = socketChannel.read(byteBuffer);

            if (readLen > 0){
                String message = new String(byteBuffer.array());

                System.out.println("客户 " + socketChannel.getRemoteAddress() + "发言: ====>" + message);

                translatMessage2OtherClient(selector, message, selectionKey.hashCode());
            }

            byteBuffer.clear();
        } catch (IOException e) {
            try {
                System.out.println("客户 " + socketChannel.getRemoteAddress() + "已经离线");
                //取消注册
                selectionKey.cancel();
                //关闭sockchannel;
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private void translatMessage2OtherClient(Selector selector, String message, int nowClientCode) throws IOException {
        System.out.println("服务器开始转发消息");

        for (SelectionKey selectionKey : selector.keys()){
            if ((selectionKey.hashCode() != nowClientCode) && !(selectionKey.channel() instanceof ServerSocketChannel)){
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
                socketChannel.write(byteBuffer);
            }
        }
    }

    private void handleAccept(ServerSocketChannel serverSocketChannel, Selector selector) {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();

            //必须为非阻塞
            socketChannel.configureBlocking(false);

            socketChannel.register(selector, SelectionKey.OP_READ);

            System.out.println("连接一个新客户: IP:===>" + socketChannel.getRemoteAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer();
        chatServer.listen();
    }
}
