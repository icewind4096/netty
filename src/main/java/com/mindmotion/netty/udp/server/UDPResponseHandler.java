package com.mindmotion.netty.udp.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

public class UDPResponseHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket datagramPacket) throws Exception {
        int dataLength = datagramPacket.content().readableBytes();
        byte[] data = new byte[dataLength];
        datagramPacket.content().readBytes(data);

        String context = bytesToHexString(data);

        System.out.println("服务端接收到地址@" + datagramPacket.sender().getAddress() + ":" + datagramPacket.sender().getPort() + " 数据为:" + context + " 字符串:" + new String(data, StandardCharsets.UTF_8));

        context = new String(data, StandardCharsets.UTF_8);
        ByteBuf byteBuf = Unpooled.copiedBuffer(context.getBytes(CharsetUtil.UTF_8));

        ctx.writeAndFlush(new DatagramPacket(byteBuf, datagramPacket.sender()));
    }

    private String bytesToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
}
