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
    
    private ReadOnlyConfiguration              langConfig;
    private ReadOnlyConfiguration              userLangCfg;
    protected final Map<String, MessageFormat> formatCache = Maps.newHashMap();
    
    public StandardMessageManager(ResourceManager resourceManager, LocaleManager localeManager) {
        this.resourceManager = resourceManager;
        this.localeManager = localeManager;
        MISSING_LANGUAGE = new MessageFormat("missing language:{0}");
        this.setup();
    }
    
    /**
     * 初始化
     */
    protected void setup() {
        MISSING_LANGUAGE = this.getMessageFormat("message.missingLanguage");
    }
    
    @Override
    public String getMessage(String key, Object... args) {
        MessageFormat format = this.getMessageFormat(key);
        return format.format(args);
    }
    
    @Override
    public Messager createMessager() {
        return new DefaultMessager(this);
    }
    
    @Override
    public MessageFormat getMessageFormat(String key) {
        MessageFormat format = this.formatCache.get(key);
        if (format == null) format = this.getUserMessageFormat(key);
        if (format == null) {
            ReadOnlyConfiguration cfg = this.getLanguageConfig(key);
            if (cfg == null) return this.getMissingLanguageFormat(key);
            String msg = cfg.getString(key);
            if (msg == null) return this.getMissingLanguageFormat(key);
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            format = new MessageFormat(msg, this.localeManager.getFormatLocale());
            this.formatCache.put(key, format);
        }
        return format;
    }
    
    /**
     * 获取 无法找到本地化信息时的默认信息
     * 
     * @param key
     *            本地化信息的键
     * @return 无法找到本地化信息时的默认信息
     */
    protected MessageFormat getMissingLanguageFormat(String key) {
        return new MessageFormat(MISSING_LANGUAGE.format(new Object[] { key }));
    }
    
    /**
     * 获取自身语言文件配置
     * 
     * @param key 本地化信息的键
     * @return 自身语言文件配置
     */
    protected ReadOnlyConfiguration getLanguageConfig(String key) {
        if (this.langConfig == null) {
            Resource res = this.resourceManager.getSelfResource(MESSAGE_RES_PATH + "message.yml");
            if (res == null) return null;
            this.langConfig = ReadOnlyConfiguration.loadConfiguration(res.getInputStream());
        }
        return this.langConfig;
    }
    
    /**
     * 获取用户的语言文件配置
     * 
     * @return 用户的语言文件配置
     */
    protected ReadOnlyConfiguration getUserLanguageConfig() {
        if (this.userLangCfg == null) {
            Resource res = this.resourceManager.getFileResource("message.yml");
            if (res == null) return null;
            this.userLangCfg = ReadOnlyConfiguration.loadConfiguration(res.getInputStream());
        }
        return this.userLangCfg;
    }
    
    /**
     * 获取用户定义的信息的MessageFormat对象
     * 
     * @param key
     *            信息对应的键
     * @return 信息的MessageFormat对象
     */
    protected MessageFormat getUserMessageFormat(String key) {
        MessageFormat format = this.formatCache.get(key);
        if (format == null) {
            ReadOnlyConfiguration cfg = this.getUserLanguageConfig();
            if (cfg == null) return null;
            String msg = cfg.getString(key);
            if (msg == null) return null;
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            format = new MessageFormat(msg, this.localeManager.getFormatLocale());
            this.formatCache.put(key, format);
        }
        return format;
    }
}
