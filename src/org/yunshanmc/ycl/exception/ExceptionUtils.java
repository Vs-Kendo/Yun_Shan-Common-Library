package org.yunshanmc.ycl.exception;

import com.google.common.collect.Maps;
import org.bukkit.plugin.Plugin;
import org.yunshanmc.ycl.utils.BukkitUtils;

import java.util.List;
import java.util.Map;

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
            Plugin plugin = BukkitUtils.tracePlugin(true).get(0);
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
        ExceptionHandler handler = null;
        if (!plugins.isEmpty()) {
            handler = exceptionHandler.get(plugins.get(0));
        }
        if (handler == null) handler = DEFAULT_HANDLER;
        handler.handle(t);
    }
    
}
