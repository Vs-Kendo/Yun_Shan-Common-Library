package org.yunshanmc.ycl.utils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.yunshanmc.ycl.config.ReadOnlyConfiguration;
import org.yunshanmc.ycl.resource.Resource;
import org.yunshanmc.ycl.utils.reflect.ReflectionUtils;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Bukkit工具
 * <p>
 * 作者：YunShan<br>
 * 创建日期：2016年7月20日
 */
public final class BukkitUtils {
    
    private static final PluginManager pluginManager  = Bukkit.getPluginManager();
    /**
     * 未知插件
     */
    private static final Plugin        Plugin_Unknown = (Plugin) Proxy.newProxyInstance(BukkitUtils.class.getClassLoader(),
                                                                                        new Class[] { Plugin.class },
                                                                                        (proxy, method, args) -> {
                                                                                    if ("getName".equals(method.getName())) {
                                                                                        return "UnknownPlugin";
                                                                                    }
                                                                                    return null;
                                                                                });
    
    private BukkitUtils() {
    }
    
    /**
     * 跟踪插件
     * <p>
     * 从调用栈路径遍历每个类，并尝试获取这些类所属的插件
     *
     * @param hasSelf
     *     是否包括自身
     *
     * @return 按调用顺序排列的插件列表
     */
    public static List<Plugin> tracePlugin(boolean hasSelf) {
        List<Plugin> plugins = tracePlugin(new Throwable().getStackTrace());
        if (!hasSelf) {
            plugins.remove(0);
        }
        return plugins;
    }
    
    /**
     * 跟踪插件
     * <p>
     * 从指定调用栈路径遍历每个类，并尝试获取这些类所属的插件
     *
     * @param stackTrace
     *     指定的调用栈路径
     *
     * @return 按调用顺序排列的插件列表
     */
    public static List<Plugin> tracePlugin(StackTraceElement[] stackTrace) {
        List<Resource> resources = ReflectionUtils.traceResource("plugin.yml", stackTrace);
        List<Plugin> plugins = Lists.newLinkedList();
        Iterables.addAll(plugins, Lists.transform(resources, BukkitUtils::resolvePlugin));
        return plugins;
    }
    
    private static Plugin resolvePlugin(Resource resource) {
        ReadOnlyConfiguration info = ReadOnlyConfiguration.loadConfiguration(resource.getInputStream());
        if (info == null) {
            return Plugin_Unknown;
        }
        return pluginManager.getPlugin(info.getString("name"));
    }
}
