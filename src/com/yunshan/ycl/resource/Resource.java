package com.yunshan.ycl.resource;

import java.io.InputStream;
import java.net.URL;

/**
 * 表示一个资源
 * <p>
 * 作者：YunShan
 * <p>
 * 创建日期：2016年7月1日
 * <p>
 */
public interface Resource {
    
    /**
     * 获取资源的输入流
     * 
     * @return 资源的输入流，获取失败时返回null
     */
    InputStream getInputStream();
    
    /**
     * 获取资源的URI(Uniform Resource Identifier) 
     * 
     * @return 资源的URI，获取失败时返回null
     */
    URL getUrl();
    
    /**
     * 获取资源的大小
     * 
     * @return 资源的大小
     */
    long getSize();
}
