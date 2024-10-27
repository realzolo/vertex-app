package com.onezol.vertex.framework.common.helper;

import com.onezol.vertex.framework.common.constant.enums.BizCode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 全局编码生成器, 如用户编码, 组织机构编码等
 */
public final class CodeGenerationHelper {

    /**
     * 生成编码
     *
     * @param typeCode 类型编码, 取自{@link com.onezol.vertex.framework.common.constant.enums.BizCode}的code
     * @return 编码
     */
    public static String generateCode(BizCode bizCode) {
        Objects.requireNonNull(bizCode);

        // 用户编码
        if (BizCode.USER_CODE.getCode() == bizCode.getCode()) {
            return generateUserCode();
        }
        // .
        else {
            return "";
        }
    }

    /**
     * 生成用户编码。策略：9位数字，首位不为0
     *
     * @return 用户编码
     */
    private static String generateUserCode() {
        // TODO: 临时方案，后续优化
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

}
