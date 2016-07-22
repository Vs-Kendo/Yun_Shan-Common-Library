package com.yunshan.ycl.command;

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
    
    private PluginCommand oldHandle;
    
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
        if (this.oldHandle != null) {
            this.oldHandle.setExecutor(null);
        }
        PluginCommand cmd = plugin.getCommand(handle);
        cmd.setExecutor(this);
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
            this.messager.info(sender, "message.command.notMain", this.oldHandle.getName());
            return true;
        }
        Command cmd = this.commands.get(cmdName);
        
        if (cmd != null) {
            if (!cmd.isVaild()) {
                this.messager.info(sender, "message.command.invaild", this.oldHandle.getName(), cmdName);
                return true;
            }
            return cmd.execute(sender, args);
        } else {
            this.messager.info(sender, "message.command.noFound", this.oldHandle.getName(), cmdName);
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
