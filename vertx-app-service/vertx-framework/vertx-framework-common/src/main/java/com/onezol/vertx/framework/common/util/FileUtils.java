package com.onezol.vertx.framework.common.util;

import java.io.File;

public final class FileUtils {

    private FileUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    private static final CharSequence[] SPECIAL_SUFFIX = new CharSequence[]{"tar.bz2", "tar.Z", "tar.gz", "tar.xz"};

    /**
     * 获取文件后缀
     * @param file 文件
     * @return 文件后缀
     */
    public static String getExtName(File file) {
        if (null == file) {
            return null;
        } else {
            return file.isDirectory() ? null : getExtName(file.getName());
        }
    }

    /**
     * 获取文件后缀
     * @param fileName 文件名称
     * @return 文件后缀
     */
    public static String getExtName(String fileName) {
        if (fileName == null) {
            return null;
        } else {
            int index = fileName.lastIndexOf(".");
            if (index == -1) {
                return "";
            } else {
                int secondToLastIndex = fileName.substring(0, index).lastIndexOf(".");
                String substr = fileName.substring(secondToLastIndex == -1 ? index : secondToLastIndex + 1);
                if (StringUtils.containsAny(substr, SPECIAL_SUFFIX)) {
                    return substr;
                } else {
                    String ext = fileName.substring(index + 1);
                    return StringUtils.containsAny(ext, '/', '\\') ? "" : ext;
                }
            }
        }
    }
}
