package com.onezol.vertx.framework.common.util;

import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 对象转换工具类，继承自 Spring 的 BeanUtils 类，提供了一些对象转换的实用方法。
 */
public final class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 缓存 BeanCopier 实例的映射，键为源类名和目标类名的组合，值为对应的 BeanCopier 实例。
     */
    private static final Map<String, BeanCopier> BEAN_COPIER_CACHE = new HashMap<>();

    private BeanUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    /**
     * 将源对象转换为目标对象。
     *
     * @param source     源对象，若为 null 则直接返回 null。
     * @param targetType 目标对象的类型，若为 null 则直接返回 null。
     * @param <S>        源对象的类型。
     * @param <T>        目标对象的类型。
     * @return 转换后的目标对象实例，若转换失败则抛出异常。
     */
    public static <S, T> T toBean(S source, @NonNull Class<T> targetType) {
        if (source == null) {
            return null;
        }
        // 生成缓存键，由源类名和目标类名组合而成
        String key = source.getClass().getName() + targetType.getName();
        // 从缓存中获取 BeanCopier 实例，若不存在则创建新的实例并放入缓存
        BeanCopier copier = BEAN_COPIER_CACHE.computeIfAbsent(key, k -> BeanCopier.create(source.getClass(), targetType, true));
        try {
            Constructor<T> constructor = targetType.getDeclaredConstructor();
            T newInstance = constructor.newInstance();
            // 使用 BeanCopier 自定义转换器复制源对象的属性到目标对象
            copier.copy(source, newInstance, getConverter());
            return newInstance;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("目标类 " + targetType.getName() + " 没有无参构造函数", e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("无法实例化目标类 " + targetType.getName(), e);
        }
    }

    /**
     * 将源对象集合转换为目标对象集合。
     *
     * @param source     源对象集合，若为 null 或为空则返回空列表。
     * @param targetType 目标对象的类型。
     * @param <S>        源对象的类型。
     * @param <T>        目标对象的类型。
     * @return 转换后的目标对象列表。
     */
    public static <S, T> List<T> copyToList(Collection<S> source, @NonNull Class<T> targetType) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(source.size());
        for (S item : source) {
            result.add(toBean(item, targetType));
        }
        return result;
    }

    /**
     * 获取自定义的属性转换器，用于处理枚举类型的转换。
     *
     * @return 实现了 Converter 接口的匿名内部类实例。
     */
    private static Converter getConverter() {
        return (value, target, context) -> {
            if (value == null) {
                return null;
            }

            // 若值为 StandardEnumeration 类型，调用 convertFromEnumeration 方法进行转换
            if (value instanceof StandardEnumeration<?>) {
                return convertFromEnumeration((StandardEnumeration<?>) value, target);
            }

            // 获取目标类型实现的所有接口
            List<Class<?>> targetInterfaces = Arrays.stream(target.getInterfaces())
                    .<Class<?>>map(Class.class::cast)
                    .toList();
            // 若目标类型实现了 StandardEnumeration 接口，调用 convertToEnumeration 方法进行转换
            if (targetInterfaces.contains(StandardEnumeration.class)) {
                return convertToEnumeration(value, target);
            }

            // 若不需要特殊处理，直接返回原始值
            return value;
        };
    }

    /**
     * 将 StandardEnumeration 类型的源对象转换为目标类型。
     *
     * @param source 源对象，若为 null 则直接返回 null。
     * @param target 目标类型。
     * @return 转换后的对象，若无法转换则返回 null。
     */
    private static Object convertFromEnumeration(StandardEnumeration<?> source, @NonNull Class<?> target) {
        // 若源对象的类型与目标类型相同，直接返回源对象
        if (source.getClass() == target) {
            return source;
        }

        // 获取枚举的值
        Serializable value = source.getValue();
        // 若枚举值的类型与目标类型相同，直接返回枚举值
        if (value != null && value.getClass() == target) {
            return value;
        }

        // 若无法转换，返回 null
        return null;
    }

    /**
     * 将对象转换为 StandardEnumeration 类型的枚举值。
     *
     * @param sourceObject 源对象，若为 null 则直接返回 null。
     * @param target       目标枚举类型，若为 null 则直接返回 null。
     * @return 转换后的枚举值，若无法转换则抛出 IllegalArgumentException 异常。
     */
    private static Object convertToEnumeration(Object sourceObject, @NonNull Class<?> target) {
        // 获取目标枚举类型的所有枚举常量
        Object[] enumConstants = target.getEnumConstants();
        // 遍历枚举常量，查找与源对象值相等的枚举常量
        for (Object enumConstant : enumConstants) {
            StandardEnumeration<?> stdEnum = (StandardEnumeration<?>) enumConstant;
            if (stdEnum.getValue().equals(sourceObject)) {
                return stdEnum;
            }
        }

        throw new IllegalArgumentException("无法将值 " + sourceObject + " 转换为枚举类型 " + target.getName());
    }

}
