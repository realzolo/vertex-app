package com.onezol.vertx.framework.component.storage.helper;

import com.onezol.vertx.framework.common.constant.DatePattern;
import com.onezol.vertx.framework.common.constant.GenericConstants;
import com.onezol.vertx.framework.common.constant.StringConstants;
import com.onezol.vertx.framework.common.util.DateUtils;
import com.onezol.vertx.framework.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

@Slf4j
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
     * 修复本地路径
     *
     * @param localPath 本地路径
     * @return 修复后的本地路径
     */
    public static String fixLocalPath(String localPath) {
        if (StringUtils.isBlank(localPath) || Objects.equals(localPath, StringConstants.SLASH)) {
            return StringConstants.SLASH;
        }
        // 将路径中的反斜杠替换为正斜杠
        localPath = localPath.replaceAll("\\\\", StringConstants.SLASH);
        if (!localPath.endsWith(StringConstants.SLASH)) {
            localPath = localPath + StringConstants.SLASH;
        }
        return localPath;
    }

    /**
     * 获取缩略图名称
     *
     * @param thumbnailUrl 缩略图地址
     */
    public static String getThumbnailName(String thumbnailUrl) {
        if (StringUtils.isBlank(thumbnailUrl)) {
            return StringConstants.EMPTY;
        }
        int index = thumbnailUrl.lastIndexOf(StringConstants.SLASH);
        if (index == -1) {
            return StringConstants.EMPTY;
        }
        return thumbnailUrl.substring(index + 1);
    }

    /**
     * 获取本地文件API访问前缀
     * @param domain 域名
     */
    public static String getLocalFileApiPrefix(String domain) {
        if (StringUtils.isBlank(domain)) {
            return StringConstants.EMPTY;
        }
        if (domain.startsWith(GenericConstants.PROTOCOL_HTTP) || domain.startsWith(GenericConstants.PROTOCOL_HTTPS)) {
            try {
                URI uri = new URI(domain);
                String path = uri.getPath();
                if (path == null || path.isEmpty()) {
                    return StringConstants.EMPTY;
                } else {
                    return path;
                }
            } catch (URISyntaxException e) {
                log.error("[StorageHelper] 获取本地文件API访问前缀失败", e);
                return StringConstants.EMPTY;
            }
        } else {
            return domain;
        }
    }

}
