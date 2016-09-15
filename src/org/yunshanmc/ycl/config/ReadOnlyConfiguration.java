package org.yunshanmc.ycl.config;

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
import org.yunshanmc.ycl.exception.ExceptionUtils;
import org.yunshanmc.ycl.utils.IOUtils;

/**
 * 只读配置节点
 */
public class ReadOnlyConfiguration {
    
    protected ConfigurationSection config;
    
    protected final String contents;
    
    /**
     * @param cfg
     *            配置节点
     */
    public ReadOnlyConfiguration(YamlConfiguration cfg) {
        this.config = cfg;
        this.contents = cfg.saveToString();
    }
    
    protected ReadOnlyConfiguration(ConfigurationSection cfg, String contents) {
        this.config = cfg;
        this.contents = contents;
    }
    
    /**
     * @param path
     *            Path of the Object to get.
     * @param def
     *            The default value to return if the path is not found.
     * @see org.bukkit.configuration.ConfigurationSection#get(java.lang.String,
     *      java.lang.Object)
     * @return Requested Object.
     */
    public Object get(String path, Object def) {
        return this.config.get(path, def);
    }
    
    /**
     * @param path
     *            Path of the Object to get.
     * @see org.bukkit.configuration.ConfigurationSection#get(java.lang.String)
     * @return Requested Object.
     */
    public Object get(String path) {
        return this.config.get(path);
    }
    
    /**
     * @param path
     *            Path of the boolean to get.
     * @param def
     *            The default value to return if the path is not found.
     * @see org.bukkit.configuration.ConfigurationSection#getBoolean(java.lang.String,
     *      boolean)
     * @return Requested boolean.
     */
    public boolean getBoolean(String path, boolean def) {
        return this.config.getBoolean(path, def);
    }
    
    /**
     * @param path
     *            Path of the boolean to get.
     * @see org.bukkit.configuration.ConfigurationSection#getBoolean(java.lang.String)
     * @return Requested boolean.
     */
    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }
    
    /**
     * @param path
     *            Path of the List to get.
     * @see org.bukkit.configuration.ConfigurationSection#getBooleanList(java.lang.String)
     * @return Requested List of Boolean.
     */
    public List<Boolean> getBooleanList(String path) {
        return this.config.getBooleanList(path);
    }
    
    /**
     * @param path
     *            Path of the List to get.
     * @see org.bukkit.configuration.ConfigurationSection#getByteList(java.lang.String)
     * @return Requested List of Byte.
     */
    public List<Byte> getByteList(String path) {
        return this.config.getByteList(path);
    }
    
    /**
     * @param path
     *            Path of the List to get.
     * @see org.bukkit.configuration.ConfigurationSection#getCharacterList(java.lang.String)
     * @return Requested List of Character.
     */
    public List<Character> getCharacterList(String path) {
        return this.config.getCharacterList(path);
    }
    
    /**
     * @param path
     *            Path of the Color to get.
     * @param def
     *            The default value to return if the path is not found.
     * @see org.bukkit.configuration.ConfigurationSection#getColor(java.lang.String,
     *      org.bukkit.Color)
     * @return Requested Color.
     */
    public Color getColor(String path, Color def) {
        return this.config.getColor(path, def);
    }
    
    /**
     * @param path
     *            Path of the Color to get.
     * @see org.bukkit.configuration.ConfigurationSection#getColor(java.lang.String)
     * @return Requested Color.
     */
    public Color getColor(String path) {
        return this.config.getColor(path);
    }
    
    /**
     * @param path
     *            Path of the ReadOnlyConfiguration to get.
     * @see org.bukkit.configuration.ConfigurationSection#getConfigurationSection(java.lang.String)
     * @return Requested ReadOnlyConfiguration.
     */
    public ReadOnlyConfiguration getReadOnlyConfiguration(String path) {
        ConfigurationSection section = this.config.getConfigurationSection(path);
        if (section == null) return null;
        return new ReadOnlyConfiguration(section, this.contents);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getCurrentPath()
     * @return Path of this section relative to its root
     */
    public String getCurrentPath() {
        return this.config.getCurrentPath();
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getDefaultSection ()
     * @return Equivalent section in root configuration.
     */
    public ConfigurationSection getDefaultSection() {
        return this.config.getDefaultSection();
    }
    
    /**
     * @param path
     *            Path of the double to get.
     * @param def
     *            The default value to return if the path is not found.
     * @see org.bukkit.configuration.ConfigurationSection#getDouble(java.lang.String,
     *      double)
     * @return Requested double.
     */
    public double getDouble(String path, double def) {
        return this.config.getDouble(path, def);
    }
    
    /**
     * @param path
     *            Path of the double to get.
     * @see org.bukkit.configuration.ConfigurationSection#getDouble(java.lang.String)
     * @return Requested double.
     */
    public double getDouble(String path) {
        return this.config.getDouble(path);
    }
    
    /**
     * @param path
     *            Path of the List to get.
     * @see org.bukkit.configuration.ConfigurationSection#getDoubleList(java.lang.String)
     * @return Requested List of Double.
     */
    public List<Double> getDoubleList(String path) {
        return this.config.getDoubleList(path);
    }
    
    /**
     * @param path
     *            Path of the List to get.
     * @see org.bukkit.configuration.ConfigurationSection#getFloatList(java.lang.String)
     * @return Requested List of Float.
     */
    public List<Float> getFloatList(String path) {
        return this.config.getFloatList(path);
    }
    
    /**
     * @param path
     *            Path of the int to get.
     * @param def
     *            The default value to return if the path is not found.
     * @see org.bukkit.configuration.ConfigurationSection#getInt(java.lang.String,
     *      int)
     * @return Requested int.
     */
    public int getInt(String path, int def) {
        return this.config.getInt(path, def);
    }
    
    /**
     * @param path
     *            Path of the int to get.
     * @see org.bukkit.configuration.ConfigurationSection#getInt(java.lang.String)
     * @return Requested int.
     */
    public int getInt(String path) {
        return this.config.getInt(path);
    }
    
    /**
     * @param path
     *            Path of the List to get.
     * @see org.bukkit.configuration.ConfigurationSection#getIntegerList(java.lang.String)
     * @return Requested List of Integer.
     */
    public List<Integer> getIntegerList(String path) {
        return this.config.getIntegerList(path);
    }
    
    /**
     * @param path
     *            Path of the ItemStack to get.
     * @param def
     *            The default value to return if the path is not found.
     * @see org.bukkit.configuration.ConfigurationSection#getItemStack(java.lang.String,
     *      org.bukkit.inventory.ItemStack)
     * @return Requested ItemStack.
     */
    public ItemStack getItemStack(String path, ItemStack def) {
        return this.config.getItemStack(path, def);
    }
    
    /**
     * @param path
     *            Path of the ItemStack to get.
     * @see org.bukkit.configuration.ConfigurationSection#getItemStack(java.lang.String)
     * @return Requested ItemStack.
     */
    public ItemStack getItemStack(String path) {
        return this.config.getItemStack(path);
    }
    
    /**
     * @param deep
     *            Whether or not to get a deep list, as opposed to a shallow
     *            list.
     * @see org.bukkit.configuration.ConfigurationSection#getKeys(boolean)
     * @return Set of keys contained within this ConfigurationSection.
     */
    public Set<String> getKeys(boolean deep) {
        return this.config.getKeys(deep);
    }
    
    /**
     * @param path
     *            Path of the List to get.
     * @param def
     *            The default value to return if the path is not found.
     * @see org.bukkit.configuration.ConfigurationSection#getList(java.lang.String,
     *      java.util.List)
     * @return Requested List.
     */
    public List<?> getList(String path, List<?> def) {
        return this.config.getList(path, def);
    }
    
    /**
     * @param path
     *            Path of the List to get.
     * @see org.bukkit.configuration.ConfigurationSection#getList(java.lang.String)
     * @return Requested List.
     */
    public List<?> getList(String path) {
        return this.config.getList(path);
    }
    
    /**
     * @param path
     *            Path of the long to get.
     * @param def
     *            The default value to return if the path is not found.
     * @see org.bukkit.configuration.ConfigurationSection#getLong(java.lang.String,
     *      long)
     * @return Requested long.
     */
    public long getLong(String path, long def) {
        return this.config.getLong(path, def);
    }
    
    /**
     * @param path
     *            Path of the long to get.
     * @see org.bukkit.configuration.ConfigurationSection#getLong(java.lang.String)
     * @return Requested long.
     */
    public long getLong(String path) {
        return this.config.getLong(path);
    }
    
    /**
     * @param path
     *            Path of the List to get.
     * @see org.bukkit.configuration.ConfigurationSection#getLongList(java.lang.String)
     * @return Requested List of Long.
     */
    public List<Long> getLongList(String path) {
        return this.config.getLongList(path);
    }
    
    /**
     * @param path
     *            Path of the List to get.
     * @see org.bukkit.configuration.ConfigurationSection#getMapList(java.lang.String)
     * @return Requested List of Maps.
     */
    public List<Map<?, ?>> getMapList(String path) {
        return this.config.getMapList(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getName()
     * @return Name of this section
     */
    public String getName() {
        return this.config.getName();
    }
    
    /**
     * @param path
     *            Path of the OfflinePlayer to get.
     * @param def
     *            The default value to return if the path is not found.
     * @see org.bukkit.configuration.ConfigurationSection#getOfflinePlayer(java.lang.String,
     *      org.bukkit.OfflinePlayer)
     * @return Requested OfflinePlayer.
     */
    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def) {
        return this.config.getOfflinePlayer(path, def);
    }
    
    /**
     * @param path
     *            Path of the OfflinePlayer to get.
     * @see org.bukkit.configuration.ConfigurationSection#getOfflinePlayer(java.lang.String)
     * @return Requested OfflinePlayer.
     */
    public OfflinePlayer getOfflinePlayer(String path) {
        return this.config.getOfflinePlayer(path);
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getParent()
     * @return Parent section containing this section.
     */
    public ConfigurationSection getParent() {
        return this.config.getParent();
    }
    
    /**
     * @see org.bukkit.configuration.ConfigurationSection#getRoot()
     * @return Root configuration containing this section.
     */
    public Configuration getRoot() {
        return this.config.getRoot();
    }
    
    /**
     * @param path
     *            Path of the List to get.
     * @see org.bukkit.configuration.ConfigurationSection#getShortList(java.lang.String)
     * @return Requested List of Maps.
     */
    public List<Short> getShortList(String path) {
        return this.config.getShortList(path);
    }
    
    /**
     * @param path
     *            Path of the String to get.
     * @param def
     *            The default value to return if the path is not found.
     * @see org.bukkit.configuration.ConfigurationSection#getString(java.lang.String,
     *      java.lang.String)
     * @return Requested String.
     */
    public String getString(String path, String def) {
        return this.config.getString(path, def);
    }
    
    /**
     * @param path
     *            Path of the String to get.
     * @see org.bukkit.configuration.ConfigurationSection#getString(java.lang.String)
     * @return Requested String.
     */
    public String getString(String path) {
        return this.config.getString(path);
    }
    
    /**
     * @param path
     *            Path of the List to get.
     * @see org.bukkit.configuration.ConfigurationSection#getStringList(java.lang.String)
     * @return Requested List of String.
     */
    public List<String> getStringList(String path) {
        return this.config.getStringList(path);
    }
    
    /**
     * @param deep
     *            Whether or not to get a deep list, as opposed to a shallow
     *            list.
     * @see org.bukkit.configuration.ConfigurationSection#getValues(boolean)
     * @return Map of keys and values of this section.
     */
    public Map<String, Object> getValues(boolean deep) {
        return this.config.getValues(deep);
    }
    
    /**
     * @param path
     *            Path of the Vector to get.
     * @param def
     *            The default value to return if the path is not found.
     * @see org.bukkit.configuration.ConfigurationSection#getVector(java.lang.String,
     *      org.bukkit.util.Vector)
     * @return Requested Vector.
     */
    public Vector getVector(String path, Vector def) {
        return this.config.getVector(path, def);
    }
    
    /**
     * @param path
     *            Path of the Vector to get.
     * @see org.bukkit.configuration.ConfigurationSection#getVector(java.lang.String)
     * @return Requested Vector.
     */
    public Vector getVector(String path) {
        return this.config.getVector(path);
    }
    
    /**
     * @param path
     *            Path of the boolean to check.
     * @see org.bukkit.configuration.ConfigurationSection#isBoolean(java.lang.String)
     * @return Whether or not the specified path is a boolean.
     */
    public boolean isBoolean(String path) {
        return this.config.isBoolean(path);
    }
    
    /**
     * @param path
     *            Path of the Color to check.
     * @see org.bukkit.configuration.ConfigurationSection#isColor(java.lang.String)
     * @return Whether or not the specified path is a Color.
     */
    public boolean isColor(String path) {
        return this.config.isColor(path);
    }
    
    /**
     * @param path
     *            Path of the ConfigurationSection to check.
     * @see org.bukkit.configuration.ConfigurationSection#isConfigurationSection(java.lang.String)
     * @return Whether or not the specified path is a ConfigurationSection.
     */
    public boolean isConfigurationSection(String path) {
        return this.config.isConfigurationSection(path);
    }
    
    /**
     * @param path
     *            Path of the double to check.
     * @see org.bukkit.configuration.ConfigurationSection#isDouble(java.lang.String)
     * @return Whether or not the specified path is a double.
     */
    public boolean isDouble(String path) {
        return this.config.isDouble(path);
    }
    
    /**
     * @param path
     *            Path of the int to check.
     * @see org.bukkit.configuration.ConfigurationSection#isInt(java.lang.String)
     * @return Whether or not the specified path is a int.
     */
    public boolean isInt(String path) {
        return this.config.isInt(path);
    }
    
    /**
     * @param path
     *            Path of the ItemStack to check.
     * @see org.bukkit.configuration.ConfigurationSection#isItemStack(java.lang.String)
     * @return Whether or not the specified path is a ItemStack.
     */
    public boolean isItemStack(String path) {
        return this.config.isItemStack(path);
    }
    
    /**
     * @param path
     *            Path of the List to check.
     * @see org.bukkit.configuration.ConfigurationSection#isList(java.lang.String)
     * @return Whether or not the specified path is a List.
     */
    public boolean isList(String path) {
        return this.config.isList(path);
    }
    
    /**
     * @param path
     *            Path of the long to check.
     * @see org.bukkit.configuration.ConfigurationSection#isLong(java.lang.String)
     * @return Whether or not the specified path is a long.
     */
    public boolean isLong(String path) {
        return this.config.isLong(path);
    }
    
    /**
     * @param path
     *            Path of the OfflinePlayer to check.
     * @see org.bukkit.configuration.ConfigurationSection#isOfflinePlayer(java.lang.String)
     * @return Whether or not the specified path is a OfflinePlayer.
     */
    public boolean isOfflinePlayer(String path) {
        return this.config.isOfflinePlayer(path);
    }
    
    /**
     * @param path
     *            Path to check for existence.
     * @see org.bukkit.configuration.ConfigurationSection#isSet(java.lang.String)
     * @return True if this section contains the requested path, regardless of
     *         having a default.
     */
    public boolean isSet(String path) {
        return this.config.isSet(path);
    }
    
    /**
     * @param path
     *            Path of the String to check.
     * @see org.bukkit.configuration.ConfigurationSection#isString(java.lang.String)
     * @return Whether or not the specified path is a String.
     */
    public boolean isString(String path) {
        return this.config.isString(path);
    }
    
    /**
     * @param path
     *            Path of the Vector to check.
     * @see org.bukkit.configuration.ConfigurationSection#isVector(java.lang.String)
     * @return Whether or not the specified path is a Vector.
     */
    public boolean isVector(String path) {
        return this.config.isVector(path);
    }
    
    /**
     * 把配置对象转换为yaml格式的配置文本
     * 
     * @return yaml格式的配置文本
     */
    @Override
    public String toString() {
        return this.contents;
    }
    
    /**
     * 使用<b>UTF-8</b>编码从输入流读取配置
     * 
     * @param input
     *            含有配置信息的输入流
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
     *            含有配置信息的输入流
     * @param charset
     *            配置的编码
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
            ExceptionUtils.handle(e);
            return null;
        }
        
    }
    
}
