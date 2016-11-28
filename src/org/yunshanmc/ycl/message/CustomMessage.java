package org.yunshanmc.ycl.message;

/**
 * 自定义信息
 * <p>
 * 作者： Yun-Shan <br>
 * 创建日期： 2016/11/17.
 */
public class CustomMessage {
    
    private final String template;
    
    public CustomMessage(String template) {
        this.template = template;
        
    }
    
    /**
     * 获取字符串形式的信息，用于sendMessage(String);
     * @return 用于sendMessage(String)的信息
     */
    public String[] getLegacyMessage() {
        return null;
    }
    
    /**
     * 获取Json形式的信息，用于sendRawMessage(String);
     * @return 用于sendRawMessage(String)的信息
     */
    public String[] getJsonMessage() {
        return null;
    }
}
