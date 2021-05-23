package com.mindmotion.nio.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class ChatClient {
    final private static String host = "127.0.0.1";
    final private static int port = 9999;

    private SocketChannel socketChannel;

    private Selector selector;

    private String userName;

    public ChatClient() throws IOException {
        selector = Selector.open();

        //提供服务器端的IP地址和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);

        //得到一个网络通道
        socketChannel = SocketChannel.open(inetSocketAddress);

        //设置为非阻塞模式
        socketChannel.configureBlocking(false);

        userName = socketChannel.getLocalAddress().toString();

        socketChannel.register(selector, SelectionKey.OP_READ);

        System.out.println("用户地址 " + socketChannel.getLocalAddress());
    }

    public void receiveMessage(){
        try {
            int affectChannelCount = selector.selectNow();
            if (affectChannelCount > 0){
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator iterator = selectionKeySet.iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = (SelectionKey) iterator.next();
                    if (selectionKey.isReadable()){
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
                        socketChannel.read(byteBuffer);
                        String message = new String(byteBuffer.array());
                        System.out.println(message);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        message = userName + " 说： ====》" + message;

        ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));

        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            System.out.println("服务器不存在");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient();

        ChatClientRunable chatClientRunable = new ChatClientRunable(chatClient);

        Thread thread = new Thread(chatClientRunable);
        thread.start();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            String line = bufferedReader.readLine();
            if (line == null) continue;
            if ("exit".equals(line)){
                chatClientRunable.stopWork();
            } else {
                chatClient.sendMessage(line);
            }
        }
    }
}

class ChatClientRunable implements Runnable{
    private ChatClient chatClient;
    public volatile boolean cancelFlag = false;

    public void stopWork(){
        cancelFlag = true;
    }

    public ChatClientRunable(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void run() {
        while (true){
            if (cancelFlag == false){
                chatClient.receiveMessage();
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                break;
            }
        }
    }
}
