package com.onezol.vertx.framework.component.storage.model.dto;

import com.onezol.vertx.framework.common.skeleton.model.BaseDTO;
import com.onezol.vertx.framework.component.storage.annotation.StorageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "存储平台")
@Data
@EqualsAndHashCode(callSuper = true)
public class StoragePlatform extends BaseDTO {

    @Schema(name = "存储名称")
    private String name;

    @Schema(name = "存储编码")
    private String code;

    @Schema(name = "存储类型")
    private StorageType type;

    @Schema(name = "accessKey")
    private String accessKey;

    @Schema(name = "secretKey")
    private String secretKey;

    @Schema(name = "端点")
    private String endpoint;

    @Schema(name = "存储桶名称(本地存储的存储路径)")
    private String bucketName;

    @Schema(name = "访问域名")
    private String domain;

    @Schema(name = "是否默认存储")
    private Boolean isDefault;

    @Schema(name = "排序")
    private Integer sort;

    @Schema(name = "备注")
    private String remark;

}
