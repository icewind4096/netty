package com.mindmotion.netty.simple.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 1.定义自定义 处理器(Handler) 时，需要继承Netty规定好的某一个 处理器适配器(HanderApapter), 此时自定义的Handler才可以成为一个Handler
 * 2.如果需要进行一个耗时的操作，可以使用异步执行，提交改channel到对应的NIOEventLoop的taskqueue中实现
 * 3.NIOEventLoop里面的taskqueue，是顺序执行
 * 4.taskequeue的优先级高于ScheeuleTaskQueue
 */
public class NettyServerHandler_1 extends ChannelInboundHandlerAdapter {

    //捕获发送的异常，需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        String msg = "你好，客户端@" + ctx.channel().remoteAddress();
        ctx.writeAndFlush(Unpooled.copiedBuffer(msg.getBytes(StandardCharsets.UTF_8)));
    }

    //这里读取客户端发送的的数据
    //不可以在此处调用父类的默认处理方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Server->: 客户端地址 " + ctx.channel().remoteAddress());
        System.out.println("Server->: 服务器线程信息 " + "线程名称 " + Thread.currentThread().getName());

//      解决耗时操作的问题,使用异步操作  三种实现方式
//      1. 用户自定义普通任务 任务被放置到TaskQueue中
        ctx.channel().eventLoop().execute(new UserDefineTask_1(ctx, 10));
        ctx.channel().eventLoop().execute(new UserDefineTask_1(ctx, 20));

//      2. 用户自定义定时任务 任务被放置到ScheeuleTaskQueue中，此方法是延时10秒以后执行，只持续一次!!!
        ctx.channel().eventLoop().schedule(new UserDefineScheduleTask_1(ctx, "一次定时"), 1, TimeUnit.SECONDS);

//      3. 用户自定义定时任务 任务被放置到ScheeuleTaskQueue中，此方法是延时15秒以后执行，每次延时10秒，重复执行!!!
        ctx.channel().eventLoop().scheduleAtFixedRate(new UserDefineScheduleTask_1(ctx, "重复定时"), 5, 10, TimeUnit.SECONDS);

//        正常操作, 显示当前读取到的数据===================================================
//        //将msg转为Netty.ByteBuf
//        ByteBuf byteBuf = (ByteBuf) msg;
//        System.out.println("Server->: [客户端发送数据] " + byteBuf.toString(CharsetUtil.UTF_8));
    }

}

class UserDefineTask_1 implements Runnable{
    private ChannelHandlerContext channelHandlerContext;
    private int delayTime;

    UserDefineTask_1(ChannelHandlerContext channelHandlerContext, int delayTime){
        this.channelHandlerContext = channelHandlerContext;
        this.delayTime = delayTime;
    }

    @Override
    public void run() {
        String text = "你好，客户端@" + channelHandlerContext.channel().remoteAddress() + "准备任务一Delay " + delayTime + " 秒";
        System.out.println(text);
        try {
            Thread.sleep(delayTime * 1000);
            channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(text.getBytes(StandardCharsets.UTF_8)));
            System.out.println("Server-> 任务一退出延时" + delayTime + "秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class UserDefineScheduleTask_1 implements Runnable{
    private String message;
    private ChannelHandlerContext channelHandlerContext;

    UserDefineScheduleTask_1(ChannelHandlerContext channelHandlerContext, String message){
        this.channelHandlerContext = channelHandlerContext;
        this.message = message;
    }

    @Override
    public void run() {
        String text = "你好，客户端@" + channelHandlerContext.channel().remoteAddress() + "准备" + message + " 时间在: " + new Date();
        System.out.println(text);
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(text.getBytes(StandardCharsets.UTF_8)));
        System.out.println("Server-> 定时任务退出");
    }
}