package org.yunshanmc.ycl.utils.reflect;

import com.google.common.collect.Sets;
import org.yunshanmc.ycl.resource.Resource;
import org.yunshanmc.ycl.resource.URLResource;

import java.net.URL;
import java.util.Collection;
import java.util.Objects;

/**
 * 反射工具
 */
public final class ReflectionUtils {
    
    private ReflectionUtils() {
    }
    
    /**
     * 跟踪资源
     * <p>
     * 从调用栈路径遍历每个类，并通过<code>clazz.getResource(resourceName);</code>尝试获取资源
     * 
     * @param resourceName
     *            资源名，需要完整名称，不接受"/"开头的形式
     * @return 资源集合
     */
    public static Collection<Resource> traceResource(String resourceName) {
        return traceResource(resourceName, new Throwable().getStackTrace());
    }
    
    /**
     * 跟踪资源
     * <p>
     * 从指定调用栈路径遍历每个类，并通过<code>clazz.getResource(resourceName);</code>尝试获取资源
     * 
     * @param resourceName
     *            资源名，需要完整名称，不接受"/"开头的形式
     * @param stackTrace
     *            指定的调用栈路径
     * @return 资源集合
     */
    public static Collection<Resource> traceResource(String resourceName, StackTraceElement[] stackTrace) {
        Objects.requireNonNull(resourceName);
        Objects.requireNonNull(stackTrace);
        Collection<Resource> resources = Sets.newHashSet();
        for (StackTraceElement stack : stackTrace) {
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
