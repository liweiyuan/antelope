package com.learn.netty;

import com.learn.netty.init.AntelopeInitializer;
import com.learn.netty.util.LoggerBuilder;
import com.learn.netty.util.ThreadLocalHolder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;

/**
 * @Author :lwy
 * @Date : 2018/10/12 18:30
 * @Description :
 */
public class NettyServer {

    private static final Logger logger = LoggerBuilder.getLogger(NettyServer.class);


    private static NioEventLoopGroup boss = new NioEventLoopGroup();
    private static NioEventLoopGroup worker = new NioEventLoopGroup();

    /**
     * nettyServer启动
     */

    public static void start() throws InterruptedException {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new AntelopeInitializer());

            ChannelFuture channelFuture = bootstrap.bind(8080).sync();
            if (channelFuture.isSuccess()) {
                appLog();
            }
            Channel channel = channelFuture.channel();
            channel.closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    private static void appLog() {
        long startTime = ThreadLocalHolder.getLocalTime();
        long end = System.currentTimeMillis();
        logger.info("Cicada started cost {}ms", end - startTime);
    }
}
