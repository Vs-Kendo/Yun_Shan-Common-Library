package com.yunshan.ycl.exception;

import com.google.common.base.Function;

/**
 * 异常处理器
 * <p>
 * 作者：YunShan<br>
 * 创建日期：2016年7月1日
 */
public abstract class ExceptionHandler implements Function<Throwable, Object> {
    
    /**
     * 使用{@link #handle(Throwable)}
     * 
     * @see #handle(Throwable)
     */
    @Deprecated
    @Override
    public final Object apply(Throwable t) {
        this.handle(t);
        return null;
    }
    
    /**
     * 处理异常
     * 
     * @param t
     *            要处理的异常
     */
    public abstract void handle(Throwable t);
    
}
