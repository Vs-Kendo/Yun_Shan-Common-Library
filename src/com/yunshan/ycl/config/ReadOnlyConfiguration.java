package com.yunshan.ycl.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.yunshan.ycl.util.IOUtils;

/**
 * 只读配置节点
 */
public class ReadOnlyConfiguration {
    
    protected ConfigurationSection config;
    
    /**
     * @param cfg
     *        配置节点
     */
    public ReadOnlyConfiguration(YamlConfiguration cfg) {
        this.config = cfg;
    }
    
    protected ReadOnlyConfiguration(ConfigurationSection cfg) {
        this.config = cfg;
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#get(java.lang. String,
     *      java.lang.Object)
     */
    public Object get(String path, Object def) {
        return this.config.get(path, def);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#get(java.lang. String)
     */
    public Object get(String path) {
        return this.config.get(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getBoolean(java.
     *      lang.String, boolean)
     */
    public boolean getBoolean(String path, boolean def) {
        return this.config.getBoolean(path, def);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getBoolean(java.
     *      lang.String)
     */
    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getBooleanList(
     *      java.lang.String)
     */
    public List<Boolean> getBooleanList(String path) {
        return this.config.getBooleanList(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getByteList(java.
     *      lang.String)
     */
    public List<Byte> getByteList(String path) {
        return this.config.getByteList(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getCharacterList(
     *      java.lang.String)
     */
    public List<Character> getCharacterList(String path) {
        return this.config.getCharacterList(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getColor(java.
     *      lang.String, org.bukkit.Color)
     */
    public Color getColor(String path, Color def) {
        return this.config.getColor(path, def);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getColor(java.
     *      lang.String)
     */
    public Color getColor(String path) {
        return this.config.getColor(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#
     *      getConfigurationSection(java.lang.String)
     */
    public ReadOnlyConfiguration getReadOnlyConfiguration(String path) {
        return new ReadOnlyConfiguration(this.config.getConfigurationSection(path));
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getCurrentPath()
     */
    public String getCurrentPath() {
        return this.config.getCurrentPath();
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getDefaultSection ()
     */
    public ConfigurationSection getDefaultSection() {
        return this.config.getDefaultSection();
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getDouble(java.
     *      lang.String, double)
     */
    public double getDouble(String path, double def) {
        return this.config.getDouble(path, def);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getDouble(java.
     *      lang.String)
     */
    public double getDouble(String path) {
        return this.config.getDouble(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getDoubleList(
     *      java.lang.String)
     */
    public List<Double> getDoubleList(String path) {
        return this.config.getDoubleList(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getFloatList(java
     *      .lang.String)
     */
    public List<Float> getFloatList(String path) {
        return this.config.getFloatList(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getInt(java.lang.
     *      String, int)
     */
    public int getInt(String path, int def) {
        return this.config.getInt(path, def);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getInt(java.lang.
     *      String)
     */
    public int getInt(String path) {
        return this.config.getInt(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getIntegerList(
     *      java.lang.String)
     */
    public List<Integer> getIntegerList(String path) {
        return this.config.getIntegerList(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getItemStack(java
     *      .lang.String, org.bukkit.inventory.ItemStack)
     */
    public ItemStack getItemStack(String path, ItemStack def) {
        return this.config.getItemStack(path, def);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getItemStack(java
     *      .lang.String)
     */
    public ItemStack getItemStack(String path) {
        return this.config.getItemStack(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getKeys(boolean)
     */
    public Set<String> getKeys(boolean deep) {
        return this.config.getKeys(deep);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getList(java.lang
     *      .String, java.util.List)
     */
    public List<?> getList(String path, List<?> def) {
        return this.config.getList(path, def);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getList(java.lang
     *      .String)
     */
    public List<?> getList(String path) {
        return this.config.getList(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getLong(java.lang
     *      .String, long)
     */
    public long getLong(String path, long def) {
        return this.config.getLong(path, def);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getLong(java.lang
     *      .String)
     */
    public long getLong(String path) {
        return this.config.getLong(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getLongList(java.
     *      lang.String)
     */
    public List<Long> getLongList(String path) {
        return this.config.getLongList(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getMapList(java.
     *      lang.String)
     */
    public List<Map<?, ?>> getMapList(String path) {
        return this.config.getMapList(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getName()
     */
    public String getName() {
        return this.config.getName();
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getOfflinePlayer(
     *      java.lang.String, org.bukkit.OfflinePlayer)
     */
    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def) {
        return this.config.getOfflinePlayer(path, def);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getOfflinePlayer(
     *      java.lang.String)
     */
    public OfflinePlayer getOfflinePlayer(String path) {
        return this.config.getOfflinePlayer(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getParent()
     */
    public ConfigurationSection getParent() {
        return this.config.getParent();
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getRoot()
     */
    public Configuration getRoot() {
        return this.config.getRoot();
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getShortList(java
     *      .lang.String)
     */
    public List<Short> getShortList(String path) {
        return this.config.getShortList(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getString(java.
     *      lang.String, java.lang.String)
     */
    public String getString(String path, String def) {
        return this.config.getString(path, def);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getString(java.
     *      lang.String)
     */
    public String getString(String path) {
        return this.config.getString(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getStringList(
     *      java.lang.String)
     */
    public List<String> getStringList(String path) {
        return this.config.getStringList(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getValues( boolean)
     */
    public Map<String, Object> getValues(boolean deep) {
        return this.config.getValues(deep);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getVector(java.
     *      lang.String, org.bukkit.util.Vector)
     */
    public Vector getVector(String path, Vector def) {
        return this.config.getVector(path, def);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getVector(java.
     *      lang.String)
     */
    public Vector getVector(String path) {
        return this.config.getVector(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#isBoolean(java.
     *      lang.String)
     */
    public boolean isBoolean(String path) {
        return this.config.isBoolean(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#isColor(java.lang
     *      .String)
     */
    public boolean isColor(String path) {
        return this.config.isColor(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#
     *      isConfigurationSection(java.lang.String)
     */
    public boolean isConfigurationSection(String path) {
        return this.config.isConfigurationSection(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#isDouble(java.
     *      lang.String)
     */
    public boolean isDouble(String path) {
        return this.config.isDouble(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#isInt(java.lang.
     *      String)
     */
    public boolean isInt(String path) {
        return this.config.isInt(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#isItemStack(java.
     *      lang.String)
     */
    public boolean isItemStack(String path) {
        return this.config.isItemStack(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#isList(java.lang.
     *      String)
     */
    public boolean isList(String path) {
        return this.config.isList(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#isLong(java.lang.
     *      String)
     */
    public boolean isLong(String path) {
        return this.config.isLong(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#isOfflinePlayer(
     *      java.lang.String)
     */
    public boolean isOfflinePlayer(String path) {
        return this.config.isOfflinePlayer(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#isSet(java.lang.
     *      String)
     */
    public boolean isSet(String path) {
        return this.config.isSet(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#isString(java.
     *      lang.String)
     */
    public boolean isString(String path) {
        return this.config.isString(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#isVector(java.
     *      lang.String)
     */
    public boolean isVector(String path) {
        return this.config.isVector(path);
    }
    
    /**
     * 使用<b>UTF-8</b>编码从输入流读取配置
     * 
     * @param input
     *        含有配置信息的输入流
     * @return 只读配置
     * @see #loadConfiguration(InputStream, Charset)
     */
    public static ReadOnlyConfiguration loadConfiguration(InputStream input) {
        return loadConfiguration(input, StandardCharsets.UTF_8);
    }
    
    /**
     * 使用指定编码从输入流读取配置
     * 
     * @param input
     *        含有配置信息的输入流
     * @param charset
     *        配置的编码
     * @return 只读配置
     */
    public static ReadOnlyConfiguration loadConfiguration(InputStream input, Charset charset) {
        try {
            InputStreamReader reader = IOUtils.getInputStreamReader(input, charset);
            String content = IOUtils.readAll(reader);
            YamlConfiguration cfg = new YamlConfiguration();
            cfg.loadFromString(content);
            return new ReadOnlyConfiguration(cfg);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
}
