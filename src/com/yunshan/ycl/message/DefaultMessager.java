package com.yunshan.ycl.message;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

/**
 * 默认的消息发送者
 */
public class DefaultMessager implements Messager {
    
    protected static final ConsoleCommandSender Console = Bukkit.getConsoleSender();
    
    protected static final String MSG_TYPE_PREFIX = "message.format.";
    protected static final String TYPE_INFO       = "info";
    protected static final String TYPE_DEBUG      = "debug";
    protected static final String TYPE_WARNING    = "warning";
    protected static final String TYPE_ERROR      = "error";
    
    private final MessageManager messageManager;
    
    private int debugLevel;
    
    public DefaultMessager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }
    
    @Override
    public void info(CommandSender receiver, String msgKey, Object... args) {
        this.sendMessage(receiver, TYPE_INFO, msgKey, args);
    }
    
    @Override
    public void debug(int debugLevel, CommandSender receiver, String msgKey, Object... args) {
        if (this.debugLevel >= debugLevel) {
            this.sendMessage(receiver, TYPE_DEBUG, msgKey, args);
        }
    }
    
    @Override
    public void warning(CommandSender receiver, String msgKey, Object... args) {
        this.sendMessage(receiver, TYPE_WARNING, msgKey, args);
    }
    
    @Override
    public void error(CommandSender receiver, String msgKey, Object... args) {
        this.sendMessage(receiver, TYPE_ERROR, msgKey, args);
    }
    
    @Override
    public void infoConsole(String msgKey, Object... args) {
        this.info(Console, msgKey, args);
    }
    
    @Override
    public void debugConsole(int debugLevel, String msgKey, Object... args) {
        this.debug(debugLevel, Console, msgKey, args);
    }
    
    @Override
    public void warningConsole(String msgKey, Object... args) {
        this.warning(Console, msgKey, args);
    }
    
    @Override
    public void errorConsole(String msgKey, Object... args) {
        this.error(Console, msgKey, args);
    }
    
    @Override
    public DefaultMessager setDebugLevel(int debugLevel) {
        this.debugLevel = debugLevel;
        return this;
    }
    
    @Override
    public int getDebugLevel() {
        return this.debugLevel;
    }
    
    protected void sendMessage(CommandSender receiver, String msgType, String msgKey, Object... args) {
        receiver.sendMessage(this.getMessage(msgType, msgKey, args));
    }
    
    /** 无消息类型前缀 */
    private static final String NO_MSG_TYPE_PREFIX = "$$";
    
    protected String[] getMessage(String msgType, String msgKey, Object... args) {
        String msg = this.messageManager.getMessage(msgKey, args);
        String[] formatMsgs = msg.split("\n");
        for (int i = 0; i < formatMsgs.length; i++) {
            String formatMsg = formatMsgs[i];
            if (!formatMsg.startsWith(NO_MSG_TYPE_PREFIX)) {
                formatMsg = this.messageManager.getMessage(MSG_TYPE_PREFIX + msgType, formatMsg);
            }
            formatMsgs[i] = formatMsg;
        }
        return formatMsgs;
    }
}
