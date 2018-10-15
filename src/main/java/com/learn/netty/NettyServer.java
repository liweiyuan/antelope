package com.learn.netty;

import com.learn.netty.init.AntelopeInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author :lwy
 * @Date : 2018/10/12 18:30
 * @Description :
 */
public class NettyServer {


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
                //TODO 开启日志
            }
            Channel channel = channelFuture.channel();
            channel.closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
