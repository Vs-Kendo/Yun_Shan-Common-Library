package com.yunshan.ycl.message;

import java.text.MessageFormat;
import java.util.Map;

import com.google.common.collect.Maps;
import com.yunshan.ycl.config.ReadOnlyConfiguration;
import com.yunshan.ycl.locale.LocaleManager;
import com.yunshan.ycl.resource.Resource;
import com.yunshan.ycl.resource.ResourceManager;

/**
 * 分组的信息管理器
 * <p>
 * 此管理器使用键中第一次出现的<code>'.'</code>作为分组依据，其前面的字符为组别，后面的字符为实际文件中的键<br/>
 * 未出现<code>'.'</code>时将使用默认的分组：message
 */
public class GroupMessageManager extends StandardMessageManager {
    
    private static final char   GROUP_SEPARATOR = '.';
    private static final String DEFAULT_GROUP   = "message";
    
    private Map<String, ReadOnlyConfiguration> languageCache = Maps.newHashMap();
    
    public GroupMessageManager(ResourceManager resourceManager, LocaleManager localeManager) {
        super(resourceManager, localeManager);
    }
    
    @Override
    protected MessageFormat getMessageFormatFromCache(String key) {
        return super.getMessageFormatFromCache(toRealKey(key));
    }
    
    @Override
    protected ReadOnlyConfiguration getLanguageConfig(String key) {
        return this.getLanguageCache(key);
    }
    
    private ReadOnlyConfiguration getLanguageCache(String key) {
        ReadOnlyConfiguration cfg = this.languageCache.get(key);
        if (cfg == null) {
            Resource res = super.resourceManager.getSelfResource(MESSAGE_RES_PATH + toGroup(key) + ".yml");
            if (res == null) return null;
            cfg = ReadOnlyConfiguration.loadConfiguration(res.getInputStream());
            this.languageCache.put(key, cfg);
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
            return key.substring(idx - 1);
        } else {
            return key;
        }
    }
}
