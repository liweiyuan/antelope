package com.learn.netty.action;

import com.learn.netty.action.param.Param;
import com.learn.netty.context.AntelopeContext;

/**
 * @Author :lwy
 * @Date : 2018/10/17 15:59
 * @Description :
 */
public interface WorkAction {


    void execute(AntelopeContext context, Param param) throws Exception;
}
