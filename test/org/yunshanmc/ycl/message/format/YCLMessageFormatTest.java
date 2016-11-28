package org.yunshanmc.ycl.message.format;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * TODO 注释
 * <p>
 * 作者： Yun-Shan <br>
 * 创建日期： 2016/10/9.
 */
public class YCLMessageFormatTest {
    
    @Before
    public void setUp() throws Exception {
        YCLMessageFormat.registerFormat("message", MessageFormat::new);
        YCLMessageFormat.registerFormat("currency", mod -> DecimalFormat.getCurrencyInstance(mod != null ? Locale.forLanguageTag(mod): Locale.getDefault(
            Locale.Category.FORMAT)));
    }
    
    @Test
    public void format() throws Exception {
        Assert.assertEquals("3￥50.00", YCLMessageFormat.format("{0,message,{2}}{1,currency,zh-CN}",new Object[]{1,2,3}, 50.0));
    }
    
}