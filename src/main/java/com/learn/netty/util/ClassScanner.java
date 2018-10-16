package com.learn.netty.util;

import com.learn.netty.annotation.AntelopeAction;
import com.learn.netty.annotation.AntelopeInterceptor;
import com.learn.netty.configuration.AbstractAntelopeConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author :lwy
 * @Date : 2018/10/16 16:01
 * @Description : Class扫描类
 */
public class ClassScanner {

    private static final Logger logger = LoggerBuilder.getLogger(ClassScanner.class);

    //存储所有的class
    private static Set<Class<?>> classes = null;

    private static List<Class<?>> configurationList = null;

    //基于Action注解
    private static Map<String, Class<?>> actionMap = null;

    //拦截器
    private static Map<String, Class<?>> interceptorMap = null;

    /**
     * 扫描跟包class
     *
     * @param rootPackageName
     * @return
     */
    public static List<Class<?>> configurations(String rootPackageName) {
        if (configurationList == null) {
            getClasses(rootPackageName);
            //TODO 添加系统配置
            configurationList = new ArrayList<>(16);

            classes.forEach(scanClass -> {
                if (scanClass.getSuperclass() != AbstractAntelopeConfiguration.class) {
                    return;
                }
                configurationList.add(scanClass);
            });
        }
        return configurationList;
    }

    /**
     * 扫描获取所有的class
     *
     * @param rootPackageName
     * @return
     */
    private static void getClasses(String rootPackageName) {

        if (classes == null) {
            classes = new HashSet<>(32);
            String rootPackageDirName = rootPackageName.replace('.', '/');
            Enumeration<URL> resources;
            try {
                resources = Thread.currentThread().getContextClassLoader().getResources(rootPackageDirName);
                while (resources.hasMoreElements()) {
                    URL url = resources.nextElement();
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        String filePath = URLDecoder.decode(url.getFile(), "utf-8");
                        findAndAddClassesInPackageByFile(rootPackageName, filePath, true, classes);
                    } else if ("jar".equals(protocol)) {
                        JarFile jar;
                        try {
                            jar = ((JarURLConnection) url.openConnection()).getJarFile();
                            Enumeration<JarEntry> entries = jar.entries();
                            while (entries.hasMoreElements()) {
                                JarEntry entry = entries.nextElement();
                                String name = entry.getName();
                                if (name.charAt(0) == '/') {
                                    name = name.substring(1);
                                }
                                if (name.startsWith(rootPackageDirName)) {
                                    int idx = name.lastIndexOf('/');
                                    if (idx != -1) {
                                        rootPackageName = name.substring(0, idx).replace('/', '.');
                                    }
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        String className = name.substring(rootPackageName.length() + 1, name.length() - 6);
                                        try {
                                            classes.add(Class.forName(rootPackageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            logger.error("IOException", e);
                                        }
                                    }
                                }
                            }
                        } catch (IOException e) {
                            logger.error("IOException", e);
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("IOException", e);
            }
        }
    }

    private static void findAndAddClassesInPackageByFile(String rootPackageName, String filePath, boolean recursive,
                                                         Set<Class<?>> classes) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles(file ->
                (recursive && file.isDirectory()) || (file.getName().endsWith(".class"))
        );
        assert files != null;
        Arrays.stream(files).forEach(file -> {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(rootPackageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);

            } else {
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(rootPackageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    logger.error("ClassNotFoundException", e);
                }
            }
        });
    }

    /**
     * 初始化Action注解
     */
    public static void initAnnotation() {

        if (classes == null || classes.isEmpty()) {
            return;
        }
        actionMap = new HashMap<>(16);
        interceptorMap = new HashMap<>(16);
        classes.forEach(scanClass -> {
            if (scanClass.getAnnotation(AntelopeAction.class) != null ||
                    scanClass.getAnnotation(AntelopeInterceptor.class) != null) {
                Annotation[] annotations = scanClass.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof AntelopeAction) {
                        AntelopeAction antelopeAction = (AntelopeAction) annotation;
                        actionMap.put(StringUtils.isEmpty(antelopeAction.value()) ? scanClass.getName() : antelopeAction.value(), scanClass);
                    }
                    if (annotation instanceof AntelopeInterceptor) {
                        AntelopeInterceptor antelopeInterceptor = (AntelopeInterceptor) annotation;
                        interceptorMap.put(StringUtils.isEmpty(antelopeInterceptor.value()) ? scanClass.getName() : antelopeInterceptor.value(), scanClass);
                    }
                }
            }
        });
    }
}
