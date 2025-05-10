package com.onezol.vertx.framework.common.util;

import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import org.springframework.cglib.beans.BeanCopier;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 对象转换工具类
 */
public final class BeanUtils extends org.springframework.beans.BeanUtils {

    private BeanUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    /**
     * 将源对象换为目标对象
     *
     * @param source     源对象
     * @param targetType 目标对象类型
     * @return 转后的目标对象
     */
    public static <S, T> T toBean(S source, Class<T> targetType) {
        if (source == null || targetType == null) {
            return null;
        }
        BeanCopier copier = BeanCopier.create(source.getClass(), targetType, true);
        try {
            Constructor<T> constructor = targetType.getDeclaredConstructor();
            T newInstance = constructor.newInstance();
            copier.copy(source, newInstance, (sourceObject, target, context) -> {
                List<Class> targetTypes = Arrays.stream(target.getInterfaces()).toList();
                if (sourceObject instanceof StandardEnumeration<?>) {
                    // 如果目标类型和源类型相同，则直接返回源对象
                    if (sourceObject.getClass() == target) {
                        return sourceObject;
                    }
                    // 目标类型与枚举value的类型相同，则直接返回枚举value
                    Serializable value = ((StandardEnumeration<?>) sourceObject).getValue();
                    if (value.getClass() == target) {
                        return value;
                    }
                    // 无法转换，返回null
                    return null;
                } else if (targetTypes.contains(StandardEnumeration.class)) {
                    Object[] enumConstants = target.getEnumConstants();
                    for (Object enumConstant : enumConstants) {
                        StandardEnumeration<?> aEnum = (StandardEnumeration<?>) enumConstant;
                        if (aEnum.getValue().equals(sourceObject)) {
                            return aEnum;
                        }
                    }
                    throw new RuntimeException("无法转换的枚举类型！");
                }
                return sourceObject;
            });
            return newInstance;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("类型转换失败！" + e.getMessage());
        }
    }

    /**
     * 将源对象集合换为目标对象集合
     *
     * @param source 源对象集合
     * @return 转换后的目标对象List
     */
    public static <S, T> List<T> toList(Collection<S> source, Class<T> targetType) {
        return source.stream().map(item -> toBean(item, targetType)).toList();
    }

}
