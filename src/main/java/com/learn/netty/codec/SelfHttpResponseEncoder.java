package com.learn.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.List;

/**
 * @Author :lwy
 * @Date : 2018/10/17 18:04
 * @Description :
 */
public class SelfHttpResponseEncoder extends HttpResponseEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        super.encode(ctx, msg, out);
    }
}
