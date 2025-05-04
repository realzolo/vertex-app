package com.onezol.vertx.framework.support.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

/**
 * P6spy SQL 格式化器
 */
public class P6spySqlFormatter implements MessageFormattingStrategy {
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (StringUtils.isNotBlank(sql)) {
            String timestamp = formatFullTime(LocalDateTime.now(), DATE_TIME_FORMATTER);
            return timestamp +
                   " | 连接ID: " + connectionId +
                   " | 类别: " + category + 
                   " | 耗时: " + elapsed + " ms" + 
                   " | SQL 语句: \n" + sql.replaceAll("\\s+", " ") + ";";
        }
        return "";
    }

    public static String formatFullTime(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        if (localDateTime == null) {
            return "";
        }
        return localDateTime.format(formatter);
    }
}
