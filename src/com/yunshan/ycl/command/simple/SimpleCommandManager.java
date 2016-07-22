package com.yunshan.ycl.command.simple;

import com.yunshan.ycl.command.BaseCommandManager;
import com.yunshan.ycl.command.Command;
import com.yunshan.ycl.message.Messager;

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
        return super.registerCommand(command);
    }   
}
