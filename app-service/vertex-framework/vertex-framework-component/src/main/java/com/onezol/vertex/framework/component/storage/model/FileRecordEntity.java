package com.onezol.vertex.framework.component.storage.model;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.constant.StringConstants;
import com.onezol.vertex.framework.common.constant.enumeration.FileTypeEnum;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.x.file.storage.core.FileInfo;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_file_record")
public class FileRecordEntity extends BaseEntity {
    /**
     * 名称
     */
    private String name;

    /**
     * 大小（字节）
     */
    private Long size;

    /**
     * URL
     */
    private String url;

    /**
     * 扩展名
     */
    private String extension;

    /**
     * 类型
     */
    private FileTypeEnum type;

    /**
     * 缩略图大小（字节)
     */
    private Long thumbnailSize;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;

    /**
     * 存储策略 ID
     */
    private Long storageStrategyId;

    public FileInfo toFileInfo(String storageCode) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setUrl(this.url);
        fileInfo.setSize(this.size);
        fileInfo.setFilename(StrUtil.contains(this.url, StringConstants.SLASH)
                ? StrUtil.subAfter(this.url, StringConstants.SLASH, true)
                : this.url);
        fileInfo.setOriginalFilename(this.name + StringConstants.DOT + this.extension);
        fileInfo.setBasePath(StringConstants.EMPTY);
        fileInfo.setPath(StrUtil.subBefore(this.url, StringConstants.SLASH, true) + StringConstants.SLASH);
        fileInfo.setExt(this.extension);
        fileInfo.setPlatform(storageCode);
        fileInfo.setThUrl(this.thumbnailUrl);
        fileInfo.setThFilename(StrUtil.contains(this.thumbnailUrl, StringConstants.SLASH)
                ? StrUtil.subAfter(this.thumbnailUrl, StringConstants.SLASH, true)
                : this.thumbnailUrl);
        fileInfo.setThSize(this.thumbnailSize);
        return fileInfo;
    }
}
