package com.onezol.vertx.framework.component.storage.helper;

import com.onezol.vertx.framework.common.constant.DatePattern;
import com.onezol.vertx.framework.common.constant.StringConstants;
import com.onezol.vertx.framework.common.util.DateUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public final class StorageHelper {

    /**
     * 生成文件名
     *
     * @return 文件名
     */
    public static String generateFileName() {
        long timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long uniqueValue = timestamp << 2 ^ Calendar.DAY_OF_WEEK;
        return Long.toHexString(uniqueValue).toUpperCase(Locale.CHINA);
    }

    /**
     * 生成文件路径
     *
     * @return 文件路径
     */
    public static String generateFilePath() {
        String dateFormatStr = DateUtils.format(LocalDateTime.now(), DatePattern.YYYYMMDD);
        long v = Long.parseLong(dateFormatStr);
        long uniqueValue = v << 2 ^ Calendar.DAY_OF_YEAR;
        return StringConstants.SLASH + uniqueValue + StringConstants.SLASH;
    }

    /**
     * 修复域名(去除尾部斜杠)
     *
     * @param domain 域名
     * @return 修复后的域名
     */
    public static String fixDomain(String domain) {
        if (domain == null) {
            return StringConstants.EMPTY;
        }
        if (domain.endsWith(StringConstants.SLASH)) {
            domain = domain.substring(0, domain.length() - 1);
        }
        return domain;
    }

    /**
     * 修复根路径
     *
     * @param rootPath 根路径
     * @return 修复后的根路径
     */
    public static String fixRootPath(String rootPath) {
        if (rootPath == null || Objects.equals(rootPath, StringConstants.SLASH)) {
            return StringConstants.EMPTY;
        }
        if (!rootPath.startsWith(StringConstants.SLASH)) {
            rootPath = StringConstants.SLASH + rootPath;
        }
        return rootPath;
    }

}
