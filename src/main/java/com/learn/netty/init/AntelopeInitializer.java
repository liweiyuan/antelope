package com.learn.netty.init;

import com.learn.netty.codec.SelfHttpResponseEncoder;
import com.learn.netty.handler.OtherMessageHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import static com.learn.netty.handler.HttpExecuteHandler.INSTANCE;

/**
 * @Author :lwy
 * @Date : 2018/10/12 18:43
 * @Description :
 */
public class AntelopeInitializer extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new HttpRequestDecoder())
                .addLast(new SelfHttpResponseEncoder())
                .addLast(INSTANCE)
                .addLast("logger", new LoggingHandler(LogLevel.INFO))
                /*.addLast(new OtherMessageHandler())*/;
    }
}
