package org.yunshanmc.ycl.message;

import java.text.MessageFormat;
import java.util.Map;

import org.bukkit.ChatColor;
import org.yunshanmc.ycl.config.ReadOnlyConfiguration;
import org.yunshanmc.ycl.locale.LocaleManager;
import org.yunshanmc.ycl.resource.Resource;
import org.yunshanmc.ycl.resource.ResourceManager;

import com.google.common.collect.Maps;

/**
 * 分组的信息管理器
 * <p>
 * 此管理器使用键中第一次出现的<code>'.'</code>作为分组依据，其前面的字符为组别，后面的字符为实际文件中的键<br>
 * 未出现<code>'.'</code>时将使用默认的分组：message
 */
public class GroupMessageManager extends StandardMessageManager {
    
    private static final char   GROUP_SEPARATOR = '.';
    private static final String DEFAULT_GROUP   = "message";
    
    private Map<String, ReadOnlyConfiguration> languageCache;
    
    public GroupMessageManager(ResourceManager resourceManager, LocaleManager localeManager) {
        super(resourceManager, localeManager);
    }
    
    @Override
    protected void setup() {
        this.languageCache = Maps.newHashMap();
        super.setup();
    }
    
    @Override
    public MessageFormat getMessageFormat(String key) {
        MessageFormat format = this.formatCache.get(key);
        if (format == null) format = this.getUserMessageFormat(key);
        if (format == null) {
            ReadOnlyConfiguration cfg = this.getLanguageConfig(key);
            if (cfg == null) return this.getMissingLanguageFormat(key);
            String msg = cfg.getString(toRealKey(key)/* 转换为实际配置中的key */);
            if (msg == null) return this.getMissingLanguageFormat(key);
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            format = new MessageFormat(msg, this.localeManager.getFormatLocale());
            this.formatCache.put(key, format);
        }
        return format;
    }
    
    @Override
    protected ReadOnlyConfiguration getLanguageConfig(String key) {
        String group = toGroup(key);
        ReadOnlyConfiguration cfg = this.languageCache.get(group);
        if (cfg == null) {
            Resource res = super.resourceManager.getSelfResource(MESSAGE_RES_PATH + group + ".yml");
            if (res == null) return null;
            cfg = ReadOnlyConfiguration.loadConfiguration(res.getInputStream());
            this.languageCache.put(group, cfg);
        }
        return cfg;
    }
    
    private static String toGroup(String key) {
        int idx = key.indexOf(GROUP_SEPARATOR);
        if (idx > 0) {
            return key.substring(0, idx);
        } else {
            return DEFAULT_GROUP;
        }
    }
    
    private static String toRealKey(String key) {
        int idx = key.indexOf(GROUP_SEPARATOR);
        if (idx > 0) {
            return key.substring(idx + 1);
        } else {
            return key;
        }
    }
}
