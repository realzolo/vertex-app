package com.onezol.vertex.framework.common.util;


import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResourceUtils extends org.springframework.util.ResourceUtils {

    /**
     * 获取项目根路径
     *
     * @param path 路径
     * @return 文件内容
     */
    public static String readAsText(String path) throws IOException {
        Objects.requireNonNull(path, "path must not be null");

        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 从classpath/resource读取文件内容为字符串
     *
     * @param resourceLocation 文件路径(相对于classpath/resource), 如: static/test.txt
     * @return 文件内容
     */
    public static String readClassPathResourceAsText(String resourceLocation) throws IOException {
        Objects.requireNonNull(resourceLocation, "resource location must not be null");
        File file = getFile(resourceLocation);
        return StreamUtils.copyToString(new FileInputStream(file), StandardCharsets.UTF_8);
    }

    /**
     * 从classpath/resource读取文件内容为字节数组
     *
     * @param resourceLocation 文件路径(相对于classpath/resource), 如: static/test.txt
     */
    public static byte[] readClassPathResourceAsByte(String resourceLocation) throws IOException {
        Objects.requireNonNull(resourceLocation, "resource location must not be null");

        File file = getFile(resourceLocation);
        return StreamUtils.copyToByteArray(new FileInputStream(file));
    }
}
