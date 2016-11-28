package org.yunshanmc.ycl.message.format;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.text.Format;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * YCL信息格式化类 <br>
 * 可增加自定义格式化器
 * <p>
 * 作者： Yun-Shan <br>
 * 创建日期： 2016/10/8.
 */
public class YCLMessageFormat extends ExtendMessageFormat {
    
    private static final Set<String> SYS_FORMAT_TYPE = Sets.newHashSet(ExtendMessageFormat.TYPE_KEYWORDS);
    
    private static final Map<String, Function<String, Format>> CUSTOM_FORMATS = Maps.newHashMap();
    
    public YCLMessageFormat(String pattern) {
        super(pattern);
    }
    
    public YCLMessageFormat(String pattern, Locale locale) {
        super(pattern, locale);
    }
    
    public static String format(String pattern, Object... arguments) {
        return new YCLMessageFormat(pattern).format(arguments);
    }
    
    /**
     * 注册格式化器
     *
     * @param type
     *     格式化器类型
     * @param formatSupplier
     *     格式化器提供者，将传入语言文件提供的格式化器修饰符，需返回生成的格式化器
     *
     * @return 若格式化器类型已存在则注册失败并返回false，否则注册成功并返回true
     */
    public static boolean registerFormat(String type, Function<String, Format> formatSupplier) {
        if (SYS_FORMAT_TYPE.contains(type) || CUSTOM_FORMATS.containsKey(type)) return false;
        CUSTOM_FORMATS.put(type, formatSupplier);
        return true;
    }
    
    /**
     * 反注册格式化器类型
     *
     * @param type
     *     格式化器类型
     *
     * @return 若不存在此格式化器类型则返回false，否则返回true
     */
    public static boolean unregisterFormat(String type) {
        return CUSTOM_FORMATS.remove(type) != null;
    }
    
    @Override
    protected Format findCustomFormat(String type, String modifier) {
        Function<String, Format> fun = CUSTOM_FORMATS.get(type);
        if (fun != null) {
            return fun.apply(modifier);
        }
        return super.findCustomFormat(type, modifier);
    }
    
    @Override
    protected ExtendMessageFormat newThisInstance(String pattern, Locale locale) {
        return new YCLMessageFormat(pattern, locale);
    }
}
