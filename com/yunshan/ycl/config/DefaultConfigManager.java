package com.yunshan.ycl.config;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.yunshan.ycl.resource.Resource;
import com.yunshan.ycl.resource.ResourceManager;

/**
 * 默认配置管理器
 * <p>
 * 作者：YunShan
 * <p>
 * 创建日期：2016年6月30日
 * <p>
 */
public class DefaultConfigManager implements ConfigManager {
    
    private static final Predicate<String> CONFIG_FILTER = new Predicate<String>() {
        
        // 配置文件过滤器
        @Override
        public boolean apply(String name) {
            return name.endsWith(".yml");
        }
    };
    
    private static final String PLUGIN_CONFIG_PATH = "config.yml";
    
    private final ResourceManager resourceManager;
    
    private ReadWriteConfiguration pluginConfig;
    
    private final Map<String, ReadWriteConfiguration> configChace = Maps.newHashMap();
    
    public DefaultConfigManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.reload();
    }
    
    /**
     * @see com.yunshan.ycl.config.ConfigManager#reload()
     */
    @Override
    public void reload() {
        this.configChace.clear();
        if (this.saveDefaultConfig(PLUGIN_CONFIG_PATH, PLUGIN_CONFIG_PATH, false)) {
            this.pluginConfig = this.getConfig(PLUGIN_CONFIG_PATH);
        } else {// 空配置
            this.pluginConfig = new ReadWriteConfiguration(new YamlConfiguration(), this);
        }
    }
    
    /**
     * @see com.yunshan.ycl.config.ConfigManager#getPluginConfig()
     */
    @Override
    public ReadOnlyConfiguration getPluginConfig() {
        return this.pluginConfig;
    }
    
    /**
     * @see com.yunshan.ycl.config.ConfigManager#getConfig(java.lang.String)
     */
    @Override
    public ReadWriteConfiguration getConfig(String filePath) {
        ReadWriteConfiguration cfg = this.configChace.get(filePath);
        if (cfg == null) {
            Resource res = this.resourceManager.getFileResource(filePath);
            if (res != null) {
                cfg = ReadWriteConfiguration.loadConfiguration(res.getInputStream(), this);
                this.configChace.put(filePath, cfg);
            }
        }
        return cfg;
    }
    
    /**
     * @see com.yunshan.ycl.config.ConfigManager#getReadOnlyConfig(java.lang.String)
     */
    @Override
    public ReadOnlyConfiguration getReadOnlyConfig(String filePath) {
        return this.getConfig(filePath);
    }
    
    /**
     * @see com.yunshan.ycl.config.ConfigManager#getAllConfigs(java.lang.String,
     *      boolean)
     */
    @Override
    public Map<String, ReadWriteConfiguration> getAllConfigs(String dirPath, boolean deep) {
        Map<String, Resource> ress = this.resourceManager.getFolderResources(dirPath, CONFIG_FILTER, deep);
        if (ress != null) {
            Map<String, ReadWriteConfiguration> configs = Maps.newHashMap();
            for (Entry<String, Resource> res : ress.entrySet()) {
                configs.put(res.getKey(),
                        ReadWriteConfiguration.loadConfiguration(res.getValue().getInputStream(), this));
            }
            return configs;
        }
        return null;
    }
    
    /**
     * @see com.yunshan.ycl.config.ConfigManager#getAllReadOnlyConfigs(java.lang.String,
     *      boolean)
     */
    @Override
    public Map<String, ReadOnlyConfiguration> getAllReadOnlyConfigs(String dirPath, boolean deep) {
        Map<String, ReadWriteConfiguration> configs = this.getAllConfigs(dirPath, deep);
        if (configs == null) return null;
        return Maps.transformValues(configs, new Function<ReadWriteConfiguration, ReadOnlyConfiguration>() {
            
            @Override
            public ReadOnlyConfiguration apply(ReadWriteConfiguration cfg) {
                return cfg;
            }
        });
    }
    
    /**
     * @see com.yunshan.ycl.config.ConfigManager#saveDefaultConfig(java.lang.String,
     *      java.lang.String, boolean)
     */
    @Override
    public boolean saveDefaultConfig(String pathInJar, String filePath, boolean force) {
        Resource res = this.resourceManager.getSelfResource(pathInJar);
        if (res != null) {
            return this.resourceManager.writeResource(filePath != null ? filePath : pathInJar, res.getInputStream(),
                    force);
        }
        return false;
    }
    
    /**
     * @see com.yunshan.ycl.config.ConfigManager#saveConfig(com.yunshan.ycl.config.ReadWriteConfiguration,
     *      java.lang.String)
     */
    @Override
    public boolean saveConfig(ReadWriteConfiguration cfg, String path) {
        return this.resourceManager.writeResource(path,
                new ByteArrayInputStream(cfg.saveToYaml().getBytes(Charsets.UTF_8)), true);
    }
}
