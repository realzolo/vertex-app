package com.onezol.vertx.framework.component.storage.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.constant.StringConstants;
import com.onezol.vertx.framework.common.constant.enumeration.FileType;
import com.onezol.vertx.framework.common.skeleton.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.x.file.storage.core.FileInfo;

@Schema(name = "文件记录")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_file_record")
public class FileRecordEntity extends BaseEntity {

    @Schema(name = "名称")
    private String name;

    @Schema(name = "原始名称")
    private String originalName;

    @Schema(name = "大小（字节）")
    private Long size;

    @Schema(name = "文件存储路径")
    private String path;

    @Schema(name = "文件URL")
    private String url;

    @Schema(name = "文件扩展名")
    private String extension;

    @Schema(name = "文件类型")
    private FileType type;

    @Schema(name = "缩略图名称")
    private String thumbnailName;

    @Schema(name = "缩略图大小（字节）")
    private Long thumbnailSize;

    @Schema(name = "缩略图URL")
    private String thumbnailUrl;

    @Schema(name = "存储平台ID")
    private Long storagePlatformId;

    public FileInfo toFileInfo(String platformCode) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setUrl(this.url);
        fileInfo.setSize(this.size);
        fileInfo.setFilename(this.name);
        fileInfo.setOriginalFilename(this.originalName);
        fileInfo.setBasePath(StringConstants.EMPTY);
        fileInfo.setPath(this.path);
        fileInfo.setExt(this.extension);
        fileInfo.setPlatform(platformCode);
        fileInfo.setThUrl(this.thumbnailUrl);
        fileInfo.setThFilename(this.thumbnailName);
        fileInfo.setThSize(this.thumbnailSize);
        return fileInfo;
    }
}
