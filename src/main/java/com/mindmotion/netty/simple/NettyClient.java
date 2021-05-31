package com.mindmotion.netty.simple;

import com.mindmotion.netty.simple.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        //客户端只需要一个事件循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            //创建客户端启动对象,客户端使用Bootstrap而不是SerBootStrap,
            Bootstrap bootstrap = new Bootstrap();

            //设置参数
            bootstrap.group(eventLoopGroup)                     //设置线程组
                    .channel(NioSocketChannel.class)            //设置客户端通道实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //此处加入自己的处理器
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            System.out.println("客户端就绪");

            //启动客户端连接服务器
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9999).sync();

            //给通道添加一个关闭监听
            channelFuture.channel().closeFuture().sync();
        }
        finally {
            eventLoopGroup.shutdownGracefully();
        }

    }
}
