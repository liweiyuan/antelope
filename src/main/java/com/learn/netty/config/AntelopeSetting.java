package com.learn.netty.config;

import com.learn.AntelopeServer;
import com.learn.netty.configuration.AbstractAntelopeConfiguration;
import com.learn.netty.configuration.ApplicationConfiguration;
import com.learn.netty.configuration.ConfigurationHolder;
import com.learn.netty.constant.AntelopeConstant;
import com.learn.netty.util.ClassScanner;
import com.learn.netty.util.LoggerBuilder;
import com.learn.netty.util.ThreadLocalHolder;
import org.slf4j.Logger;

import java.io.*;
import java.util.List;
import java.util.Properties;

import static com.learn.netty.configuration.ConfigurationHolder.getConfiguration;

/**
 * @Author :lwy
 * @Date : 2018/10/15 16:05
 * @Description :
 */
public class AntelopeSetting {

    private static final Logger logger = LoggerBuilder.getLogger(AntelopeSetting.class);

    /**
     * 资源初始化
     *
     * @param clzz
     * @param scanRootPath
     */
    public static void setting(Class<?> clzz, String scanRootPath) {
        initializeConfig(clzz);
        setAppConfig(scanRootPath);
    }

    private static void setAppConfig(String scanRootPath) {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfiguration(ApplicationConfiguration.class);

        if (scanRootPath == null) {
            scanRootPath = applicationConfiguration.get(AntelopeConstant.ROOT_PATH);
        }
        String port = applicationConfiguration.get(AntelopeConstant.ANTELOPE_PORT);

        if (scanRootPath == null) {
            throw new RuntimeException("No [cicada.root.path] exists ");
        }
        if (port == null) {
            throw new RuntimeException("No [cicada.port] exists ");
        }
        AppConfig.newInstance().setRootPath(scanRootPath);
        AppConfig.newInstance().setPort(Integer.parseInt(port));
    }

    /**
     * 初始化系统参数
     *
     * @param clzz
     */
    private static void initializeConfig(Class<?> clzz) {
        //初始化开始时间
        ThreadLocalHolder.setLocalTime(System.currentTimeMillis());
        //设置扫描包路径
        AppConfig.newInstance().setRootPackageName(clzz.getPackage().getName());
        //扫描Class
        List<Class<?>> configurations = ClassScanner.configurations(AppConfig.newInstance().getRootPackageName());
        //初始化基于注解的action与拦截器处理
        ClassScanner.initAnnotation();
        configurations.forEach(configurationClass -> {
            try {
                AbstractAntelopeConfiguration configuration = (AbstractAntelopeConfiguration) configurationClass.newInstance();
                //read
                InputStream stream;

                String systemProperty = System.getProperty(configuration.getPropertiesName());
                if (systemProperty != null) {
                    stream = new FileInputStream(new File(systemProperty));
                } else {
                    stream = AntelopeServer.class.getClassLoader().getResourceAsStream(configuration.getPropertiesName());
                }
                Properties properties=new Properties();
                properties.load(stream);

                configuration.setProperties(properties);

                ConfigurationHolder.addConfiguration(configurationClass.getName(),configuration);

            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("create new class failed.", e);
            } catch (FileNotFoundException e) {
                logger.error("FileNotFoundException", e);
            } catch (IOException e) {
                logger.error("IOException", e);
            }
        });
    }
}
