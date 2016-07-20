package com.yunshan.ycl.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.google.common.collect.Lists;
import com.yunshan.ycl.YCL;
import com.yunshan.ycl.config.ReadOnlyConfiguration;
import com.yunshan.ycl.resource.Resource;

/**
 * Bukkit工具
 * <p>
 * 作者：YunShan
 * <p>
 * 创建日期：2016年7月20日
 * <p>
 */
public final class BukkitUtils {
    
    private BukkitUtils() {
    }
    
    private static final Plugin PLUGIN_SELF   = YCL.getInstance();
    /** 未知插件 */
    private static final Plugin Plugin_Unknow = (Plugin) Proxy.newProxyInstance(BukkitUtils.class.getClassLoader(),
            new Class[] { Plugin.class }, new InvocationHandler() {
                
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if ("getName".equals(method.getName())) {
                        return "UnknowPlugin";
                    }
                    return null;
                }
            });
            
    /**
     * 跟踪插件
     * <p>
     * 从调用栈路径遍历每个类，并尝试获取这些类所属的插件
     * 
     * @param hasSelf
     *            是否包括自身
     * @return 按调用顺序排列的插件列表
     */
    public static List<Plugin> tracePlugin(boolean hasSelf) {
        return tracePlugin(hasSelf, new Throwable().getStackTrace());
    }
    
    /**
     * 跟踪插件
     * <p>
     * 从指定调用栈路径遍历每个类，并尝试获取这些类所属的插件
     * 
     * @param hasSelf
     *            是否包括自身
     * @return 按调用顺序排列的插件列表
     */
    public static List<Plugin> tracePlugin(boolean hasSelf, StackTraceElement[] stackTrace) {
        Collection<Resource> resources = ReflectionUtils.traceResource("plugin.yml", stackTrace);
        List<Plugin> plugins = Lists.newLinkedList();
        PluginManager pm = Bukkit.getPluginManager();
        for (Resource res : resources) {
            ReadOnlyConfiguration info = ReadOnlyConfiguration.loadConfiguration(res.getInputStream());
            if (info == null) {
                plugins.add(Plugin_Unknow);
                continue;
            }
            Plugin plugin = pm.getPlugin(info.getString("name"));
            if (plugin != PLUGIN_SELF && !plugins.contains(plugin)) {
                plugins.add(plugin);
            }
        }
        Collections.reverse(plugins);
        if (hasSelf) {
            plugins.add(PLUGIN_SELF);
        }
        return plugins;
    }
}
