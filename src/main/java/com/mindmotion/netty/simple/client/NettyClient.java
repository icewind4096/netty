package com.mindmotion.netty.simple.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端只需要一个事件循环组就可以了
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //创建启动对象, 客户端使用 bootstrap  和 服务器端的启动对象不一样,切记!!!
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)       //使用NioSocketChannel类作为客户端通道实现
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyClientHandler_1());
                        }
                    });

            System.out.println("客户端 is ready...................");

            //绑定端口并启动客户端连接服务器
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9999).sync();

            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }
        finally {
            workGroup.shutdownGracefully();
        }
    }
}
