package com.learn.netty.configuration;

import com.learn.netty.constant.AntelopeConstant;

/**
 * @Author :lwy
 * @Date : 2018/10/16 18:25
 * @Description :
 */
public class ApplicationConfiguration extends AbstractAntelopeConfiguration {

    public ApplicationConfiguration() {
        super.setPropertiesName(AntelopeConstant.SystemProperties.APPLICATION_PROPERTIES);
    }
}
