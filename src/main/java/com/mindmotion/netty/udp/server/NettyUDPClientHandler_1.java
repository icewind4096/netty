package com.mindmotion.netty.udp.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class NettyUDPClientHandler_1 extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        int dataLength = packet.content().readableBytes();
        byte[] data = new byte[dataLength];
        packet.content().readBytes(data);

        System.out.println("client>>>" + new String(data, CharsetUtil.UTF_8));
    }
}
