package com.yunshan.ycl.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunshan.ycl.exception.ExceptionUtils;

/**
 * 标准资源管理器
 */
public class StandardResourceManager implements ResourceManager {
    
    private final ClassLoader classLoader;
    private final Path        pluginFolder;
    
    /**
     * @param plugin
     *        插件对象
     */
    public StandardResourceManager(JavaPlugin plugin) {
        this.pluginFolder = plugin.getDataFolder().toPath();
        this.classLoader = plugin.getClass().getClassLoader();
    }
    
    @Override
    public Resource getSelfResource(String path) {
        return this.getSelfResource(this.checkResourcePath(path));
    }
    
    @Override
    public Resource getFileResource(String path) {
        return this.getFileResource(this.checkResourcePath(path));
    }
    
    @Override
    public Map<String, Resource> getFolderResources(String path, Predicate<String> nameFilter, boolean deep) {
        return this.getFolderResources(this.checkResourcePath(path), nameFilter, deep);
    }
    
    protected Resource getSelfResource(Path resPath) {
        URL url = this.classLoader.getResource("/" + resPath.toString().replace('\\', '/'));
        if (url == null) return null;
        return new URLResource(url);
    }
    
    protected Resource getFileResource(Path resPath) {
        resPath = this.pluginFolder.resolve(resPath);
        if (Files.isReadable(resPath) && Files.isRegularFile(resPath, LinkOption.NOFOLLOW_LINKS)) {
            return new FileResource(resPath);
        }
        return null;
    }
    
    protected Map<String, Resource> getFolderResources(Path dirPath, Predicate<String> nameFilter, boolean deep) {
        dirPath = this.pluginFolder.resolve(dirPath);
        if (!Files.isDirectory(dirPath, LinkOption.NOFOLLOW_LINKS)) return null;
        try {
            Map<String, Resource> allRes = Maps.newLinkedHashMap();
            ResourceFileVisitor visitor = deep ? new DeepFileVisitor(nameFilter) : new NotDeepFileVisitor(nameFilter);
            Files.walkFileTree(dirPath, visitor);
            List<Path> resPaths = visitor.getResourcePaths();
            for (Path resPath : resPaths) {
                allRes.put(this.pluginFolder.relativize(resPath).toString(), new FileResource(resPath));
            }
            if (!allRes.isEmpty()) return allRes;
        } catch (IOException e1) {
            ExceptionUtils.handle(e1);
        }
        return null;
    }
    
    @Override
    public boolean writeResource(String path, InputStream resToWrite, boolean force) {
        Path resPath = this.checkResourcePath(path);
        resPath = this.pluginFolder.resolve(resPath);
        if (!force && Files.exists(resPath, LinkOption.NOFOLLOW_LINKS)) {
            return true;// 资源已存在，且参数force设为不覆盖，直接返回
        }
        try {
            Files.copy(resToWrite, resPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            ExceptionUtils.handle(e);
            return false;
        }
    }
    
    protected Path checkResourcePath(String path) {
        Path resPath = resolvePath(path);
        Objects.requireNonNull(resPath);
        return resPath;
    }
    
    private static Path resolvePath(String path) {
        if (Strings.isNullOrEmpty(path)) return null;
        path = path.replace('\\', '/');
        Iterator<String> splited = Splitter.on('/').omitEmptyStrings().split(path).iterator();
        if (!splited.hasNext()) return null;
        String first = splited.next();
        Path resPath = null;
        if (splited.hasNext()) {
            List<String> list = Lists.newArrayList();
            while (splited.hasNext()) {
                String subPath = splited.next();
                list.add(subPath);
            }
            String[] more = list.toArray(new String[list.size()]);
            resPath = Paths.get(first, more);
        } else {
            resPath = Paths.get(first);
        }
        resPath = resPath.normalize();
        if (resPath.startsWith("..")) {// 禁止切到父级目录
            resPath = resPath.subpath(1, resPath.getNameCount());
        }
        if (resPath.toString().length() == 0) return null;
        return resPath;
    }
    
    private static abstract class ResourceFileVisitor extends SimpleFileVisitor<Path> {
        
        private static final Predicate<String> DEFAULT_NAME_FILTER = new Predicate<String>() {
            
            @Override
            public boolean apply(String arg0) {
                return true;
            }
        };
        
        private final Predicate<String> nameFilter;
        private final List<Path>        resPaths = Lists.newLinkedList();
        
        protected ResourceFileVisitor(Predicate<String> nameFilter) {
            if (nameFilter != null) {
                this.nameFilter = nameFilter;
            } else {
                this.nameFilter = DEFAULT_NAME_FILTER;
            }
        }
        
        @Override
        public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
            Objects.requireNonNull(filePath);
            Objects.requireNonNull(attrs);
            File file = filePath.toFile();
            if (file.canRead() && this.nameFilter.apply(file.getName())) {
                this.resPaths.add(filePath);
            }
            return FileVisitResult.CONTINUE;
        }
        
        public List<Path> getResourcePaths() {
            return this.resPaths;
        }
    }
    
    private static class NotDeepFileVisitor extends ResourceFileVisitor {
        
        protected NotDeepFileVisitor(Predicate<String> nameFilter) {
            super(nameFilter);
        }
        
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.SKIP_SUBTREE;
        }
    }
    
    private static class DeepFileVisitor extends ResourceFileVisitor {
        
        protected DeepFileVisitor(Predicate<String> nameFilter) {
            super(nameFilter);
        }
        
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            Objects.requireNonNull(dir);
            Objects.requireNonNull(attrs);
            return FileVisitResult.CONTINUE;
        }
    }
}
