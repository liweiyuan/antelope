package com.learn.netty.util;

import com.learn.netty.annotation.AntelopeAction;
import com.learn.netty.annotation.AntelopeInterceptor;
import com.learn.netty.annotation.AntelopeRoute;
import com.learn.netty.config.AppConfig;
import com.learn.netty.configuration.AbstractAntelopeConfiguration;
import com.learn.netty.configuration.ApplicationConfiguration;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
    private static Map<HashMap<String, Integer>, Class<?>> interceptorMap = null;

    //匹配路由规则
    private static Map<String, Method> routeMap = null;

    /**
     * 扫描跟包class
     *
     * @param rootPackageName
     * @return
     */
    public static List<Class<?>> configurations(String rootPackageName) {
        if (configurationList == null) {
            getClasses(rootPackageName);

            configurationList = new ArrayList<>(16);

            classes.add(ApplicationConfiguration.class);

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
        //2018年11月19日
        routeMap = new HashMap<>(16);
        classes.forEach(scanClass -> {
            if (scanClass.getAnnotation(AntelopeAction.class) != null ||
                    scanClass.getAnnotation(AntelopeInterceptor.class) != null) {
                Annotation[] annotations = scanClass.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof AntelopeAction) {
                        AntelopeAction antelopeAction = (AntelopeAction) annotation;
                        actionMap.put(antelopeAction.value(), scanClass);

                        //扫描方法级别的注解
                        Method[] methods = scanClass.getDeclaredMethods();
                        for (Method method : methods) {
                            if (method.getModifiers() != Modifier.PUBLIC) {
                                continue;
                            }
                            AntelopeRoute route = method.getAnnotation(AntelopeRoute.class);
                            if (route == null) {
                                continue;
                            }
                            routeMap.put(AppConfig.newInstance().getRootPath() + antelopeAction.value() + "/" +
                                    route.value(), method);
                        }
                    }
                    if (annotation instanceof AntelopeInterceptor) {
                        AntelopeInterceptor antelopeInterceptor = (AntelopeInterceptor) annotation;
                        HashMap<String, Integer> hashMap = new HashMap<>();
                        hashMap.put(antelopeInterceptor.value(), antelopeInterceptor.order());
                        interceptorMap.put(hashMap, scanClass);
                    }
                }
            }
        });
    }

    /**
     * 获取Action
     *
     * @param actionPath
     * @return
     */
    public static Class<?> getActionClass(String actionPath) {
        return actionMap.get(actionPath);
    }

    /**
     * 获取拦截器
     *
     * @param
     * @return
     */
    public static Map<HashMap<String, Integer>, Class<?>> getInterceptorMap() {
        return interceptorMap;
    }

    /**
     * 获取路由器
     *
     * @return
     */
    public static Map<String, Method> getRouteMap() {
        return routeMap;
    }
}
