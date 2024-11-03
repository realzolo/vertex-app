package com.onezol.vertex.framework.common.util;

import com.onezol.vertex.framework.common.constant.enumeration.Enumeration;
import org.springframework.cglib.beans.BeanCopier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

/**
 * 对象转换工具类
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

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
        BeanCopier copier = BeanCopier.create(source.getClass(), targetType, true);
        try {
            Constructor<T> constructor = targetType.getDeclaredConstructor();
            T newInstance = constructor.newInstance();
            copier.copy(source, newInstance, (value, target, context) -> {
                if (value instanceof Enumeration<?> && target != Enumeration.class) {
                    return ((Enumeration<?>) value).getValue();
                }
                if (Arrays.stream(target.getInterfaces()).toList().contains(Enumeration.class) && !(value instanceof Enumeration<?>)) {
                    Object[] enumConstants = target.getEnumConstants();
                    for (Object enumConstant : enumConstants) {
                        Enumeration<?> aEnum = (Enumeration<?>) enumConstant;
                        if (aEnum.getValue().equals(value)) {
                            return aEnum;
                        }
                    }
                }
                return value;
            });
            return newInstance;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将源对象集合换为目标对象集合
     *
     * @param source 源对象集合
     * @return 转换后的目标对象List
     */
    public static <S, T> Collection<T> toList(Collection<S> source, Class<T> targetType) {
        return source.stream().map(item -> toBean(item, targetType)).toList();
    }
}
