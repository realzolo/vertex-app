package com.onezol.vertx.framework.common.util;

/**
 * 反射工具类
 */
public final class ReflectionUtils {

    /**
     * 根据类路径获取类信息
     *
     * @param classPath 类路径
     */
    public static Class<?> getClass(String classPath) {
        try {
            return Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据类信息创建实例
     *
     * @param clazz 类信息
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
