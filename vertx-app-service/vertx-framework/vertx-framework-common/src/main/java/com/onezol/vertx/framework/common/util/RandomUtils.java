package com.onezol.vertx.framework.common.util;

import java.security.SecureRandom;
import java.util.Arrays;

public final class RandomUtils {

    // 预定义字符池
    private static final char[] NUMERIC_POOL = "0123456789".toCharArray();
    private static final char[] LETTERS_POOL =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] LOWERCASE_POOL =
            "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] UPPERCASE_POOL =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] ALPHANUMERIC_POOL =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private static final SecureRandom RANDOM = new SecureRandom();

    private RandomUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }


    /**
     * 生成纯数字随机字符串
     *
     * @param length 字符串长度（必须大于0）
     * @return 纯数字随机字符串
     * @throws IllegalArgumentException 如果长度不合法
     */
    public static String randomNumbers(int length) {
        return generate(length, NUMERIC_POOL);
    }

    /**
     * 生成混合大小写字母的随机字符串
     *
     * @param length 字符串长度（必须大于0）
     * @return 大小写混合的字母字符串
     * @throws IllegalArgumentException 如果长度不合法
     */
    public static String randomStrings(int length) {
        return generate(length, LETTERS_POOL);
    }

    /**
     * 生成小写字母随机字符串
     *
     * @param length 字符串长度
     * @return 全小写字母字符串
     */
    public static String randomLowercase(int length) {
        return generate(length, LOWERCASE_POOL);
    }

    /**
     * 生成大写字母随机字符串
     *
     * @param length 字符串长度
     * @return 全大写字母字符串
     */
    public static String randomUppercase(int length) {
        return generate(length, UPPERCASE_POOL);
    }

    /**
     * 生成字母数字混合随机字符串
     *
     * @param length 字符串长度
     * @return 包含数字和字母的字符串
     */
    public static String randomAlphanumeric(int length) {
        return generate(length, ALPHANUMERIC_POOL);
    }

    /**
     * 核心生成方法（通用逻辑）
     *
     * @param length 字符串长度
     * @param pool   字符池
     * @return 随机字符串
     */
    private static String generate(int length, char[] pool) {
        validateLength(length);
        final int poolSize = pool.length;
        final char[] buffer = new char[length];

        // 批量生成随机数优化性能
        for (int i = 0; i < length; ) {
            int randomInt = RANDOM.nextInt();
            // 每次处理4字节（32位）的随机数据
            for (int j = 0; j < 4 && i < length; j++) {
                int index = (randomInt & 0xFF) % poolSize; // 取低8位计算索引
                buffer[i++] = pool[index];
                randomInt >>= 8; // 右移8位处理下一个字节
            }
        }
        return new String(buffer);
    }

    /**
     * 验证长度参数
     *
     * @param length 输入长度
     * @throws IllegalArgumentException 如果长度不合法
     */
    private static void validateLength(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive: " + length);
        }
    }

    /**
     * 安全擦除字符数组（防止内存泄漏敏感数据）
     *
     * @param arr 需要清理的数组
     */
    public static void secureWipe(char[] arr) {
        if (arr != null) {
            // 用空字符覆盖
            Arrays.fill(arr, '\0');
        }
    }

}
