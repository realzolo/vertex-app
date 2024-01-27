package com.onezol.vertex.framework.common.util;


import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResourceUtils extends org.springframework.util.ResourceUtils {

    /**
     * 从classpath/resource读取文件内容为字符串
     *
     * @param resourceLocation 文件路径(相对于classpath/resource), 如: static/test.txt
     * @return 文件内容
     */
    public static String readAsText(String resourceLocation) throws IOException {
        File file = getFile(resourceLocation);
        return StreamUtils.copyToString(new FileInputStream(file), StandardCharsets.UTF_8);
    }

    /**
     * 从classpath/resource读取文件内容为字节数组
     *
     * @param path 文件路径(相对于classpath/resource), 如: static/test.txt
     */
    public static byte[] readAsByte(String path) throws IOException {
        File file = getFile(path);
        return StreamUtils.copyToByteArray(new FileInputStream(file));
    }
}
