package com.learn.netty.handler;

import com.learn.netty.action.WorkAction;
import com.learn.netty.action.param.Param;
import com.learn.netty.action.param.ParamMap;
import com.learn.netty.action.req.AntelopeHttpRequest;
import com.learn.netty.action.req.AntelopeRequest;
import com.learn.netty.action.res.AntelopeHttpResponse;
import com.learn.netty.action.res.AntelopeResponse;
import com.learn.netty.config.AppConfig;
import com.learn.netty.constant.AntelopeConstant;
import com.learn.netty.context.AntelopeContext;
import com.learn.netty.enums.StatusType;
import com.learn.netty.exception.AntelopeException;
import com.learn.netty.inteceptor.AbstractAntelopeInterceptorAdapter;
import com.learn.netty.inteceptor.OrderComparator;
import com.learn.netty.util.ClassScanner;
import com.learn.netty.util.LoggerBuilder;
import com.learn.netty.util.PathUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.*;

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
        List<AbstractAntelopeInterceptorAdapter> interceptors = new ArrayList<>();

        //1.请求处理
        AntelopeRequest request = AntelopeHttpRequest.initRequest(msg);

        //1.1过滤请求
        boolean filterRequest = filterRequest(request, ctx);

        if (filterRequest) {
            //2.初始化响应体
            AntelopeResponse antelopeResponse = AntelopeHttpResponse.initResponse();

            try {
                //3.请求上下文
                AntelopeContext.setContext(new AntelopeContext(request, antelopeResponse));
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

                WorkAction action = (WorkAction) actionClass.newInstance();
                action.execute(AntelopeContext.getContext(), param);
                //11.执行后置拦截器
                interceptorAfter(interceptors, param);
            } finally {
                if (AntelopeContext.getResponse().getHttpContent() != null) {
                    responseContent(ctx, AntelopeContext.getResponse().getHttpContent());
                    AntelopeContext.removeContext();
                }
            }
        }


    }

    /**
     * 请求过滤
     *
     * @param request
     * @param ctx
     */
    private boolean filterRequest(AntelopeRequest request, ChannelHandlerContext ctx) {
        if (request.getUrl().equals("/favicon.ico")) {
            return false;
        }
        return true;
    }

    /**
     * 构建响应输出
     *
     * @param ctx
     * @param httpContent
     */
    private void responseContent(ChannelHandlerContext ctx, String httpContent) {
        ByteBuf buf = Unpooled.wrappedBuffer(httpContent.getBytes(StandardCharsets.UTF_8));
        //写出消息
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        builderHeader(response);
        ctx.writeAndFlush(response);
    }

    private void interceptorAfter(List<AbstractAntelopeInterceptorAdapter> interceptors, Param param) {
        Map<HashMap<String, Integer>, Class<?>> interceptorMap = ClassScanner.getInterceptorMap();
        interceptorMap.forEach((key, clzz) -> {
            try {
                AbstractAntelopeInterceptorAdapter interceptor = (AbstractAntelopeInterceptorAdapter) clzz.newInstance();
                interceptor.after(param);
                interceptors.add(interceptor);
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("illegal interceptor ", e);
            }
        });
    }

    /**
     * 获取前置拦截器
     *
     * @param interceptors
     * @param param
     */
    private void interceptorBefore(List<AbstractAntelopeInterceptorAdapter> interceptors, Param param) {
        Map<HashMap<String, Integer>, Class<?>> interceptorMap = ClassScanner.getInterceptorMap();


        interceptorMap.forEach((key, clzz) -> {
            Iterator<Map.Entry<String, Integer>> iterator = key.entrySet().iterator();
            int order = 0;
            if (iterator.hasNext()) {
                order = iterator.next().getValue();
            }
            try {
                AbstractAntelopeInterceptorAdapter antelopeInterceptor = (AbstractAntelopeInterceptorAdapter) clzz.newInstance();
                antelopeInterceptor.before(param);
                antelopeInterceptor.setOrder(order);
                interceptors.add(antelopeInterceptor);
                //排序
                interceptors.sort(new OrderComparator());
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
        headers.set(HttpHeaderNames.CONTENT_TYPE, AntelopeConstant.ContentType.JSON);
    }
}
