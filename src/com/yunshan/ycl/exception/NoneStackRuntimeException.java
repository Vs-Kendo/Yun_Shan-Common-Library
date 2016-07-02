package com.yunshan.ycl.exception;

/**
 * 没有记录栈路径的运行期异常
 */
public abstract class NoneStackRuntimeException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public NoneStackRuntimeException() {
    }
    
    public NoneStackRuntimeException(Throwable cause) {
        super(cause);
    }
    
    /**
     * 直接返回，将不会记录栈路径
     * 
     * @return 异常本体
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
    
}
