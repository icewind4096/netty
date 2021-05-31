package com.mindmotion.netty.simple.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 1.创建bossGroup 处理连接请求 无限循环
 * 2.创建WorkGroup 处理I/O读写 真正和客户端交互处理 无限循环
 * 3.创建服务器端启动对象
 * 4.配置启动参数
 *
 *
 * 每个NioEventLoop实例都持有一个线程，一个类型为LinkedBlockingQueue的任务队列、和一个selector，并对selector进行优化。
 * NioEventLoop线程命名规则：nioEventLoop-x(第几个NioEventLoopGroup)-xx(第几个NioEventLoop)
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //创建BossGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        //创建WorkGroup
        EventLoopGroup workGroup = new NioEventLoopGroup(2);

        try {
            //创建启动对象 服务器端使用 ServerBootstrap 和 客户端的启动对象不一样,切记!!!
            ServerBootstrap bootstrap = new ServerBootstrap();

            //配置启动参数
            bootstrap.group(bossGroup, workGroup)                              //设置2个线程组
                    .channel(NioServerSocketChannel.class)                     //使用NioServerSocketChannel类作为服务器通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)               //设置线程得到的连接的个数, 服务端处理客户端连接请求是顺序处理的，
                    //所以同一时间只能处理一个客户端连接，多个客户端来的时候，
                    //服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
                    //!!!重点:对程序的连接数没影响，但是影响的是还没有被accept取出的连接
                    .childHandler(new ChannelInitializer<SocketChannel>() {    //创建一个通道初始化对象
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception { //向workgroup中的的Eventloop中，对应的pipeline中添加各种处理器(Handler)
                            socketChannel.pipeline().addLast(new NettyServerHandler_1());
                        }
                    });
            System.out.println("服务器 is ready...................");

            //绑定端口并启动服务器
            ChannelFuture channelFuture = bootstrap.bind(9999).sync();

            //注册监听器，监控关心的事件
            //GenericFutureListener<? extends Future<? super Void>> listener
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()){
                        System.out.println("服务器: 绑定端口成功");
                    } else {
                        System.out.println("服务器: 绑定端口失败");

                    }
                }
            });

            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }
        finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
