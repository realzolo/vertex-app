package com.onezol.vertex.framework.common.exception;


import com.onezol.vertex.framework.common.constant.enums.BizHttpStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常，用于抛出业务相关的异常。此类型异常信息需要反馈给前端展示
 */

@Schema(description = "业务异常")
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BusinessException extends RuntimeException {

    @Schema(description = "异常状态码", requiredMode = Schema.RequiredMode.REQUIRED)
    private final int code;

    @Schema(description = "异常信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String message;

    public BusinessException() {
        this.code = BizHttpStatus.INTERNAL_SERVER_ERROR.getCode();
        this.message = BizHttpStatus.INTERNAL_SERVER_ERROR.getValue();
    }

    public BusinessException(String message) {
        this.code = BizHttpStatus.INTERNAL_SERVER_ERROR.getCode();
        this.message = message;
    }

    public BusinessException(BizHttpStatus bizHttpStatus) {
        this.code = bizHttpStatus.getCode();
        this.message = bizHttpStatus.getValue();
    }

    public BusinessException(BizHttpStatus bizHttpStatus, String message) {
        this.code = bizHttpStatus.getCode();
        this.message = message;
    }

}
