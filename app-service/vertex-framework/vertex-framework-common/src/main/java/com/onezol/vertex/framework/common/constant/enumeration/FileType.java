package com.onezol.vertex.framework.common.constant.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import com.onezol.vertex.framework.common.util.StringUtils;
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
public enum FileType implements StandardEnumeration<Integer> {

    /**
     * 其他
     */
    UNKNOWN("其他", 0, Collections.emptyList()),

    /**
     * 图片
     */
    IMAGE("图片", 1, List
            .of("jpg", "jpeg", "png", "gif", "bmp", "webp", "ico", "psd", "tiff", "dwg", "jxr", "apng", "xcf")),

    /**
     * 文档
     */
    DOC("文档", 2, List.of("txt", "pdf", "doc", "xls", "ppt", "docx", "xlsx", "pptx")),

    /**
     * 视频
     */
    VIDEO("视频", 3, List.of("mp4", "avi", "mkv", "flv", "webm", "wmv", "m4v", "mov", "mpg", "rmvb", "3gp")),

    /**
     * 音频
     */
    AUDIO("音频", 4, List.of("mp3", "flac", "wav", "ogg", "midi", "m4a", "aac", "amr", "ac3", "aiff"));


    private final String name;

    @EnumValue
    private final Integer value;

    private final List<String> extensions;

    /**
     * 根据扩展名查询
     *
     * @param extension 扩展名
     * @return 文件类型
     */
    public static FileType getByExtension(String extension) {
        if (StringUtils.isBlank(extension)) {
            return UNKNOWN;
        }
        return Arrays.stream(FileType.values())
                .filter(t -> t.getExtensions().contains(extension.toLowerCase()))
                .findFirst()
                .orElse(FileType.UNKNOWN);
    }

}
