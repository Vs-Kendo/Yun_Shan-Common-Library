package com.yunshan.ycl.exception;

import java.util.List;
import java.util.Map;

import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;
import com.yunshan.ycl.util.BukkitUtils;

/**
 * 异常工具类
 */
public final class ExceptionUtils {
    
    private ExceptionUtils() {
    }
    
    private static final ExceptionHandler DEFAULT_HANDLER = new ExceptionHandler() {
        
        @Override
        public void handle(Throwable t) {
            t.printStackTrace();
        }
    };
    
    private static Map<Plugin, ExceptionHandler> exceptionHandler = Maps.newHashMap();
    
    /**
     * 设置异常处理器
     * 
     * @param handler
     *            要使用的异常处理器
     */
    public static void setExceptionHandler(ExceptionHandler handler) {
        if (handler != null) {
            List<Plugin> plugins = BukkitUtils.tracePlugin(true);
            Plugin plugin;
            if (plugins.size() >= 2) {
                plugin = plugins.get(1);
            } else {
                plugin = plugins.get(0);
            }
            exceptionHandler.put(plugin, handler);
        }
    }
    
    /**
     * 处理异常
     * 
     * @param t
     *            要处理的异常
     * @see ExceptionHandler#handle(Throwable)
     */
    public static void handle(Throwable t) {
        List<Plugin> plugins = BukkitUtils.tracePlugin(t.getStackTrace());
        ExceptionHandler handler;
        if (!plugins.isEmpty()) {
            handler = exceptionHandler.get(plugins.get(0));
        } else {
            handler = DEFAULT_HANDLER;
        }
        handler.handle(t);
    }
    
}
