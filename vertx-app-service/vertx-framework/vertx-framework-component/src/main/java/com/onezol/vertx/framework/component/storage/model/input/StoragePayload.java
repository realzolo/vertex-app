package com.onezol.vertx.framework.component.storage.model.input;

import com.onezol.vertx.framework.component.storage.annotation.StorageType;
import lombok.Data;

import java.io.Serializable;

@Data
public class StoragePayload implements Serializable {

    private Long id;

    private String name;

    private String code;

    private StorageType type;

    private String accessKey;

    private String secretKey;

    private String endpoint;

    private String bucketName;

    private String domain;

    private Boolean isDefault;

    private Integer sort;

    private String remark;

    private Integer status;

}
