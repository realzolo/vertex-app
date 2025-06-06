package com.onezol.vertx.framework.common.util;

import com.onezol.vertx.framework.common.constant.StringConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public final class StringUtils extends org.apache.commons.lang3.StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    /**
     * 下划线转大驼峰 例如：user_name -> UserName
     *
     * @param input 输入字符串
     * @return 大驼峰字符串
     */
    public static String underlineToCamelCase(String input) {
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == StringConstants.C_UNDERLINE) {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    sb.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 下划线转小驼峰 例如：user_name -> userName
     *
     * @param input 输入字符串
     * @return 小驼峰字符串
     */
    public static String underlineToLowerCamelCase(String input) {
        String camelCase = underlineToCamelCase(input);
        return Character.toLowerCase(camelCase.charAt(0)) + camelCase.substring(1);
    }

    /**
     * 驼峰转下划线 例如：UserName -> user_name
     *
     * @param input 输入字符串
     * @return 下划线字符串
     */
    public static String camelCaseToUnderline(String input) {
        if (input == null || EMPTY.equals(input.trim())) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(StringConstants.C_UNDERLINE).append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        // 如果下划线在开头则去掉
        if (sb.charAt(0) == StringConstants.C_UNDERLINE) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    /**
     * 使用正则表达式从输入字符串中获取第一个匹配项的内容。
     *
     * @param input 输入字符串
     * @param regex 正则表达式
     * @return 匹配项的内容，如果未找到匹配项则返回 null
     */
    public static String getMatchedString(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return EMPTY;
        }
    }

    /**
     * 将输入字符串中的空值转换为空字符串。
     * @param input 输入字符串
     * @return 处理后的字符串，如果输入字符串为空则返回空字符串，否则返回原始字符串
     */
    public static String nullToEmpty(String input) {
        return input == null ? EMPTY : input;
    }

}
