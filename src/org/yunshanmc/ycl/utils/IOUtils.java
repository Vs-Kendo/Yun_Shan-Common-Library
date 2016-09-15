package org.yunshanmc.ycl.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * I/O 工具类
 * <p>
 * 作者：YunShan<br>
 * 创建日期：2016年6月30日
 */
public final class IOUtils {
    
    private IOUtils() {
    }
    
    /**
     * 获取UTF-8编码的输入流读取器
     * 
     * @param stream
     *            输入流
     * @return UTF-8编码的输入流读取器
     */
    public static InputStreamReader getUTF8InputStreamReader(InputStream stream) {
        return getInputStreamReader(stream, StandardCharsets.UTF_8);
    }
    
    /**
     * 获取指定编码的输入流读取器
     * 
     * @param stream
     *            输入流
     * @param charset
     *            输入流读取器的编码
     * @return 指定编码的输入流读取器
     */
    public static InputStreamReader getInputStreamReader(InputStream stream, Charset charset) {
        return new InputStreamReader(stream, charset);
    }
    
    /**
     * 从指定的输入流读取器中读取全部内容
     * <p>
     * 该方法会自动调用{@link InputStreamReader}的{@link InputStreamReader#close()}方法
     * 
     * @param reader
     *            输入流读取器
     * @return 输入流中的所有内容
     * @throws IOException
     *             出现I/O异常时会抛出此异常
     */
    public static String readAll(InputStreamReader reader) throws IOException {
        char[] buffer = new char[1024];
        StringBuilder contents = new StringBuilder();
        int len = 0;
        while ((len = reader.read(buffer)) != -1) {
            contents.append(buffer, 0, len);
        }
        reader.close();
        return contents.toString();
    }
}
