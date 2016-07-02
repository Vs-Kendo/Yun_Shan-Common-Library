package com.yunshan.ycl.message;

import org.bukkit.command.CommandSender;

/**
 * 信息发送者
 */
public interface Messager {
    
    /**
     * 发送普通信息
     * 
     * @param receiver
     *            信息接收者
     * @param msgKey
     *            信息的键
     * @param args
     *            信息文本中的参数列表
     */
    void info(CommandSender receiver, String msgKey, Object... args);
    
    /**
     * 发送调试信息
     * 
     * @param debugLvl
     *            调试级别，若当前调试级别低于此调试级别则该条信息不会发送
     * @param receiver
     *            信息接收者
     * @param msgKey
     *            信息的键
     * @param args
     *            信息文本中的参数列表
     */
    void debug(int debugLevel, CommandSender receiver, String msgKey, Object... args);
    
    /**
     * 发送警告信息
     * 
     * @param receiver
     *            信息接收者
     * @param msgKey
     *            信息的键
     * @param args
     *            信息文本中的参数列表
     */
    void warning(CommandSender receiver, String msgKey, Object... args);
    
    /**
     * 发送错误信息
     * 
     * @param receiver
     *            信息接收者
     * @param msgKey
     *            信息的键
     * @param args
     *            信息文本中的参数列表
     */
    void error(CommandSender receiver, String msgKey, Object... args);
    
    /**
     * 向控制台发送普通信息
     * 
     * @param receiver
     *            信息接收者
     * @param msgKey
     *            信息的键
     * @param args
     *            信息文本中的参数列表
     * @see #info(CommandSender, String, Object...)
     */
    void infoConsole(String msgKey, Object... args);
    
    /**
     * 向控制台发送调试信息
     * 
     * @param debugLvl
     *            调试级别，若当前调试级别低于此调试级别则该条信息不会向控制台发送
     * @param receiver
     *            信息接收者
     * @param msgKey
     *            信息的键
     * @param args
     *            信息文本中的参数列表
     * @see #debug(CommandSender, String, Object...)
     */
    void debugConsole(int debugLevel, String msgKey, Object... args);
    
    /**
     * 向控制台发送警告信息
     * 
     * @param receiver
     *            信息接收者
     * @param msgKey
     *            信息的键
     * @param args
     *            信息文本中的参数列表
     * @see #warning(CommandSender, String, Object...)
     */
    void warningConsole(String msgKey, Object... args);
    
    /**
     * 向控制台发送错误信息
     * 
     * @param receiver
     *            信息接收者
     * @param msgKey
     *            信息的键
     * @param args
     *            信息文本中的参数列表
     * @see #error(CommandSender, String, Object...)
     */
    void errorConsole(String msgKey, Object... args);
    
    /**
     * 设置当前调试级别
     * <p>
     * 此调试级别将用来判断是否需要输出debug信息
     * 
     * @param debugLevel
     *            要设置的调试级别
     * @return 返回本体(<code>return this</code>)
     */
    Messager setDebugLevel(int debugLevel);
    
    /**
     * 获取当前调试级别
     * 
     * @return 当前调试级别
     */
    int getDebugLevel();
    
}
