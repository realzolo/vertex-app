package com.onezol.vertx.framework.common.constant.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Schema(name = "文件类型")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "文件类型", value = "file_type")
public enum FileType implements StandardEnumeration<String> {

    OTHER("其它", "OTHER", Collections.emptyList()),

    IMAGE("图片", "IMAGE", List.of("jpg", "jpeg", "png", "gif", "bmp", "webp", "ico", "psd", "tiff", "dwg", "jxr", "apng", "xcf")),

    DOC("文档", "DOC", List.of("txt", "pdf", "doc", "xls", "ppt", "docx", "xlsx", "pptx")),

    VIDEO("视频", "VIDEO", List.of("mp4", "avi", "mkv", "flv", "webm", "wmv", "m4v", "mov", "mpg", "rmvb", "3gp")),

    AUDIO("音频", "AUDIO", List.of("mp3", "flac", "wav", "ogg", "midi", "m4a", "aac", "amr", "ac3", "aiff")),

    ZIP("压缩包", "ZIP", List.of("zip", "rar", "7z", "tar", "gz", "bz2", "xz", "lz", "lzma", "lzo", "ar", "iso", "dmg"));

    private final String name;

    @EnumValue
    private final String value;

    private final List<String> extensions;

    /**
     * 根据扩展名查询
     *
     * @param extension 扩展名
     * @return 文件类型
     */
    public static FileType getByExtension(String extension) {
        if (StringUtils.isBlank(extension)) {
            return OTHER;
        }
        return Arrays.stream(FileType.values())
                .filter(t -> t.getExtensions().contains(extension.toLowerCase()))
                .findFirst()
                .orElse(FileType.OTHER);
    }

}
