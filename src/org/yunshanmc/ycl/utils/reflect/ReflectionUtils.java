package org.yunshanmc.ycl.utils.reflect;

import com.google.common.collect.Lists;
import org.yunshanmc.ycl.exception.ExceptionUtils;
import org.yunshanmc.ycl.resource.Resource;
import org.yunshanmc.ycl.resource.URLResource;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * 反射工具
 */
public final class ReflectionUtils {
    
    private ReflectionUtils() {
    }
    
    /**
     * 设置字段值
     *
     * @param clazz 字段所属的class
     * @param fieldName 字段名
     * @param obj 字段所属的对象，静态字段传null即可
     * @param value 要设置的字段值
     *
     * @return 设置成功返回true，失败返回false
     * @see #setFieldValueWithException(Class, String, Object, Object)
     */
    public static boolean setFieldValue(
        final Class<?> clazz, final String fieldName, final Object obj, final Object value) {
        try {
            setFieldValueWithException(clazz, fieldName, obj, value);
            return true;
        } catch (NoSuchFieldException | IllegalArgumentException e) {
            ExceptionUtils.handle(e);
            return false;
        }
    }
    
    /**
     * 获取字段值
     *
     * @param clazz 字段所属的class
     * @param fieldName 字段名
     * @param obj 字段所属的对象，静态字段传null即可
     *
     * @return 设置成功返回true，失败返回false
     * @see #getFieldValueWithException(Class, String, Object)
     */
    public static Object getFieldValue(final Class<?> clazz, final String fieldName, final Object obj) {
        try {
            return getFieldValueWithException(clazz, fieldName, obj);
        } catch (NoSuchFieldException | IllegalArgumentException e) {
            ExceptionUtils.handle(e);
            return null;
        }
    }
    
    /**
     * 设置字段值
     *
     * @param clazz 字段所属的class
     * @param fieldName 字段名
     * @param obj 字段所属的对象，静态字段传null即可
     * @param value 要设置的字段值
     *
     * @throws NoSuchFieldException if a field with the specified name is not found.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof), or if an unwrapping conversion fails.
     */
    public static void setFieldValueWithException(Class<?> clazz, String fieldName, Object obj, Object value)
        throws NoSuchFieldException, IllegalArgumentException {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        try {
            f.set(obj, value);
        } catch (IllegalAccessException e) {
            ExceptionUtils.handle(e);
            // 已经setAccessible, 该错误不会出现
        }
    }
    
    /**
     * 获取字段值
     *
     * @param clazz 字段所属的class
     * @param fieldName 字段名
     * @param obj 字段所属的对象，静态字段传null即可
     *
     * @return 设置成功返回true，失败返回false
     * @throws NoSuchFieldException if a field with the specified name is not found.
     * @throws IllegalArgumentException if the specified object is not an instance of the class or interface declaring the underlying field (or a subclass or implementor thereof).
     */
    public static Object getFieldValueWithException(final Class<?> clazz, final String fieldName, final Object obj)
        throws NoSuchFieldException, IllegalArgumentException {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        try {
            return f.get(obj);
        } catch (IllegalAccessException e) {
            ExceptionUtils.handle(e);
            // 已经setAccessible, 该错误不会出现
            return null;
        }
    }
    
    /**
     * 跟踪资源
     * <p>
     * 从调用栈路径遍历每个类，并通过<code>clazz.getResource(resourceName);</code>尝试获取资源
     *
     * @param resourceName
     *     资源名，需要完整名称，不接受"/"开头的形式
     *
     * @return 资源列表
     */
    public static List<Resource> traceResource(String resourceName) {
        return traceResource(resourceName, new Throwable().getStackTrace());
    }
    
    /**
     * 跟踪资源
     * <p>
     * 从指定调用栈路径遍历每个类，并通过<code>clazz.getResource(resourceName);</code>尝试获取资源
     *
     * @param resourceName
     *     资源名，需要完整名称，不接受"/"开头的形式
     * @param stackTrace
     *     指定的调用栈路径
     *
     * @return 资源列表
     */
    public static List<Resource> traceResource(String resourceName, StackTraceElement[] stackTrace) {
        Objects.requireNonNull(resourceName);
        Objects.requireNonNull(stackTrace);
        List<Resource> resources = Lists.newLinkedList();
        for (StackTraceElement stack : stackTrace) {
            try {
                Class<?> clazz = Class.forName(stack.getClassName());
                if (clazz.getClassLoader() == null) continue; //无ClassLoader的类是Java核心类
                URL resource = clazz.getClassLoader().getResource(resourceName);
                if (resource != null) resources.add(new URLResource(resource));
            } catch (ClassNotFoundException e) {
                //无法找到不在classPath的类，但没有影响
            }
        }
        return resources;
    }
}
