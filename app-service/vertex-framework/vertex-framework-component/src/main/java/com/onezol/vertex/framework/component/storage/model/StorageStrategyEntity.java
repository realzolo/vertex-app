package com.onezol.vertex.framework.component.storage.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.constant.enumeration.DisEnableStatusEnum;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import com.onezol.vertex.framework.component.storage.annotation.StorageTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_storage_strategy")
public class StorageStrategyEntity extends BaseEntity {

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    @TableField("type")
    private StorageTypeEnum type;

    @TableField("access_key")
    private String accessKey;

    @TableField("secret_key")
    private String secretKey;

    @TableField("endpoint")
    private String endpoint;

    @TableField("bucket_name")
    private String bucketName;

    @TableField("root_path")
    private String rootPath;

    @TableField("domain")
    private String domain;

    @TableField("is_default")
    private Boolean isDefault;

    @TableField("sort")
    private Integer sort;

    @TableField("remark")
    private String remark;

    @TableField("status")
    private DisEnableStatusEnum status;

}
