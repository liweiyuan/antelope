package com.learn.netty.handler;

import com.learn.netty.action.req.AntelopeHttpRequest;
import com.learn.netty.action.req.AntelopeRequest;
import com.learn.netty.action.res.AntelopeHttpResponse;
import com.learn.netty.action.res.AntelopeResponse;
import com.learn.netty.config.AppConfig;
import com.learn.netty.enums.StatusType;
import com.learn.netty.exception.AntelopeException;
import com.learn.netty.util.PathUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

/**
 * @Author :lwy
 * @Date : 2018/10/15 15:34
 * @Description :
 */
@ChannelHandler.Sharable
public class HttpExecuteHandler extends SimpleChannelInboundHandler<DefaultHttpRequest> {

    public static HttpExecuteHandler INSTANCE = new HttpExecuteHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultHttpRequest msg) throws Exception {


        //1.请求处理
        AntelopeRequest request = AntelopeHttpRequest.initRequest(msg);
        //2.初始化响应体
        AntelopeResponse antelopeResponse = AntelopeHttpResponse.initResponse();

        //TODO 请求上下文

        //4.请求uri
        String uri = request.getUrl();
        //5.构建请求解码器
        QueryStringDecoder queryStringDecoder=new QueryStringDecoder(uri);
        //6.获取配置的系统参数
        AppConfig config=checkAndget(uri,queryStringDecoder);

        //写出消息
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        builderHeader(response);
        ctx.writeAndFlush(response);
    }

    /**
     * 获取系统参数
     * @param uri
     * @param queryStringDecoder
     * @return
     */
    private AppConfig checkAndget(String uri, QueryStringDecoder queryStringDecoder) {

        AppConfig config=AppConfig.newInstance();
        if(!PathUtil.getRootPath(queryStringDecoder.path()).equals(config.getRootPath())){
            throw new AntelopeException(StatusType.REQUEST_ERROR,uri);
        }
        return config;
    }

    /**
     * 构建header
     *
     * @param response
     */
    private void builderHeader(DefaultFullHttpResponse response) {
        HttpHeaders headers = response.headers();
        headers.setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        headers.set(HttpHeaderNames.CONTENT_TYPE, "application/json");
    }
}
