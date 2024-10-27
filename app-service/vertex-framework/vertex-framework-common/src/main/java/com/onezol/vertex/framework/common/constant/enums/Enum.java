package com.onezol.vertex.framework.common.constant.enums;

/**
 * 枚举接口, 提供枚举类的基础方法
 */
public interface Enum {

    /**
     * 根据code获取枚举类
     *
     * @param enumClass 枚举类
     * @param code      枚举Code
     * @return 枚举类
     */
    static <T extends Enum> T getEnumByCode(Class<T> enumClass, int code) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T enumConstant : enumConstants) {
            if (enumConstant.getCode() == code) {
                return enumConstant;
            }
        }
        return null;
    }

    /**
     * 根据value获取枚举类
     *
     * @param enumClass 枚举类
     * @param value     枚举value
     * @return 枚举类
     */
    static <T extends Enum> T getEnumByValue(Class<T> enumClass, String value) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T enumConstant : enumConstants) {
            if (enumConstant.getValue().equals(value)) {
                return enumConstant;
            }
        }
        return null;
    }

    /**
     * 判断code是否存在
     *
     * @param enumClass 枚举类
     * @param code      枚举Code
     * @return 是否存在
     */
    static <T extends Enum> boolean isEnumCode(Class<T> enumClass, int code) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T enumConstant : enumConstants) {
            if (enumConstant.getCode() == code) {
                return true;
            }
        }
        return false;
    }

    int getCode();

    String getValue();
}
