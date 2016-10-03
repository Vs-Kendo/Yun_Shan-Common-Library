package org.yunshanmc.ycl.command;

import org.bukkit.command.CommandSender;
import org.yunshanmc.ycl.message.Messager;

/**
 * 命令接口
 */
public interface Command {
    
    /**
     * 获取命令名
     *
     * @return 命令名
     */
    String getName();
    
    /**
     * 获取命令语法
     *
     * @return 命令语法
     */
    String getUsage();
    
    /**
     * 获取命令描述
     *
     * @return 命令描述
     */
    String getDescription();
    
    /**
     * 查看命令是否有效
     * <p>
     * 当返回false时命令将不会被调用，并向玩家返回命令被禁用的信息
     *
     * @return 命令有效返回true，否则返回false
     */
    boolean isValid();
    
    /**
     * 设置命令有效性
     *
     * @param valid
     *     是否有效
     *
     * @return 是否设置成功
     */
    boolean setValidity(boolean valid);
    
    /**
     * 设置Messager，用于发送信息
     *
     * @param messager
     *     Messager
     */
    void setMessager(Messager messager);
    
    /**
     * 执行命令
     *
     * @param sender
     *     命令发送者
     * @param args
     *     参数列表
     */
    void execute(CommandSender sender, String... args);
}
