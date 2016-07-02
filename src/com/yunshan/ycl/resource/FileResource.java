package com.yunshan.ycl.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.yunshan.ycl.exception.ExceptionUtils;

/**
 * 依赖于文件的资源
 * <p>
 * 作者：YunShan
 * <p>
 * 创建日期：2016年7月1日
 * <p>
 */
public class FileResource implements Resource {
    
    private final Path path;
    
    /**
     * @param filePath
     *        文件路径
     */
    public FileResource(Path filePath) {
        this.path = filePath;
    }
    
    /**
     * @see com.yunshan.ycl.resource.Resource#getInputStream()
     */
    @Override
    public InputStream getInputStream() {
        try {
            return Files.newInputStream(this.path, StandardOpenOption.READ);
        } catch (IOException e) {
            ExceptionUtils.handle(e);
            return null;
        }
    }
    
    /**
     * @see com.yunshan.ycl.resource.Resource#getSize()
     */
    @Override
    public long getSize() {
        return this.path.toFile().length();
    }
    
    /**
     * @see com.yunshan.ycl.resource.Resource#getUrl()
     */
    @Override
    public URL getUrl() {
        try {
            return this.path.toUri().toURL();
        } catch (MalformedURLException e) {
            ExceptionUtils.handle(e);
            return null;
        }
    }
    
    /**
     * 获取资源的路径
     * 
     * @return 文件资源的路径
     */
    public Path getPath() {
        return this.path;
    }
}
