package com.learn.netty;

import com.learn.AntelopeServer;
import com.learn.netty.action.WorkAction;
import com.learn.netty.action.param.Param;
import com.learn.netty.annotation.AntelopeAction;
import com.learn.netty.context.AntelopeContext;

/**
 * @Author :lwy
 * @Date : 2018/10/15 15:37
 * @Description :
 */
@AntelopeAction(value = "hello")
public class AntelopeServerTestCase implements WorkAction {


    public static void main(String[] args) throws InterruptedException {

        AntelopeServer.start(AntelopeServerTestCase.class,"/");
    }

    @Override
    public void execute(AntelopeContext context, Param param) throws Exception {
        System.err.println("execute....");
        String method=context.getRequest().getMethod();
        String url=context.getRequest().getUrl();

        //json格式输出
        context.json(method+url);
        //text格式输出
        //context.text(method+url);

        //html格式输出
        //context.html(method+url);
    }
}
