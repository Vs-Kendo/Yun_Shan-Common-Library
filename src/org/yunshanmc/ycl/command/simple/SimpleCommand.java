package org.yunshanmc.ycl.command.simple;

import com.google.common.collect.Collections2;
import org.bukkit.command.CommandSender;
import org.yunshanmc.ycl.command.Command;
import org.yunshanmc.ycl.command.simple.ArgConverter.ArgConverterFailException;
import org.yunshanmc.ycl.exception.ExceptionUtils;
import org.yunshanmc.ycl.message.Messager;
import org.yunshanmc.ycl.message.NullMessager;
import org.yunshanmc.ycl.utils.reflect.MethodFinder;

import java.lang.annotation.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 简易命令类
 */
public abstract class SimpleCommand implements Command {
    
    private static final MethodFinder METHOD_FINDER = new MethodFinder().filterAnnotationPresent(CommandHandler.class)
                                                                        .filterPublic();
    private static final Lookup       LOOKUP        = MethodHandles.lookup();
    protected Messager messager;
    private SimpleCommandManager commandManager = null;
    private boolean                        valid;
    private String                         name;
    private MethodHandle                   commandHandler;
    private Class<? extends CommandSender> senderType;
    private int                            minArgsLength;
    private int                            maxArgsLength;
    
    /**
     * @param name
     *     命令名
     * @param argConverterManager
     *     参数转换器管理器
     */
    public SimpleCommand(String name, ArgConverterManager argConverterManager) {
        init(this, argConverterManager);
        this.name = name;
    }
    
    @Override
    public final String getName() {
        return this.name;
    }
    
    public boolean isValid() {
        return this.valid;
    }
    
    @Override
    public final boolean setValidity(boolean valid) {
        if (valid && !this.valid) {// 要设为有效且当前无效
            if (this.setValid()) {
                this.valid = true;
                return true;
            }
        } else if (!valid && this.valid) {// 要设为无效且当前有效
            if (this.setInvalid()) {
                this.valid = false;
                return true;
            }
        } else {// 要设置的有效性与当前有效性一致
            return true;
        }
        return false;
    }
    
    @Override
    public void setMessager(Messager messager) {
        this.messager = messager != null ? messager : NullMessager.getInstance();
    }
    
    @Override
    public final void execute(CommandSender sender, String... args) {
        if (this.senderType != null && !this.senderType.isInstance(sender)) {
            this.onSenderTypeDisallow(sender, args);
            return;
        } else if (args.length > this.maxArgsLength) {
            this.onTooManyArgs(sender, args);
            return;
        } else if (args.length < this.minArgsLength) {
            this.onTooLittleArgs(sender, args);
            return;
        }
        if (args.length < this.maxArgsLength) {
            args = Arrays.copyOf(args, this.maxArgsLength);
        }
        this.executeCommand(sender, args);
    }
    
    /**
     * 将命令设为有效
     *
     * @return 是否设置成功
     */
    protected boolean setValid() {
        return true;
    }
    
    /**
     * 将命令设为无效
     *
     * @return 是否设置成功
     */
    protected boolean setInvalid() {
        return true;
    }
    
    protected void executeCommand(CommandSender sender, String... args) {
        try {
            this.commandHandler.invoke(sender, args);
        } catch (ArgConverterFailException e) {
            this.onArgConvertFail(sender, e.getArg(), e.getConvertTo(), args);
        } catch (WrongMethodTypeException | ClassCastException e) {// 该项错误不应该出现
            throw new Error("error", e);
        } catch (Throwable e) {// 该项为命令处理方法抛出的异常
            ExceptionUtils.handle(e);
        }
    }
    
    /**
     * 向指定CommandSender显示命令帮助
     *
     * @param sender
     *     要显示命令帮助的CommandSender
     */
    protected void showHelp(CommandSender sender) {
        this.commandManager.showHelp(sender, this.name);
    }
    
    /**
     * 当命令发生者的类型不符合命令处理器的要求时会被调用
     *
     * @param sender
     *     命令发生者
     * @param args
     *     实际命令参数
     */
    protected void onSenderTypeDisallow(CommandSender sender, String... args) {
        this.messager.info(sender, "command.restrictSenderType." + this.senderType.getName().replace('.', '-'));
    }
    
    /**
     * 当 <b>命令参数个数</b><code> &lt; </code><b>必填参数个数</b> 时会被调用
     *
     * @param sender
     *     命令发生者
     * @param args
     *     实际命令参数
     */
    protected void onTooLittleArgs(CommandSender sender, String... args) {
        this.showHelp(sender);
    }
    
    /**
     * 当 <b>命令参数个数</b><code> &gt; </code><b>必填参数个数+可选参数个数</b> 时会被调用
     *
     * @param sender
     *     命令发生者
     * @param args
     *     实际命令参数
     */
    protected void onTooManyArgs(CommandSender sender, String... args) {
        this.showHelp(sender);
    }
    
    /**
     * 当参数转换器转换参数失败时会被调用
     * <p>
     * 当有参数转换失败后会立即调用此方法，不会继续尝试转换后续参数
     *
     * @param sender
     *     命令发生者
     * @param arg
     *     转换失败的参数
     * @param convertTo
     *     转换失败的参数应该被转换成的类型
     * @param args
     *     实际命令参数
     */
    protected void onArgConvertFail(CommandSender sender, String arg, Class<?> convertTo, String... args) {
        this.showHelp(sender);
    }
    
    private static void init(SimpleCommand command, ArgConverterManager converterManager) {
        Class<? extends SimpleCommand> cls = command.getClass();
        
        Method handler = METHOD_FINDER.findMethod(cls);
        if (handler == null) {
            throw new SimpleCommandInitFailException(command, "none valid handler method in class" + cls.getName());
        }
        
        MethodHandle targetHandler;
        try {
            handler.setAccessible(true);
            targetHandler = LOOKUP.unreflect(handler).bindTo(command);
        } catch (IllegalAccessException e) {// 此异常不会出现
            e.printStackTrace();
            throw new SimpleCommandInitFailException(command, "error");
        }
        
        CommandHandler handlerAnn = handler.getAnnotation(CommandHandler.class);
        Class<?>[] params = handler.getParameterTypes();
        
        if (handlerAnn.needSender()) {// 判断是否需要sender
            if (params.length == 0 || !CommandSender.class.isAssignableFrom(params[0])) {
                // 命令处理方法的第一个参数不是CommandSender类型或其子类型
                throw new SimpleCommandInitFailException(command,
                                                         "the first arg is not receiver a CommandSender instance");
            }
            command.senderType = params[0].asSubclass(CommandSender.class);
            params = Arrays.copyOfRange(params, 1, params.length);
        }
        
        if (params.length > 0) {
            MethodHandle[] filters;
            List<ArgConverter<?>> converters = Arrays.asList(converterManager.getArgConverters(params));
            Collection<MethodHandle> filterList = Collections2.transform(converters, ArgConverter::toMethodHandle);
            filters = filterList.toArray(new MethodHandle[filterList.size()]);
            if (handlerAnn.needSender()) {
                targetHandler = MethodHandles.filterArguments(targetHandler, 1, filters);
            } else {
                targetHandler = MethodHandles.filterArguments(targetHandler, 0, filters);
                targetHandler = MethodHandles.dropArguments(targetHandler, 0, CommandSender.class);
            }
            targetHandler = targetHandler.asSpreader(String[].class, filters.length);
        }
        command.maxArgsLength = params.length;
        int optionIdx = -1;
        Annotation[][] anns = handler.getParameterAnnotations();
        for (int i = 0; i < anns.length; i++) {
            for (Annotation ann : anns[i]) {
                if (OptionalArgStart.class.isInstance(ann)) optionIdx = i;
            }
        }
        if (handlerAnn.needSender()) {
            optionIdx = optionIdx - 1;
        }
        if (optionIdx >= 0) {
            command.minArgsLength = optionIdx;
        } else {
            command.minArgsLength = command.maxArgsLength;
        }
        command.commandHandler = targetHandler;
    }
    
    /**
     * 标记命令处理器
     */
    @Target(ElementType.METHOD)
    @Retention(value = RetentionPolicy.RUNTIME)
    protected @interface CommandHandler {
        
        /**
         * 是否需要传参的时候在第一个参数传递命令发送者，默认为false
         * <p>
         * 该方法设为true的时候必须保证命令处理方法的第一个参数的类型是CommandSender或其子类
         *
         * @return 需要返回true，不需要返回false
         */
        boolean needSender() default false;
    }
    
    /**
     * 标记可选参数的开始
     */
    @Target(ElementType.PARAMETER)
    @Retention(value = RetentionPolicy.RUNTIME)
    protected @interface OptionalArgStart {
        
    }
    
    /**
     * 简易命令初始化异常失败
     * <p>
     * 作者：YunShan<br>
     * 创建日期：2016年6月30日
     */
    public static class SimpleCommandInitFailException extends RuntimeException {
        
        private static final long serialVersionUID = 1L;
        
        /**
         * 未成功初始化的命令
         */
        private final SimpleCommand command;
        
        /**
         * @param command
         *     未成功初始化的命令
         * @param reason
         *     未成功初始化的原因
         */
        public SimpleCommandInitFailException(SimpleCommand command, String reason) {
            super(command.getName() + ":" + reason);
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
