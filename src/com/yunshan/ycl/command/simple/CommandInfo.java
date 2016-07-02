package com.yunshan.ycl.command.simple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标示命令类
 */
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    
    /**
     * 命令名，用于指定此命令的名字
     */
    String name();
}
