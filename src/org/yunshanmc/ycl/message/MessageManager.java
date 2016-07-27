package org.yunshanmc.ycl.message;

import java.text.MessageFormat;

/**
 * 信息管理器
 */
public interface MessageManager {
    
    /**
     * 获取格式化后的本地化信息
     * 
     * @param key
     *            本地化信息对应的键
     * @param args
     *            格式化信息所需的参数
     * @return 格式化后的本地化信息
     */
    String getMessage(String key, Object... args);
    
    /**
     * 创建消息发送者
     * 
     * @return 新建的消息发送者
     */
    Messager createMessager();
    
    /**
     * 获取本地化信息的MessageFormat对象
     * 
     * @param key
     *            本地化信息对应的键
     * @return 本地化信息的MessageFormat对象
     */
    MessageFormat getMessageFormat(String key);
    
}