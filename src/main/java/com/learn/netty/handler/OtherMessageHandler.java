package com.learn.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultHttpRequest;

/**
 * @Author :lwy
 * @Date : 2018/10/17 17:08
 * @Description :
 */
public class OtherMessageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof DefaultHttpRequest)) {
            System.out.println("msg:" + msg);
            ctx.channel().close();
        }
        super.channelRead(ctx, msg);
    }
}
