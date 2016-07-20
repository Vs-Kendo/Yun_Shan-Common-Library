package com.yunshan.ycl.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collection;
import java.util.Objects;

import com.google.common.collect.Sets;
import com.yunshan.ycl.resource.Resource;
import com.yunshan.ycl.resource.URLResource;

/**
 * 反射工具
 */
public final class ReflectionUtils {
    
    private ReflectionUtils() {
    }
    
    /**
     * 获取指定类中被指定注释注释了的第一个方法
     * <p>
     * 获取了第一个后会立即返回，不会向后搜寻
     * 
     * @param clazz
     *            要获取方法的类
     * @param annClass
     *            指定的注释类型
     * @return 指定类中被指定注释注释了的第一个方法
     */
    public static Method getFirstMethodByAnnotation(Class<?> clazz, Class<? extends Annotation> annClass) {
        Method[] methos = clazz.getDeclaredMethods();
        for (Method method : methos) {
            if (method.isAnnotationPresent(annClass)) return method;
        }
        return null;
    }
    
    /**
     * 获取指定类中被指定注释注释了的第一个字段
     * <p>
     * 获取了第一个后会立即返回，不会向后搜寻
     * 
     * @param clazz
     *            要获取字段的类
     * @param annClass
     *            指定的注释类型
     * @return 指定类中被指定注释注释了的第一个字段
     */
    public static Field getFirstFieldByAnnotation(Class<?> clazz, Class<? extends Annotation> annClass) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annClass)) return field;
        }
        return null;
    }
    
    /**
     * 获取指定方法中第一次出现的指定参数注释
     * <p>
     * 获取了第一个后会立即返回，不会向后搜寻
     * 
     * @param method
     *            要查找的方法
     * @param annClass
     *            指定的注释类型
     * @return 指定方法中第一次出现的指定参数注释，若从没出现则返回null
     */
    public static Annotation findFirstAnnotationInParameters(Method method, Class<? extends Annotation> annClass) {
        int idx = findFirstAnnotationIndexInParameters(method, annClass);
        if (idx != -1) {
            for (Annotation ann : method.getParameterAnnotations()[idx]) {
                if (ann.getClass() == annClass) return ann;
            }
        }
        return null;
    }
    
    /**
     * 获取指定方法中指定参数注释第一次出现的位置
     * 
     * @param method
     *            要查找的方法
     * @param annClass
     *            指定的注释类型
     * @return 指定方法中第一次出现的位置，若从没出现则返回-1
     */
    public static int findFirstAnnotationIndexInParameters(Method method, Class<? extends Annotation> annClass) {
        Annotation[][] anns = method.getParameterAnnotations();
        for (int i = 0; i < anns.length; i++) {
            for (Annotation ann : anns[i]) {
                if (ann.getClass() == annClass) return i;
            }
        }
        return -1;
    }
    
    /**
     * 跟踪资源
     * <p>
     * 从调用栈路径遍历每个类，并通过<code>clazz.getResource(resourceName);</code>尝试获取资源
     * 
     * @param resourceName
     *            资源名，前面加"/"表示绝对路径
     * @return 资源集合
     */
    public static Collection<Resource> traceResource(String resourceName) {
        Objects.requireNonNull(resourceName);
        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
        Collection<Resource> resources = Sets.newHashSet();
        for (StackTraceElement stack : stackTraceElements) {
            try {
                Class<?> clazz = Class.forName(stack.getClassName());
                if (clazz.getClassLoader() == null) break; //无ClassLoader的类意味着已经跟踪到Java核心部分了
                URL resource = clazz.getClassLoader().getResource(resourceName);
                if (resource != null) resources.add(new URLResource(resource));
            } catch (ClassNotFoundException e) {
                //无法找到不在classPath的类，但没有影响
            }
        }
        return resources;
    }
}
