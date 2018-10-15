package com.learn.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

/**
 * @Author :lwy
 * @Date : 2018/10/15 15:34
 * @Description :
 */
public class HttpExecuteHandler extends SimpleChannelInboundHandler<DefaultHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultHttpRequest msg) throws Exception {
        System.out.println(msg);

        //写出消息
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        builderHeader(response);
        ctx.writeAndFlush(response);
    }

    /**
     * 构建header
     * @param response
     */
    private void builderHeader(DefaultFullHttpResponse response) {
        HttpHeaders headers = response.headers();
        headers.setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        headers.set(HttpHeaderNames.CONTENT_TYPE, "application/json");
    }
}
