package com.yunshan.ycl.command.simple;

import com.yunshan.ycl.command.BaseCommandManager;
import com.yunshan.ycl.command.Command;
import com.yunshan.ycl.exception.NoneStackRuntimeException;
import com.yunshan.ycl.message.Messager;

/**
 * 简单命令管理器
 * <p>
 * 作者：YunShan
 * <p>
 * 创建日期：2016年6月30日
 * <p>
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
        if (command instanceof SimpleCommand) {
            SimpleCommand scmd = (SimpleCommand) command;
            if (!scmd.isInited()) throw new SimpleCommandNotInitialExecption(scmd);
        }
        return super.registerCommand(command);
    }
    
    /**
     * 简单命令未成功初始化异常<br/>
     * 该异常是在{@link SimpleCommandManager}注册命令时发现未成功初始化的SimpleCommand实例时抛出的
     * <p>
     * 作者：YunShan
     * <p>
     * 创建日期：2016年6月30日
     * <p>
     */
    public class SimpleCommandNotInitialExecption extends NoneStackRuntimeException {
        
        private static final long serialVersionUID = 1L;
        
        /** 未成功初始化的命令 */
        private final SimpleCommand command;
        
        /**
         * @param command
         *            未成功初始化的命令
         */
        public SimpleCommandNotInitialExecption(SimpleCommand command) {
            this.command = command;
        }
        
        /**
         * 获取导致此异常抛出的未成功初始化的命令
         * 
         * @return 未成功初始化的命令
         */
        protected SimpleCommand getCommand() {
            return this.command;
        }
    }
}
