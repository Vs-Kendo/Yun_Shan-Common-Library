package com.yunshan.ycl.resource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Predicate;
import com.yunshan.ycl.locale.LocaleManager;

/**
 * 本地化资源管理器
 */
public class LocaleResourceManager extends StandardResourceManager {
    
    private static final Path LOCAL_RES_PATH = Paths.get("resource");
    
    private final LocaleManager localeManager;
    
    /**
     * @param plugin
     *            要管理资源的插件
     * @param localeManager
     *            本地化管理器
     */
    public LocaleResourceManager(JavaPlugin plugin, LocaleManager localeManager) {
        super(plugin);
        this.localeManager = localeManager;
    }
    
    @Override
    public Resource getSelfResource(String path) {
        return super.getSelfResource(this.checkResourcePath(path));
    }
    
    @Override
    public Resource getFileResource(String path) {
        return super.getFileResource(this.checkResourcePath(path));
    }
    
    @Override
    public Map<String, Resource> getFolderResources(String path, Predicate<String> nameFilter, boolean deep) {
        return super.getFolderResources(this.checkResourcePath(path), nameFilter, deep);
    }
    
    //将资源路径转换为本地化资源路径
    @Override
    protected Path checkResourcePath(String path) {
        Path resPath = super.checkResourcePath(path);
        Locale locale = this.localeManager.getDisplayLocale();
        String localePath = locale.getLanguage() + "_" + locale.getCountry();
        return LOCAL_RES_PATH.resolve(localePath).resolve(resPath);
    }
}
