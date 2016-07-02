package com.yunshan.ycl.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.yunshan.ycl.message.Messager;

/**
 * 命令管理器
 */
public interface CommandManager extends CommandExecutor {
    
    /**
     * 注册命令
     * <p>
     * 若已存在同名命令会注册失败
     * 
     * @param command
     *        要注册的命令
     * @return 若已存在同名命令，则返回false,否则返回true
     */
    boolean registerCommand(Command command);
    
    /**
     * 反注册命令
     * 
     * @param command
     *        要反注册的命令
     * @return 反注册成功返回true,若指定的命令不存在会返回false
     */
    boolean unregisterCommand(Command command);
    
    /**
     * 从已注册的命令中获取指定命令
     * 
     * @param name
     *        命令名
     * @return 指定名字的命令，若没找到则返回null
     */
    Command getCommand(String name);
    
    /**
     * 设置主命令
     * <p>
     * 主命令是当用户未输入任何命令参数时所执行的命令(此处的命令参数指用户在游戏中实际输入的参数)
     * 
     * @param cmdName
     *        主命令的名字
     */
    CommandManager setMainCommand(String cmdName);
    
    /**
     * 设置用于输出信息的信息发生者
     * <p>
     * 设为null时会使命令管理器失去所有信息输出能力，因此不建议设为null
     * 
     * @param messager
     *        信息发生者
     */
    void setMessager(Messager messager);
    
    /**
     * 设置要管理的命令
     * 
     * @param handle
     *        要管理的主命令
     * @param plugin
     *        命令所属的插件
     * @return 返回自身
     */
    CommandManager setHandleCommand(String handle, JavaPlugin plugin);
}
