package com.learn.netty.inteceptor;

import java.util.Comparator;

/**
 * @Author :lwy
 * @Date : 2018/11/6 18:09
 * @Description :
 */

public class OrderComparator implements Comparator<AbstractAntelopeInterceptorAdapter> {
    @Override
    public int compare(AbstractAntelopeInterceptorAdapter o1, AbstractAntelopeInterceptorAdapter o2) {
        if(o1.getOrder()>o2.getOrder()){
            return 1;
        }
        return 0;
    }

}
