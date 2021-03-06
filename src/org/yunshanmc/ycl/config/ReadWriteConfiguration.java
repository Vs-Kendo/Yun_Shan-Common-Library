package org.yunshanmc.ycl.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yunshanmc.ycl.exception.ExceptionUtils;
import org.yunshanmc.ycl.utils.IOUtils;

/**
 * 可读写的配置
 */
public class ReadWriteConfiguration extends ReadOnlyConfiguration {
    
    private final ConfigManager configManager;
    
    public ReadWriteConfiguration(YamlConfiguration cfg, ConfigManager configManager) {
        this((ConfigurationSection) cfg, configManager, cfg.saveToString());
    }
    
    protected ReadWriteConfiguration(ConfigurationSection cfg, ConfigManager configManager, String contents) {
        super(cfg, contents);
        this.configManager = configManager;
    }
    
    /**
     * @param path
     *            Path of the value to set.
     * @param value
     *            Value to set the default to.
     * @see org.bukkit.configuration.ConfigurationSection#addDefault(java.lang.String,
     *      java.lang.Object)
     */
    public void addDefault(String path, Object value) {
        super.config.addDefault(path, value);
    }
    
    /**
     * @param path
     *            Path to create the section at.
     * @param map
     *            The values to used.
     * @see org.bukkit.configuration.ConfigurationSection#createSection(java.lang.String,
     *      java.util.Map)
     * @return Newly created section
     */
    public ConfigurationSection createSection(String path, Map<?, ?> map) {
        return super.config.createSection(path, map);
    }
    
    /**
     * @param path
     *            Path to create the section at.
     * @see org.bukkit.configuration.ConfigurationSection#createSection(java.lang.String)
     * @return Newly created section
     */
    public ReadWriteConfiguration createSection(String path) {
        return new ReadWriteConfiguration(super.config.createSection(path), this.configManager, super.contents);
    }
    
    /**
     * @param path
     *            Path of the ConfigurationSection to get.
     * @see org.bukkit.configuration.ConfigurationSection#getConfigurationSection(java.lang.String)
     * @return Requested ConfigurationSection.
     */
    public ReadWriteConfiguration getReadWriteConfiguration(String path) {
        ConfigurationSection section = super.config.getConfigurationSection(path);
        if (section == null) return null;
        return new ReadWriteConfiguration(section, this.configManager, super.contents);
    }
    
    /**
     * @param path
     *            Path of the object to set.
     * @param value
     *            New value to set the path to.
     * @see org.bukkit.configuration.ConfigurationSection#set(java.lang.String,
     *      java.lang.Object)
     */
    public void set(String path, Object value) {
        super.config.set(path, value);
    }
    
    /**
     * 保存配置文件，当文件已存在时会覆盖原文件
     * 
     * @param path
     *            要保存到的路径(相对路径)
     * @return 保存成功返回true，写入失败返回false
     * @see org.yunshanmc.ycl.config.DefaultConfigManager#saveConfig(ReadWriteConfiguration,String)
     */
    public boolean save(String path) {
        return this.configManager.saveConfig(this, path);
    }
    
    /**
     * 将配置序列化为Yaml格式的字符串
     * 
     * @return 序列化后的字符串
     */
    public String saveToYaml() {
        return ((YamlConfiguration) super.config).saveToString();
    }
    
    @Override
    public String toString() {
        return this.saveToYaml();
    }
    
    /**
     * 使用<b>UTF-8</b>编码从输入流读取配置
     * 
     * @param input
     *            含有配置信息的输入流
     * @param configManager
     *            配置管理器，用于可读写配置的配置写入
     * @return 可读写的配置，读取失败返回null
     * @see #loadConfiguration(InputStream, Charset, ConfigManager)
     */
    public static ReadWriteConfiguration loadConfiguration(InputStream input, ConfigManager configManager) {
        return loadConfiguration(input, StandardCharsets.UTF_8, configManager);
    }
    
    /**
     * 从输入流读取配置
     * 
     * @param input
     *            含有配置信息的输入流
     * @param charset
     *            配置的编码
     * @param configManager
     *            配置管理器，用于可读写配置的配置写入
     * @return 可读写的配置，读取失败返回null
     */
    public static ReadWriteConfiguration loadConfiguration(InputStream input, Charset charset,
            ConfigManager configManager) {
        try {
            InputStreamReader reader = IOUtils.getInputStreamReader(input, charset);
            String content = IOUtils.readAll(reader);
            YamlConfiguration cfg = new YamlConfiguration();
            cfg.loadFromString(content);
            return new ReadWriteConfiguration(cfg, configManager);
        } catch (IOException | InvalidConfigurationException e) {
            ExceptionUtils.handle(e);
            return null;
        }
    }
}
