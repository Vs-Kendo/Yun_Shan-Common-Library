package com.yunshan.ycl.message;

import java.text.MessageFormat;
import java.util.Map;

import org.bukkit.ChatColor;

import com.google.common.collect.Maps;
import com.yunshan.ycl.config.ReadOnlyConfiguration;
import com.yunshan.ycl.locale.LocaleManager;
import com.yunshan.ycl.resource.Resource;
import com.yunshan.ycl.resource.ResourceManager;

/**
 * 标准信息管理器
 */
public class StandardMessageManager implements MessageManager {
    
    protected static final String MESSAGE_RES_PATH = "messages/";
    
    protected MessageFormat MISSING_LANGUAGE;
    
    protected final ResourceManager resourceManager;
    protected final LocaleManager   localeManager;
    
    private ReadOnlyConfiguration              languageConfig;
    protected final Map<String, MessageFormat> formatCache = Maps.newHashMap();
    
    public StandardMessageManager(ResourceManager resourceManager, LocaleManager localeManager) {
        this.resourceManager = resourceManager;
        this.localeManager = localeManager;
        this.setup();
    }
    
    /**
     * 初始化
     */
    protected void setup() {
        MISSING_LANGUAGE = this.getMessageFormat("message.missingLanguage");
        if (MISSING_LANGUAGE == null) {
            MISSING_LANGUAGE = new MessageFormat("missing language:{0}");
        }
    }
    
    @Override
    public String getMessage(String key, Object... args) {
        MessageFormat format = this.getMessageFormat(key);
        if (format == null) {
            return MISSING_LANGUAGE.format(new Object[] { key });
        }
        return format.format(args);
    }
    
    @Override
    public Messager createMessager() {
        return new DefaultMessager(this);
    }
    
    @Override
    public MessageFormat getMessageFormat(String key) {
        MessageFormat format = this.formatCache.get(key);
        if (format == null) {
            ReadOnlyConfiguration cfg = this.getLanguageConfig(key);
            if (cfg == null) return MISSING_LANGUAGE;
            String msg = cfg.getString(key);
            if (msg == null) return MISSING_LANGUAGE;
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            format = new MessageFormat(msg, this.localeManager.getFormatLocale());
            this.formatCache.put(key, format);
        }
        return format;
    }
    
    /**
     * 获取语言文件配置
     */
    protected ReadOnlyConfiguration getLanguageConfig(String key) {
        if (this.languageConfig == null) {
            Resource res = this.resourceManager.getSelfResource(MESSAGE_RES_PATH + "message.yml");
            if (res == null) return null;
            this.languageConfig = ReadOnlyConfiguration.loadConfiguration(res.getInputStream());
        }
        return this.languageConfig;
    }
}
