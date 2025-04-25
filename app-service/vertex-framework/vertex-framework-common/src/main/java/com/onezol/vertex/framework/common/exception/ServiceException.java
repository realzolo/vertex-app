package com.onezol.vertex.framework.common.exception;


import com.onezol.vertex.framework.common.constant.enumeration.ServiceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "服务异常", description = "服务异常: 此类型异常信息需要反馈给前端展示")
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class ServiceException extends RuntimeException {

    @Schema(name = "异常状态码", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Integer code;

    @Schema(name = "异常信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String message;

    public ServiceException(String message) {
        this.code = ServiceStatus.INTERNAL_SERVER_ERROR.getValue();
        this.message = message;
    }

    public ServiceException(ServiceStatus serviceStatus) {
        this.code = serviceStatus.getValue();
        this.message = serviceStatus.getDescription();
    }

    public ServiceException(ServiceStatus serviceStatus, String message) {
        this.code = serviceStatus.getValue();
        this.message = message;
    }

}
