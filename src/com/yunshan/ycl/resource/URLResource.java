package com.yunshan.ycl.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.yunshan.ycl.exception.ExceptionUtils;

/**
 * 依赖于URL的资源
 * <p>
 * 作者：YunShan <br>
 * 创建日期：2016年7月1日
 */
public class URLResource implements Resource {
    
    private final URL url;
    
    public URLResource(URL url) {
        this.url = url;
    }
    
    /**
     * @see com.yunshan.ycl.resource.Resource#getInputStream()
     */
    @Override
    public InputStream getInputStream() {
        try {
            return this.url.openStream();
        } catch (IOException e) {
            ExceptionUtils.handle(e);
            return null;
        }
    }
    
    /**
     * @see com.yunshan.ycl.resource.Resource#getUrl()
     */
    @Override
    public URL getUrl() {
        return this.url;
    }
    
    /**
     * @see com.yunshan.ycl.resource.Resource#getSize()
     */
    @Override
    public long getSize() {
        try {
            return this.url.openConnection().getContentLengthLong();
        } catch (IOException e) {
            ExceptionUtils.handle(e);
            return -1;
        }
    }
    
}
