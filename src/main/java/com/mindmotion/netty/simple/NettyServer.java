package com.mindmotion.netty.simple;

import com.mindmotion.netty.simple.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
    //创建2个线程组， BossGroup和WorkGroup 都是无限循环, bossGroup和workerGroup含有的子线程的个数默认为cpu核心 * 2
        //创建Bossgroup 只处理连接请求 此处只在BoosGroup中分配1个线程
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        //创建workergroup 与客户端进行业务处理， 此处只在WorkerGroup中分配默认线程数，默认值维内核 * 2
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务器端启动对象,配置启动参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)                           //设置连个线程组
                    .channel(NioServerSocketChannel.class)                   //使用NIOServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)             //设置线程队列等待连接数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)     //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {  //创建通道初始化对象(匿名对象)
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //此处添加一个处理器
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });                                                      //给workgroup的NioEventLoop对应的管道配置处理器
            System.out.println("服务器就绪");

            //绑定一个端口，并同步处理
            ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();

            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
