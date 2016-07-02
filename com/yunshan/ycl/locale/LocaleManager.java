package com.yunshan.ycl.locale;

import java.util.Locale;
import java.util.Objects;

/**
 * 本地化管理器
 */
public class LocaleManager {
    
    private final static Locale DEFAULT_LOCALE = Locale.CHINESE;
    
    // 当前区域
    private Locale display;
    private Locale format;
    
    public LocaleManager() {
        this(DEFAULT_LOCALE);
    }
    
    public LocaleManager(Locale display) {
        this(display, null);
    }
    
    public LocaleManager(Locale display, Locale format) {
        Objects.requireNonNull(display);
        format = format == null ? display : format;
        this.display = display;
        this.format = format;
    }
    
    protected void setDisplayLocale(Locale display) {
        Objects.requireNonNull(display);
        this.display = display;
    }
    
    protected void setFormatLocale(Locale format) {
        Objects.requireNonNull(format);
        this.format = format;
    }
    
    /**
     * 获取显示所使用的区域
     * 
     * @return 显示所使用的区域
     */
    public Locale getDisplayLocale() {
        return this.display;
    }
    
    /**
     * 获取格式化所使用的区域
     * 
     * @return 格式化所使用的区域
     */
    public Locale getFormatLocale() {
        return this.format;
    }
    
}
