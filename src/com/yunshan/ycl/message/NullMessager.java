package com.yunshan.ycl.message;

import org.bukkit.command.CommandSender;

/**
 * 空信息发生者，此类没有任何功能，仅用于实现空对象模式
 * <p>
 * 作者：YunShan
 * <p>
 * 创建日期：2016年6月30日
 * <p>
 */
public final class NullMessager implements Messager {
    
    /**
     * 空方法
     */
    @Override
    public void info(CommandSender receiver, String msgKey, Object... args) {
    }
    
    /**
     * 空方法
     */
    @Override
    public void debug(int debugLevel, CommandSender receiver, String msgKey, Object... args) {
    }
    
    /**
     * 空方法
     */
    @Override
    public void warning(CommandSender receiver, String msgKey, Object... args) {
    }
    
    /**
     * 空方法
     */
    @Override
    public void error(CommandSender receiver, String msgKey, Object... args) {
    }
    
    /**
     * 空方法
     */
    @Override
    public void infoConsole(String msgKey, Object... args) {
    }
    
    /**
     * 空方法
     */
    @Override
    public void debugConsole(int debugLevel, String msgKey, Object... args) {
    }
    
    /**
     * 空方法
     */
    @Override
    public void warningConsole(String msgKey, Object... args) {
    }
    
    /**
     * 空方法
     */
    @Override
    public void errorConsole(String msgKey, Object... args) {
    }
    
    /**
     * 空方法
     * @return 
     */
    @Override
    public Messager setDebugLevel(int debugLevel) {
        return this;
    }
    
    /**
     * 空方法
     */
    @Override
    public int getDebugLevel() {
        return 0;
    }
    
}
