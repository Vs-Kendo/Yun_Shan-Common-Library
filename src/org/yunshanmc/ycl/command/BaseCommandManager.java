package org.yunshanmc.ycl.command;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.yunshanmc.ycl.message.Messager;
import org.yunshanmc.ycl.message.NullMessager;

import java.util.Arrays;
import java.util.Map;

/**
 * 基础命令管理器
 * <p>
 * 作者：YunShan<br>
 * 创建日期：2016年6月30日
 */
public abstract class BaseCommandManager implements CommandManager {
    
    protected Map<String, Command>              commands    = Maps.newHashMap();
    protected Map<String, Map<String, Command>> subCommands = Maps.newHashMap();
    protected String        mainCommand;
    protected Messager      messager;
    protected ThreadLocal<String> parentCmd = new ThreadLocal<>();
    private   PluginCommand handleCmd;
    private   String        handleCmdName;
    
    public BaseCommandManager() {
        this(null);
    }
    
    public BaseCommandManager(Messager messager) {
        this.setMessager(messager);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        String cmdName = args.length > 0 ? args[0] : this.mainCommand;
        if (cmdName == null) {
            this.messager.info(sender, "command.notMain", this.handleCmdName);
            return true;
        }
        Command cmd = null;
        boolean isSub = false;
        if (args.length >= 2) {// 优先搜索子命令
            Map<String, Command> subs = this.subCommands.get(cmdName);
            if (subs != null) {
                cmd = subs.get(args[1]);
            }
        }
        if (cmd != null) {
            isSub = true;
        } else {
            cmd = this.commands.get(cmdName);// 子命令不存在则搜索根命令
        }
        
        if (cmd != null) {
            if (!cmd.isValid()) {
                this.messager.info(sender, "command.invalid", this.handleCmdName, cmdName);
                return true;
            }
            int startIdx = isSub ? 2 : 1;
            if (args.length >= startIdx + 1) {
                args = Arrays.copyOfRange(args, startIdx, args.length);
            } else {
                args = new String[0];
            }
            if (isSub) parentCmd.set(cmdName);
            cmd.execute(sender, args);
            if (isSub) parentCmd.remove();// 及时移除防止数据污染
            return true;
        } else {
            this.messager.info(sender, "command.noFound", this.handleCmdName, cmdName);
            return true;
        }
    }
    
    @Override
    public Command getCommand(String name) {
        return this.commands.get(name);
    }
    
    @Override
    public String getMainCommand() {
        return this.mainCommand;
    }
    
    @Override
    public CommandManager setMainCommand(String cmdName) {
        this.mainCommand = cmdName;
        return this;
    }
    
    @Override
    public void setMessager(Messager messager) {
        this.messager = messager != null ? messager : NullMessager.getInstance();
    }
    
    @Override
    public CommandManager setHandleCommand(String handle, JavaPlugin plugin) {
        if (this.handleCmd != null) {
            this.handleCmd.setExecutor(plugin);
            this.handleCmdName = null;
        }
        PluginCommand cmd = plugin.getCommand(handle);
        cmd.setExecutor(this);
        this.handleCmd = cmd;
        this.handleCmdName = cmd.getName();
        return this;
    }
    
    @Override
    public void showUsage(CommandSender sender, String cmdName) {
        String parent = parentCmd.get();
        Command cmd;
        String path;
        if (parent != null) {
            cmd = this.subCommands.get(parent).get(cmdName);
            path = Joiner.on('.').join("command.usage.cmd", parent, cmdName);
            cmdName = parent + " " + cmdName;
        } else {
            cmd = this.commands.get(cmdName);
            path = Joiner.on('.').join("command.usage.cmd", cmdName);
        }
        this.messager.info(sender, "command.usage.head", cmdName);
        this.messager.info(sender, path, cmdName, cmd.getUsage(), cmd.getDescription());
        this.messager.info(sender, "command.usage.foot", cmdName);
    }
    
    @Override
    public boolean registerCommand(Command command) {
        if (this.commands.containsKey(command.getName())) return false;
        
        this.commands.put(command.getName(), command);
        command.setMessager(this.messager);
        return true;
    }
    
    @Override
    public boolean registerSubCommand(String parent, Command subCommand) {
        Map<String, Command> subs = this.subCommands.get(parent);
        if (subs == null) {
            subs = Maps.newHashMap();
            this.subCommands.put(parent, subs);
        }
        if (subs.containsKey(subCommand.getName())) return false;
        subs.put(subCommand.getName(), subCommand);
        return true;
    }
    
    @Override
    public boolean unregisterCommand(Command command) {
        return this.commands.remove(command.getName()) != null;
    }
    
    @Override
    public boolean unregisterSubCommand(String parent, Command subCommand) {
        Map<String, Command> subs = this.subCommands.get(parent);
        return subs != null && subs.remove(subCommand.getName()) != null;
    }
}
