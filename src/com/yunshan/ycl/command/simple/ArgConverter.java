package com.yunshan.ycl.command.simple;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;

import com.yunshan.ycl.exception.NoneStackRuntimeException;

/**
 * 参数转换器
 */
public abstract class ArgConverter<T> {
    
    private static final Method mConvert = ArgConverter.class.getDeclaredMethods()[0];
    private static final Lookup LOOKUP   = MethodHandles.lookup();
    
    private final Class<T> convertTo;
    
    public ArgConverter(Class<T> convertTo) {
        this.convertTo = convertTo;
    }
    
    /**
     * 转换参数
     * <p>
     * 将字符串类型的参数转换成对象
     * 
     * @param arg
     *            参数
     * @return 转换后的对象
     */
    public final T convert(String arg) {
        if (arg == null) return this.getDefaultVaule();
        try {
            T res = this.convertArg(arg);
            if (res == null) throw new ArgConverterFailException(arg, this.convertTo);
            return res;
        } catch (ArgConverterFailException e) {
            throw e;
        } catch (Throwable t) {
            throw new ArgConverterFailException(arg, this.convertTo, t);
        }
    }
    
    public T getDefaultVaule() {
        return null;
    }
    
    public final MethodHandle toMethodHandle() {
        try {
            MethodHandle handle = LOOKUP.unreflect(mConvert).bindTo(this);
            handle = handle.asType(handle.type().changeReturnType(this.convertTo));
            return handle;
        } catch (IllegalAccessException e) {
            // 此异常不会出现
            return null;
        }
    }
    
    /**
     * 转换参数
     * <p>
     * 将字符串类型的参数转换成对象<br>
     * <b>注意：对于相同的字符串，应返回相同的结果</b>
     * 
     * @param arg
     *            参数
     * @return 转换后的对象
     */
    protected abstract T convertArg(String arg);
    
    public static class ArgConverterFailException extends NoneStackRuntimeException {
        
        private static final long serialVersionUID = 1L;
        
        private final String   arg;
        private final Class<?> convertTo;
        
        public ArgConverterFailException(String arg, Class<?> convertTo) {
            this(arg, convertTo, null);
        }
        
        public ArgConverterFailException(String arg, Class<?> convertTo, Throwable cause) {
            super(cause);
            this.arg = arg;
            this.convertTo = convertTo;
        }
        
        public String getArg() {
            return this.arg;
        }
        
        public Class<?> getConvertTo() {
            return this.convertTo;
        }
        
    }
}
