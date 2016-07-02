package com.yunshan.ycl.command.simple;

import java.util.Map;
import java.util.Objects;

import com.google.common.collect.Maps;
import com.yunshan.ycl.exception.NoneStackException;

/**
 * 参数转换器管理器
 */
public class ArgConverterManager {
    
    private static final Map<Class<?>, ArgConverter<?>> INTERNAL_CONVERTERS = Maps.newHashMap();
    
    static {
        INTERNAL_CONVERTERS.put(int.class, new ArgConverter<Integer>(int.class) {
            
            @Override
            public Integer convertArg(String arg) {
                return Integer.valueOf(arg);
            }
            
            @Override
            public Integer getDefaultVaule() {
                return Integer.valueOf(-1);
            }
        });
        INTERNAL_CONVERTERS.put(String.class, new ArgConverter<String>(String.class) {
            
            @Override
            public String convertArg(String arg) {
                return arg;
            }
        });
    }
    
    private static ArgConverterManager instance;
    
    private Map<Class<?>, ArgConverter<?>> argConverters = Maps.newHashMap();
    
    protected ArgConverterManager() {
        // 禁止外部实例化，允许子类实例化  
        this.argConverters.putAll(INTERNAL_CONVERTERS);
    }
    
    /**
     * 获取参数转换器管理器实例
     * 
     * @return 参数转换器管理器实例
     */
    public static ArgConverterManager getInstance() {
        if (instance == null) {
            instance = new ArgConverterManager();
        }
        return instance;
    }
    
    /**
     * 注册参数转换器
     * <p>
     * 若已存在结果类型为cls的参数转换器则会注册失败
     * 
     * @param cls
     *            参数转换器的结果类型
     * @param converter
     *            参数转换器
     * @return 成功注册返回true，否则返回false
     */
    public <T> boolean registerArgConverter(Class<T> cls, ArgConverter<T> converter) {
        if (!argConverters.containsKey(cls)) {
            argConverters.put(cls, converter);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 获取指定类型的参数转换器
     * 
     * @param cls
     *            参数转换器的转换结果类型
     * @return 转换结果为cls类对象的参数转换器
     */
    @SuppressWarnings("unchecked")
    public <T> ArgConverter<T> getArgConverter(Class<T> cls) {
        return (ArgConverter<T>) argConverters.get(cls);
    }
    
    /**
     * 获取所有指定类型的参数转换器
     * 
     * @param clss
     *            参数转换器的转换结果类型
     * @return 所有指定类型的参数转换器
     * @throws NullPointerException
     *             若clss参数中有某个元素为null则会抛出此异常
     * @throws MissingArgConverterExecption
     *             若无法找到某个参数的参数转换器则会抛出此异常
     * @see {@link #getArgConverter(Class)}
     */
    public ArgConverter<?>[] getArgConverters(Class<?>[] clss) throws MissingArgConverterExecption {
        ArgConverter<?>[] handles = new ArgConverter<?>[clss.length];
        for (int i = 0; i < clss.length; i++) {
            Class<?> cls = clss[i];
            Objects.requireNonNull(cls);
            ArgConverter<?> handle = this.getArgConverter(cls);
            if (handle == null) throw new MissingArgConverterExecption(cls);
            handles[i] = handle;
        }
        return handles;
    }
    
    /**
     * 未找到参数转换器异常
     * <p>
     * 该异常是在{@link ArgConverterManager#getArgConverters(Class[])}方法中被抛出的
     */
    public static class MissingArgConverterExecption extends NoneStackException {
        
        private static final long serialVersionUID = 1L;
        
        private final Class<?> convertTo;
        
        /**
         * @param convertTo
         *            参数转换器的结果类型
         */
        private MissingArgConverterExecption(Class<?> convertTo) {
            this.convertTo = convertTo;
        }
        
        /**
         * 获取未找到的参数转换器应有的结果类型
         * 
         * @return 参数转换器的结果类型
         */
        public Class<?> getConvertTo() {
            return this.convertTo;
        }
    }
}
