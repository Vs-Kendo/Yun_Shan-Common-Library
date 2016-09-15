package org.yunshanmc.ycl.command.simple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.yunshanmc.ycl.command.Command;
import org.yunshanmc.ycl.command.simple.ArgConverter.ArgConverterFailException;
import org.yunshanmc.ycl.message.Messager;
import org.yunshanmc.ycl.message.NullMessager;
import org.yunshanmc.ycl.utils.reflect.ReflectionUtils;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * 简易命令类
 */
public abstract class SimpleCommand implements Command {
    
    private boolean vaild;
    
    private String name;
    
    private MethodHandle                   commandHandler;
    private Class<? extends CommandSender> senderType;
    
    private int minArgsLength;
    private int maxArgsLength;
    
    protected Messager messager;
    
    /**
     * @param name
     *            命令名
     */
    public SimpleCommand(String name, ArgConverterManager argConverterManager) {
        init(this, argConverterManager);
        this.name = name;
    }
    
    @Override
    public final String getName() {
        return this.name;
    }
    
    @Override
    public boolean isVaild() {
        return this.vaild;
    }
    
    @Override
    public final boolean setValidity(boolean vaild) {
        if (vaild && !this.vaild) {// 要设为有效且当前无效
            if (this.setVaild()) {
                this.vaild = true;
                return true;
            }
        } else if (!vaild && this.vaild) {// 要设为无效且当前有效
            if (this.setInvaild()) {
                this.vaild = false;
                return true;
            }
        } else {// 要设置的有效性与当前有效性一致
            return true;
        }
        return false;
    }
    
    /**
     * 将命令设为有效
     * 
     * @return 是否设置成功
     */
    protected boolean setVaild() {
        return true;
    }
    
    /**
     * 将命令设为无效
     * 
     * @return 是否设置成功
     */
    protected boolean setInvaild() {
        return true;
    }
    
    @Override
    public void setMessager(Messager messager) {
        this.messager = messager != null ? messager : NullMessager.getInstance();
    }
    
    @Override
    public final boolean execute(CommandSender sender, String... args) {
        if (this.senderType != null && !this.senderType.isInstance(sender)) {
            return this.onSenderTypeDisallow(sender, args);
        } else if (args.length > this.maxArgsLength) {
            return this.onTooManyArgs(sender, args);
        } else if (args.length < this.minArgsLength) {
            return this.onTooLittleArgs(sender, args);
        }
        if (args.length < this.maxArgsLength) {
            args = Arrays.copyOf(args, this.maxArgsLength);
        }
        return this.executeCommand(sender, args);
    }
    
    protected boolean executeCommand(CommandSender sender, String... args) {
        try {
            return (boolean) this.commandHandler.invoke(sender, args);
        } catch (ArgConverterFailException e) {
            return this.onArgConvertFail(sender, e.getArg(), e.getConvertTo(), args);
        } catch (WrongMethodTypeException | ClassCastException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }
    
    /**
     * 当命令发生者的类型不符合命令处理器的要求时会被调用
     * 
     * @param sender
     *            命令发生者
     * @param args
     *            实际命令参数
     * @return 命令语法是否正确(返回false将显示命令的默认帮助，不需要显示则返回true)
     */
    protected boolean onSenderTypeDisallow(CommandSender sender, String... args) {
        this.messager.info(sender, "command.restrictSenderType."+ this.senderType.getName().replace('.', '-'));
        return true;
    }
    
    /**
     * 当 <b>命令参数个数</b><code> &lt; </code><b>必填参数个数</b> 时会被调用
     * 
     * @param sender
     *            命令发生者
     * @param args
     *            实际命令参数
     * @return 命令语法是否正确(返回false将显示命令的默认帮助，不需要显示则返回true)
     */
    protected boolean onTooLittleArgs(CommandSender sender, String... args) {
        return false;
    }
    
    /**
     * 当 <b>命令参数个数</b><code> &gt; </code><b>必填参数个数+可选参数个数</b> 时会被调用
     * 
     * @param sender
     *            命令发生者
     * @param args
     *            实际命令参数
     * @return 命令语法是否正确(返回false将显示命令的默认帮助，不需要显示则返回true)
     */
    protected boolean onTooManyArgs(CommandSender sender, String... args) {
        return false;
    }
    
    /**
     * 当参数转换器转换参数失败时会被调用
     * <p>
     * 当有参数转换失败后会立即调用此方法，不会继续尝试转换后续参数
     * 
     * @param sender
     *            命令发生者
     * @param arg
     *            转换失败的参数
     * @param convertTo
     *            转换失败的参数应该被转换成的类型
     * @param args
     *            实际命令参数
     * @return 返回false将显示命令的默认帮助，不需要显示则返回true
     */
    protected boolean onArgConvertFail(CommandSender sender, String arg, Class<?> convertTo, String... args) {
        return false;
    }
    
    private static void init(SimpleCommand command, ArgConverterManager converterManager) {
        Class<? extends SimpleCommand> cls = command.getClass();
        Method handler = ReflectionUtils.getFirstMethodByAnnotation(cls, CommandHandler.class);
        if (handler == null) {
            // 没有带命令处理器注解的方法
            throw new SimpleCommandInitFailExecption(command, "none handler");
        }
        if (handler.getReturnType() != boolean.class) {
            // 返回值不是boolean类型
            throw new SimpleCommandInitFailExecption(command, "return type not boolean");
        }
        CommandHandler handlerAnn = handler.getAnnotation(CommandHandler.class);
        Class<?>[] params = handler.getParameterTypes();
        Lookup lookup = MethodHandles.lookup();
        MethodHandle targetHandler = null;
        try {
            targetHandler = lookup.unreflect(handler).bindTo(command);
        } catch (IllegalAccessException e1) {
            // 方法不是public的
            throw new SimpleCommandInitFailExecption(command, "handler not public");
        }
        if (params.length > 0) {
            if (handlerAnn.needSender()) {
                if (!CommandSender.class.isAssignableFrom(params[0])) {
                    // 命令处理方法的第一个参数不是CommandSender类型或其子类型
                    throw new SimpleCommandInitFailExecption(command,
                            "the first arg is not receiver a CommandSender instance");
                }
                @SuppressWarnings("unchecked")
                Class<? extends CommandSender> tmp = (Class<? extends CommandSender>) params[0];
                command.senderType = tmp;
                params = Arrays.copyOfRange(params, 1, params.length);
            }
            MethodHandle[] filters = null;
            List<ArgConverter<?>> converters = Arrays.asList(converterManager.getArgConverters(params));
            Collection<MethodHandle> filterList = Collections2.transform(converters,
                    new Function<ArgConverter<?>, MethodHandle>() {
                        
                        @Override
                        public MethodHandle apply(ArgConverter<?> converter) {
                            return converter.toMethodHandle();
                        }
                    });
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
        int optionIdx = ReflectionUtils.findFirstAnnotationIndexInParameters(handler, OptionalArgStart.class);
        if (optionIdx != -1) {
            command.minArgsLength = optionIdx;
        } else {
            command.minArgsLength = command.maxArgsLength;
        }
        if (handlerAnn.needSender()) {// sender占了一个参数位置
            command.minArgsLength -= 1;
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
    public static class SimpleCommandInitFailExecption extends RuntimeException {
        
        private static final long serialVersionUID = 1L;
        
        /** 未成功初始化的命令 */
        private final SimpleCommand command;
        
        /**
         * @param command
         *            未成功初始化的命令
         */
        public SimpleCommandInitFailExecption(SimpleCommand command, String reason) {
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
