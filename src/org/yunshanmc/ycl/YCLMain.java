package org.yunshanmc.ycl;

import java.util.Iterator;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.yunshanmc.ycl.config.ConfigManager;
import org.yunshanmc.ycl.exception.ExceptionHandler;
import org.yunshanmc.ycl.exception.ExceptionUtils;
import org.yunshanmc.ycl.locale.LocaleManager;
import org.yunshanmc.ycl.message.MessageManager;
import org.yunshanmc.ycl.message.Messager;
import org.yunshanmc.ycl.message.StandardMessageManager;
import org.yunshanmc.ycl.resource.ResourceManager;
import org.yunshanmc.ycl.resource.StandardResourceManager;
import org.yunshanmc.ycl.util.BukkitUtils;

/**
 * YCL插件主类
 * <p>
 * 作者：YunShan <br>
 * 创建日期：2016年7月21日
 */
public class YCLMain extends JavaPlugin {
    
    private ResourceManager resourceManager;
    private ConfigManager   configManager;
    private LocaleManager   localeManager;
    private MessageManager  messageManager;
    private Messager        messager;
    
    @Override
    public void onEnable() {
        this.resourceManager = new StandardResourceManager(this);
        this.localeManager = new LocaleManager();
        this.messageManager = new StandardMessageManager(this.resourceManager, this.localeManager);
        this.messager = this.messageManager.createMessager();
        ExceptionUtils.setExceptionHandler(new ExceptionHandler() {
            
            @Override
            public void handle(Throwable t) {
                messager.errorConsole("unhandleException.before", t.getClass().getName(), t.getMessage());
                t.printStackTrace();
                Iterator<Plugin> plugins = BukkitUtils.tracePlugin(t.getStackTrace()).iterator();
                StringBuilder pluginNames = new StringBuilder();
                Plugin pl = null;
                while (plugins.hasNext()) {
                    pl = plugins.next();
                    if (!plugins.hasNext()) break;// 跳过最后一个
                    pluginNames.append(messageManager.getMessage("unhandleException.pluginTrace.one", pl.getName()))
                            .append(messageManager.getMessage("unhandleException.pluginTrace.separator"));
                }
                if (pl != null) {// 最后一个不加分隔符
                    pluginNames.append(messageManager.getMessage("unhandleException.pluginTrace.one", pl.getName()));
                }
                messager.errorConsole("unhandleException.after", t.getClass().getName(), t.getMessage(),
                        pluginNames.toString());
            }
        });
    }
    
    @Override
    public void saveDefaultConfig() {
        this.configManager.saveDefaultConfig("config.yml", "config.yml", false);
    }
    
    @Override
    public FileConfiguration getConfig() {
        YamlConfiguration yml = new YamlConfiguration();
        try {
            yml.loadFromString(this.configManager.getPluginConfig().toString());
        } catch (InvalidConfigurationException e) {
            ExceptionUtils.handle(e);
        }
        return yml;
    }
    
    public ResourceManager getResourceManager() {
        return this.resourceManager;
    }
    
    public ConfigManager getConfigManager() {
        return this.configManager;
    }
    
    public LocaleManager getLocaleManager() {
        return this.localeManager;
    }
    
    public MessageManager getMessageManager() {
        return this.messageManager;
    }
}
