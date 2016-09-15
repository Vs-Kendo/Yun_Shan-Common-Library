package org.yunshanmc.ycl.utils.reflect;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * 方法搜寻器
 * <p>
 * 作者： Administrator
 * 创建日期： 2016/9/15.
 */
public class MethodFinder {
    
    private Predicate<Method> filter;
    
    public MethodFinder() {
        this.filter = Predicates.alwaysTrue();
    }
    
    private MethodFinder(Predicate<Method> filter) {
        this.filter = filter;
    }
    
    /**
     * 搜索指定类中满足条件的第一个方法
     * <p>
     * @param cls
     *     要搜索的类
     *
     * @return 搜索到的满足条件的第一个方法，没有任何方法满足条件则返回null
     */
    public Method findMethod(Class<?> cls) {
        Method[] ms = cls.getDeclaredMethods();
        for (Method m : ms) {
            if (this.filter.apply(m)) return m;
        }
        return null;
    }
    
    /**
     * 搜索指定类中满足条件的所有方法
     * <p>
     * @param cls
     *     要搜索的类
     *
     * @return 搜索到的满足条件的所有方法，没有任何方法满足条件则返回空列表
     */
    public List<Method> findMethods(Class<?> cls) {
        List<Method> list = Lists.newArrayList();
        Method[] ms = cls.getDeclaredMethods();
        for (Method m : ms) {
            if (this.filter.apply(m)) list.add(m);
        }
        return list;
    }
    
    /**
     * 增加方法过滤器
     * <p>
     * @param filter
     *     方法过滤器
     *
     * @return 新的MethodFinder实例
     */
    public MethodFinder addFilter(Predicate<Method> filter) {
        return new MethodFinder(Predicates.and(this.filter, filter));
    }
    
    /**
     * 按方法名过滤方法.
     * <p>
     * @param mName
     *     要过滤出的方法的名字
     *
     * @return 新的MethodFinder实例
     */
    public MethodFinder filterName(String mName) {
        return this.addFilter((@Nonnull Method m) -> m.getName().equals(mName));
    }
    
    /**
     * 按注解过滤方法
     * <p>
     * @param annClass
     *     要过滤出的方法所带有的注解
     *
     * @return 新的MethodFinder实例
     */
    public MethodFinder filterAnnotationPresent(Class<? extends Annotation> annClass) {
        return this.addFilter((@Nonnull Method m) -> m.isAnnotationPresent(annClass));
    }
    
    /**
     * 按修饰符过滤方法
     * <p>
     * @param modifies
     *     要过滤出的方法所拥有的修饰符
     *
     * @return 新的MethodFinder实例
     */
    public MethodFinder filterModifies(int modifies) {
        return this.addFilter((@Nonnull Method m) -> (m.getModifiers() & modifies) != 0);
    }
    
    /**
     * 过滤出修饰符为public的方法
     * <p>
     * @return 新的MethodFinder实例
     *
     * @see #filterModifies(int)
     */
    public MethodFinder filterPublic() {
        return this.filterModifies(Modifier.PUBLIC);
    }
    
    /**
     * 根据参数列表过滤方法
     * <p>
     * @param types
     *     要过滤出的方法的参数列表
     *
     * @return 新的MethodFinder实例
     */
    public MethodFinder filterParameterTypes(Class<?>... types) {
        return this.addFilter((@Nonnull Method m) -> Arrays.equals(m.getParameterTypes(), types));
    }
    
    /**
     * 根据返回值类型过滤方法
     * <p>
     * @param type
     *     要过滤出的方法的返回值类型
     *
     * @return 新的MethodFinder实例
     */
    public MethodFinder filterReturnType(Class<?> type) {
        return this.addFilter((@Nonnull Method m) -> type.equals(m.getReturnType()));
    }
}
