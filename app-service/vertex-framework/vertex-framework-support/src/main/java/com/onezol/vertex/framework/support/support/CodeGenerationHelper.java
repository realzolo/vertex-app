package com.onezol.vertex.framework.support.support;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 全局编码生成器, 如用户编码, 组织机构编码等
 */
public final class CodeGenerationHelper {

    private CodeGenerationHelper() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    /**
     * 生成编码
     *
     * @return 编码
     */
    public static String generateCode() {
        throw new UnsupportedOperationException("暂不支持该业务类型");
    }

    /**
     * 生成用户编码。策略：9位数字，首位不为0
     *
     * @return 用户编码
     */
    private static String generateUserCode() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

}
