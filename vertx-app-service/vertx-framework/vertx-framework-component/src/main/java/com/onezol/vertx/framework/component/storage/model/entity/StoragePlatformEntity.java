package com.onezol.vertx.framework.component.storage.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.constant.enumeration.DisEnableStatus;
import com.onezol.vertx.framework.common.skeleton.model.BaseEntity;
import com.onezol.vertx.framework.component.storage.annotation.StorageType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_storage_platform")
public class StoragePlatformEntity extends BaseEntity {

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    @TableField("type")
    private StorageType type;

    @TableField("access_key")
    private String accessKey;

    @TableField("secret_key")
    private String secretKey;

    @TableField("endpoint")
    private String endpoint;

    @TableField("bucket_name")
    private String bucketName;

    @TableField("domain")
    private String domain;

    @TableField("is_default")
    private Boolean isDefault;

    @TableField("sort")
    private Integer sort;

    @TableField("remark")
    private String remark;

}
