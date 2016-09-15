package org.yunshanmc.ycl.command;

import org.bukkit.command.CommandSender;

/**
 * 命令上下文
 * <p>
 * 作者： Yun-Shan
 * 创建日期： 2016/9/15.
 */
public class CommandContext {
    
    private CommandSender sender;
    private Command       command;
    private String[]      args;
    
    public CommandContext(CommandSender sender, Command command, String[] args) {
        this.sender = sender; this.command = command; this.args = args;
    }
    
    public String[] getArgs() {
        return this.args;
    }
    
    public CommandSender getSender() {
        return this.sender;
    }
    
    public Command getCommand() {
        return this.command;
    }
}
