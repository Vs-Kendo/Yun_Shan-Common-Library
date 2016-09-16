package org.yunshanmc.ycl.command.simple;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Objects;

/**
 * 参数转换器管理器
 */
public class ArgConverterManager {
    
    private static final Map<Class<?>, ArgConverter<?>> INTERNAL_CONVERTERS = Maps.newHashMap();
    
    static {
        INTERNAL_CONVERTERS.put(int.class, new ArgConverter<Integer>(int.class) {
            
            @Override
            public Integer convertArg(String arg) {
                return Integer.valueOf(arg);
            }
            
            @Override
            public Integer getDefaultValue() {
                return Integer.valueOf(-1);
            }
        });
        INTERNAL_CONVERTERS.put(Integer.class, new ArgConverter<Integer>(Integer.class) {
            
            @Override
            public Integer convertArg(String arg) {
                return Integer.valueOf(arg);
            }
            
            @Override
            public Integer getDefaultValue() {
                return Integer.valueOf(-1);
            }
        });
        INTERNAL_CONVERTERS.put(short.class, new ArgConverter<Short>(short.class) {
            
            @Override
            public Short convertArg(String arg) {
                return Short.valueOf(arg);
            }
            
            @Override
            public Short getDefaultValue() {
                return Short.valueOf((short) -1);
            }
        });
        INTERNAL_CONVERTERS.put(Short.class, new ArgConverter<Short>(Short.class) {
            
            @Override
            public Short convertArg(String arg) {
                return Short.valueOf(arg);
            }
            
            @Override
            public Short getDefaultValue() {
                return Short.valueOf((short) -1);
            }
        });
        INTERNAL_CONVERTERS.put(byte.class, new ArgConverter<Byte>(byte.class) {
            
            @Override
            public Byte convertArg(String arg) {
                return Byte.valueOf(arg);
            }
            
            @Override
            public Byte getDefaultValue() {
                return Byte.valueOf((byte) -1);
            }
        });
        INTERNAL_CONVERTERS.put(Byte.class, new ArgConverter<Byte>(Byte.class) {
            
            @Override
            public Byte convertArg(String arg) {
                return Byte.valueOf(arg);
            }
            
            @Override
            public Byte getDefaultValue() {
                return Byte.valueOf((byte) -1);
            }
        });
        INTERNAL_CONVERTERS.put(long.class, new ArgConverter<Long>(long.class) {
            
            @Override
            public Long convertArg(String arg) {
                return Long.valueOf(arg);
            }
            
            @Override
            public Long getDefaultValue() {
                return Long.valueOf(-1);
            }
        });
        INTERNAL_CONVERTERS.put(Long.class, new ArgConverter<Long>(Long.class) {
            
            @Override
            public Long convertArg(String arg) {
                return Long.valueOf(arg);
            }
            
            @Override
            public Long getDefaultValue() {
                return Long.valueOf(-1);
            }
        });
        INTERNAL_CONVERTERS.put(float.class, new ArgConverter<Float>(float.class) {
            
            @Override
            public Float convertArg(String arg) {
                return Float.valueOf(arg);
            }
            
            @Override
            public Float getDefaultValue() {
                return Float.valueOf(-1);
            }
        });
        INTERNAL_CONVERTERS.put(Float.class, new ArgConverter<Float>(Float.class) {
            
            @Override
            public Float convertArg(String arg) {
                return Float.valueOf(arg);
            }
            
            @Override
            public Float getDefaultValue() {
                return Float.valueOf(-1);
            }
        });
        INTERNAL_CONVERTERS.put(double.class, new ArgConverter<Double>(double.class) {
            
            @Override
            public Double convertArg(String arg) {
                return Double.valueOf(arg);
            }
            
            @Override
            public Double getDefaultValue() {
                return Double.valueOf(-1);
            }
        });
        INTERNAL_CONVERTERS.put(Double.class, new ArgConverter<Double>(Double.class) {
            
            @Override
            public Double convertArg(String arg) {
                return Double.valueOf(arg);
            }
            
            @Override
            public Double getDefaultValue() {
                return Double.valueOf(-1);
            }
        });
        INTERNAL_CONVERTERS.put(String.class, new ArgConverter<String>(String.class) {
            
            @Override
            public String convertArg(String arg) {
                return arg;
            }
        });
        INTERNAL_CONVERTERS.put(Player.class, new ArgConverter<Player>(Player.class) {
            
            @Override
            public Player convertArg(String arg) {
                return Bukkit.getPlayerExact(arg);
            }
        });
        INTERNAL_CONVERTERS.put(OfflinePlayer.class, new ArgConverter<OfflinePlayer>(OfflinePlayer.class) {
            
            @Override
            public OfflinePlayer convertArg(String arg) {
                OfflinePlayer p = Bukkit.getOfflinePlayer(arg);
                if (p.hasPlayedBefore()) {
                    return p;
                } else {
                    return null;
                }
            }
        });
    }
    
    private static ArgConverterManager instance;
    
    private Map<Class<?>, ArgConverter<?>> argConverters = Maps.newHashMap();
    
    protected ArgConverterManager() {
        // 禁止外部实例化，允许子类实例化  
        this.argConverters.putAll(INTERNAL_CONVERTERS);
    }
    
    /**
     * 获取参数转换器管理器实例
     * 
     * @return 参数转换器管理器实例
     */
    public static ArgConverterManager getInstance() {
        if (instance == null) {
            instance = new ArgConverterManager();
        }
        return instance;
    }
    
    /**
     * 注册参数转换器
     * <p>
     * 若已存在结果类型为cls的参数转换器则会注册失败
     * 
     * @param <T>
     *            参数转换器的结果类型
     * @param cls
     *            参数转换器的结果类型的Class对象
     * @param converter
     *            参数转换器
     * @return 成功注册返回true，否则返回false
     */
    public <T> boolean registerArgConverter(Class<T> cls, ArgConverter<T> converter) {
        if (!argConverters.containsKey(cls)) {
            argConverters.put(cls, converter);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 获取指定类型的参数转换器
     * 
     * @param <T>
     *            参数转换器的结果类型
     * @param cls
     *            参数转换器的结果类型的Class对象
     * @return 转换结果为cls类对象的参数转换器
     */
    @SuppressWarnings("unchecked")
    public <T> ArgConverter<T> getArgConverter(Class<T> cls) {
        return (ArgConverter<T>) argConverters.get(cls);
    }
    
    /**
     * 获取所有指定类型的参数转换器
     * 
     * @param clss
     *            参数转换器的转换结果类型
     * @return 所有指定类型的参数转换器
     * @throws NullPointerException
     *             若clss参数中有某个元素为null则会抛出此异常
     * @throws MissingArgConverterExecption
     *             若无法找到某个参数的参数转换器则会抛出此异常
     * @see #getArgConverter(Class)
     */
    public ArgConverter<?>[] getArgConverters(Class<?>[] clss) throws MissingArgConverterExecption {
        ArgConverter<?>[] handles = new ArgConverter<?>[clss.length];
        for (int i = 0; i < clss.length; i++) {
            Class<?> cls = clss[i];
            Objects.requireNonNull(cls);
            ArgConverter<?> handle = this.getArgConverter(cls);
            if (handle == null) throw new MissingArgConverterExecption(cls);
            handles[i] = handle;
        }
        return handles;
    }
    
    /**
     * 未找到参数转换器异常
     * <p>
     * 该异常是在{@link ArgConverterManager#getArgConverters(Class[])}方法中被抛出的
     */
    public static class MissingArgConverterExecption extends RuntimeException {
        
        private static final long serialVersionUID = 1L;
        
        private final Class<?> convertTo;
        
        /**
         * @param convertTo
         *            参数转换器的结果类型
         */
        private MissingArgConverterExecption(Class<?> convertTo) {
            super(convertTo.getName());
            this.convertTo = convertTo;
        }
        
        /**
         * 获取未找到的参数转换器应有的结果类型
         * 
         * @return 参数转换器的结果类型
         */
        public Class<?> getConvertTo() {
            return this.convertTo;
        }
    }
}
