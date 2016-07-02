package com.yunshan.ycl.exception;

/**
 * 异常工具类
 */
public final class ExceptionUtils {
    
    private ExceptionUtils() {
    }
    
    private static ExceptionHandler exceptionHandler = new ExceptionHandler() {
        
        @Override
        public void handle(Throwable t) {//默认实现
            t.printStackTrace();
        }
    };
    
    /**
     * 设置异常处理器
     * 
     * @param 要使用的异常处理器
     */
    public static void setExceptionHandler(ExceptionHandler handler) {
        if (handler != null) ExceptionUtils.exceptionHandler = handler;
    }
    
    /**
     * 处理异常
     * 
     * @param t
     *        要处理的异常
     * @see ExceptionHandler#handle(Throwable)
     */
    public static void handle(Throwable t) {
        exceptionHandler.handle(t);
    }
    
}
