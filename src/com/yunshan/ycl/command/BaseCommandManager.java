package com.yunshan.ycl.command;

import java.util.Arrays;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;
import com.yunshan.ycl.message.Messager;
import com.yunshan.ycl.message.NullMessager;

/**
 * 基础命令管理器
 * <p>
 * 作者：YunShan<br>
 * 创建日期：2016年6月30日
 */
public abstract class BaseCommandManager implements CommandManager {
    
    protected Map<String, Command> commands = Maps.newHashMap();
    
    private PluginCommand handleCmd;
    private String        handleCmdName;
    
    protected String mainCommand;
    
    protected Messager messager;
    
    public BaseCommandManager() {
        this(null);
    }
    
    public BaseCommandManager(Messager messager) {
        this.setMessager(messager);
    }
    
    @Override
    public CommandManager setHandleCommand(String handle, JavaPlugin plugin) {
        if (this.handleCmd != null) {
            this.handleCmd.setExecutor(null);
            this.handleCmdName = null;
        }
        PluginCommand cmd = plugin.getCommand(handle);
        cmd.setExecutor(this);
        this.handleCmdName = cmd.getName();
        return this;
    }
    
    @Override
    public void setMessager(Messager messager) {
        this.messager = messager != null ? messager : new NullMessager();
    }
    
    @Override
    public boolean registerCommand(Command command) {
        if (this.commands.containsKey(command.getName())) return false;
        
        this.commands.put(command.getName(), command);
        return true;
    }
    
    @Override
    public CommandManager setMainCommand(String cmdName) {
        this.mainCommand = cmdName;
        return this;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        String cmdName = args.length > 0 ? args[0] : this.mainCommand;
        if (cmdName == null) {
            this.messager.info(sender, "message.command.notMain", this.handleCmdName);
            return true;
        }
        Command cmd = this.commands.get(cmdName);
        
        if (cmd != null) {
            if (!cmd.isVaild()) {
                this.messager.info(sender, "message.command.invaild", this.handleCmdName, cmdName);
                return true;
            }
            if (args.length >= 2) {
                args = Arrays.copyOfRange(args, 1, args.length);
            } else {
                args = new String[0];
            }
            boolean usageRight = cmd.execute(sender, args);
            if (!usageRight) {
                this.messager.info(sender, "command.usage." + this.handleCmdName + "." + cmdName, this.handleCmdName,
                        cmdName);
            }
            return true;
        } else {
            this.messager.info(sender, "message.command.noFound", this.handleCmdName, cmdName);
            return true;
        }
    }
    
    @Override
    public boolean unregisterCommand(Command command) {
        return this.commands.remove(command) != null;
    }
    
    @Override
    public Command getCommand(String name) {
        return this.commands.get(name);
    }
}
