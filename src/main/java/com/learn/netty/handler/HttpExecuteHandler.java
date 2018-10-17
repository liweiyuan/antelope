package com.learn.netty.handler;

import com.learn.netty.action.param.Param;
import com.learn.netty.action.param.ParamMap;
import com.learn.netty.action.req.AntelopeHttpRequest;
import com.learn.netty.action.req.AntelopeRequest;
import com.learn.netty.action.res.AntelopeHttpResponse;
import com.learn.netty.action.res.AntelopeResponse;
import com.learn.netty.config.AppConfig;
import com.learn.netty.enums.StatusType;
import com.learn.netty.exception.AntelopeException;
import com.learn.netty.inteceptor.AntelopeInterceptor;
import com.learn.netty.util.ClassScanner;
import com.learn.netty.util.LoggerBuilder;
import com.learn.netty.util.PathUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author :lwy
 * @Date : 2018/10/15 15:34
 * @Description : http请求处理器
 */
@ChannelHandler.Sharable
public class HttpExecuteHandler extends SimpleChannelInboundHandler<DefaultHttpRequest> {

    private static final Logger logger = LoggerBuilder.getLogger(HttpExecuteHandler.class);

    public static HttpExecuteHandler INSTANCE = new HttpExecuteHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultHttpRequest msg) throws Exception {

        //构造当前请求的拦截器缓存
        List<AntelopeInterceptor> interceptors = new ArrayList<>();

        //1.请求处理
        AntelopeRequest request = AntelopeHttpRequest.initRequest(msg);
        //2.初始化响应体
        AntelopeResponse antelopeResponse = AntelopeHttpResponse.initResponse();

        //TODO 请求上下文

        //4.请求uri
        String uri = request.getUrl();
        //5.构建请求解码器
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
        //6.获取配置的系统参数
        checkAndGet(uri, queryStringDecoder);
        //7.获取action
        Class<?> actionClass = routeAction(queryStringDecoder);
        //8.获取parameter
        Param param = buildParam(queryStringDecoder);
        //9.获取拦截器
        interceptorBefore(interceptors, param);
        //10.执行方法调用

        //写出消息
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        builderHeader(response);
        ctx.writeAndFlush(response);
    }

    /**
     * 获取前置拦截器
     *
     * @param inteceptors
     * @param param
     */
    private void interceptorBefore(List<AntelopeInterceptor> inteceptors, Param param) {
        Map<String, Class<?>> interceptorMap = ClassScanner.getInteceptorMap();
        interceptorMap.forEach((key, clzz) -> {
            try {
                AntelopeInterceptor interceptor = (AntelopeInterceptor) clzz.newInstance();
                interceptor.before(param);
                inteceptors.add(interceptor);
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("illegal interceptor ", e);
            }
        });
    }


    /**
     * 获取Action
     *
     * @param queryStringDecoder
     * @return
     */
    private Class<?> routeAction(QueryStringDecoder queryStringDecoder) {
        String actionPath = PathUtil.getActionPath(queryStringDecoder.path());
        Class<?> actionClass = ClassScanner.getActionClass(actionPath);
        if (actionClass == null) {
            throw new AntelopeException(StatusType.REQUEST_ERROR, "actionClass is not found.");
        }
        return actionClass;
    }

    /**
     * 获取传递的parameter
     *
     * @param queryStringDecoder
     */
    private Param buildParam(QueryStringDecoder queryStringDecoder) {
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        Param param = new ParamMap();
        parameters.forEach((key, values) -> {
            ((ParamMap) param).put(key, values.get(0));
        });
        return param;
    }

    /**
     * 获取系统参数
     *
     * @param uri
     * @param queryStringDecoder
     * @return
     */
    private AppConfig checkAndGet(String uri, QueryStringDecoder queryStringDecoder) {

        AppConfig config = AppConfig.newInstance();
        if (!PathUtil.getRootPath(queryStringDecoder.path()).equals(config.getRootPath())) {
            throw new AntelopeException(StatusType.REQUEST_ERROR, uri);
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
