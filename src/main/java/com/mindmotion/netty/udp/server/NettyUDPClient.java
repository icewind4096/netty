package com.mindmotion.netty.udp.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

/**
 * 客户端只需要一个事件循环组就可以了
 */
public class NettyUDPClient {
    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //创建启动对象, 客户端使用 bootstrap  和 服务器端的启动对象不一样,切记!!!
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(workGroup)
                    .channel(NioDatagramChannel.class)
                    .handler(new NettyUDPClientHandler_1());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            Channel ch = bootstrap.bind(0).sync().channel();
            while (true){
                String line = bufferedReader.readLine();
                if (line == null) continue;
                if ("exit".equals(line)){break;}
                ch.writeAndFlush(new DatagramPacket(
                        Unpooled.copiedBuffer(line, CharsetUtil.UTF_8),
                        new InetSocketAddress("127.0.0.1", 9999))).sync();
            }
            System.exit(0);
        }
        finally {
            workGroup.shutdownGracefully();
        }
    }
}