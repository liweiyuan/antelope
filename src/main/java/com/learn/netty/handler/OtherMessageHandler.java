package com.learn.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.LastHttpContent;

/**
 * @Author :lwy
 * @Date : 2018/10/17 17:08
 * @Description :
 */
public class OtherMessageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof LastHttpContent){
            System.err.println(msg);
        }
        //System.out.println(msg);
        super.channelRead(ctx, msg);
    }
}
