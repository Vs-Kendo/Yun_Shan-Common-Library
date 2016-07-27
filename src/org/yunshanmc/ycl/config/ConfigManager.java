package org.yunshanmc.ycl.config;

import java.util.Map;

/**
 * 配置管理器
 * <p>
 * 作者：YunShan <br>
 * 创建日期：2016年6月30日
 */
public interface ConfigManager {
    
    void reload();
    
    /**
     * 获取插件配置
     * <p>
     * 获取的配置是对应插件文件夹下的<code>config.yml</code>
     * <p>
     * 若插件自身没有配置，则此方法返回的将是一个空的配置而不会返回null
     * 
     * @return 插件配置
     */
    ReadOnlyConfiguration getPluginConfig();
    
    /**
     * 获取可读写的配置
     * 
     * @param filePath
     *            配置文件的相对路径
     * @return 可读写的配置，当配置文件不存在或读取失败时返回null
     */
    ReadWriteConfiguration getConfig(String filePath);
    
    /**
     * 获取只读配置
     * 
     * @param filePath
     *            配置文件的相对路径
     * @return 只读配置，当配置文件不存在时返回null
     * @see #getConfig(String)
     */
    ReadOnlyConfiguration getReadOnlyConfig(String filePath);
    
    /**
     * 获取指定文件夹下的所有配置
     * <p>
     * 获取到的配置为可读写的配置
     * 
     * @param dirPath
     *            目录的相对路径
     * @param deep
     *            是否深度遍历获取配置,为true时会遍历获取子文件夹的配置
     * @return 指定文件夹下 的所有配置
     */
    Map<String, ReadWriteConfiguration> getAllConfigs(String dirPath, boolean deep);
    
    /**
     * 获取指定文件夹下的所有配置
     * <p>
     * <b>获取到的配置为只读配置</b>
     * 
     * @param dirPath
     *            目录的相对路径
     * @param deep
     *            是否深度遍历获取配置,为true时会遍历获取子文件夹的配置
     * @return 指定文件夹下的所有配置
     */
    Map<String, ReadOnlyConfiguration> getAllReadOnlyConfigs(String dirPath, boolean deep);
    
    /**
     * 保存默认配置
     * <p>
     * <code>jarPath</code>不存在或无法读取，或者保存到文件夹时失败时会返回<code>false</code>
     * 
     * @param pathInJar
     *            配置文件在自身jar中的路径(绝对路径)
     * @param filePath
     *            配置文件要保存到的路径(相对路径)，为null时将使用jar中的路径
     * @param force
     *            当配置已存在时是否覆盖
     * @return 保存成功返回true，否则返回false
     */
    boolean saveDefaultConfig(String pathInJar, String filePath, boolean force);
    
    /**
     * 保存配置文件，当文件已存在时会覆盖原文件
     * 
     * @param cfg
     *            配置文件
     * @param path
     *            要保存到的路径(相对路径)
     * @return 保存成功返回true，写入失败返回false
     */
    boolean saveConfig(ReadWriteConfiguration cfg, String path);
    
}