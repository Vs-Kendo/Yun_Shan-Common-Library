package org.yunshanmc.ycl.command.simple;

import org.yunshanmc.ycl.command.BaseCommandManager;
import org.yunshanmc.ycl.command.Command;
import org.yunshanmc.ycl.message.Messager;
import org.yunshanmc.ycl.utils.reflect.ReflectionUtils;

/**
 * 简单命令管理器
 * <p>
 * 作者：YunShan<br>
 * 创建日期：2016年6月30日
 */
public class SimpleCommandManager extends BaseCommandManager {
    
    public SimpleCommandManager() {
        this(null);
    }
    
    public SimpleCommandManager(Messager messager) {
        super(messager);
    }
    
    @Override
    public boolean registerCommand(Command command) {
        if (super.registerCommand(command)) {
            ReflectionUtils.setFieldValue(SimpleCommand.class, "commandManager", command, this);
            return true;
        }
        return false;
    }
}
