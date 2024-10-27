package com.onezol.vertex.framework.common.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

/**
 * 时间工具类，用于 {@link LocalDateTime}
 */
public class LocalDateTimeUtils {

    public static LocalDateTime EMPTY = buildTime(1970, 1, 1);

    public static LocalDateTime addTime(Duration duration) {
        return LocalDateTime.now().plus(duration);
    }

    public static LocalDateTime minusTime(Duration duration) {
        return LocalDateTime.now().minus(duration);
    }

    public static boolean beforeNow(LocalDateTime date) {
        return date.isBefore(LocalDateTime.now());
    }

    public static boolean afterNow(LocalDateTime date) {
        return date.isAfter(LocalDateTime.now());
    }

    /**
     * 创建指定时间
     *
     * @param year  年
     * @param mouth 月
     * @param day   日
     * @return 指定时间
     */
    public static LocalDateTime buildTime(int year, int mouth, int day) {
        return LocalDateTime.of(year, mouth, day, 0, 0, 0);
    }

    public static LocalDateTime[] buildBetweenTime(int year1, int mouth1, int day1, int year2, int mouth2, int day2) {
        return new LocalDateTime[]{buildTime(year1, mouth1, day1), buildTime(year2, mouth2, day2)};
    }

    /**
     * 判断当前时间是否在该时间范围内
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 是否
     */
    public static boolean isBetween(LocalDateTime startTime, LocalDateTime endTime) {
        throw new UnsupportedOperationException("Not implement yet");
    }

    /**
     * 判断当前时间是否在该时间范围内
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 是否
     */
    public static boolean isBetween(String startTime, String endTime) {
        throw new UnsupportedOperationException("Not implement yet");
    }

    /**
     * 判断时间段是否重叠
     *
     * @param startTime1 开始 time1
     * @param endTime1   结束 time1
     * @param startTime2 开始 time2
     * @param endTime2   结束 time2
     * @return 重叠：true 不重叠：false
     */
    public static boolean isOverlap(LocalTime startTime1, LocalTime endTime1, LocalTime startTime2, LocalTime endTime2) {
        throw new UnsupportedOperationException("Not implement yet");
    }

    /**
     * 获取指定日期所在的月份的开始时间
     * 例如：2023-09-30 00:00:00,000
     *
     * @param date 日期
     * @return 月份的开始时间
     */
    public static LocalDateTime beginOfMonth(LocalDateTime date) {
        return date.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
    }

    /**
     * 获取指定日期所在的月份的最后时间
     * 例如：2023-09-30 23:59:59,999
     *
     * @param date 日期
     * @return 月份的结束时间
     */
    public static LocalDateTime endOfMonth(LocalDateTime date) {
        return date.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    }


    /**
     * 计算时间差
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 时间差
     */
    public static String timeDifference(LocalDateTime startTime, LocalDateTime endTime) {
        long between = java.time.Duration.between(startTime, endTime).toMillis();
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return day + "天" + hour + "小时" + min + "分" + s + "秒";
    }

    /**
     * 计算短时间差(去除0值)
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 时间差
     */
    public static String shortTimeDifference(LocalDateTime startTime, LocalDateTime endTime) {
        String difference = LocalDateTimeUtils.timeDifference(startTime, endTime);
        String[] split = difference.split("天");
        String day = split[0];
        String[] split1 = split[1].split("小时");
        String hour = split1[0];
        String[] split2 = split1[1].split("分");
        String min = split2[0];
        String[] split3 = split2[1].split("秒");
        String s = split3[0];
        StringBuilder sb = new StringBuilder();
        if (!"0".equals(day)) {
            sb.append(day).append("天");
        }
        if (!"0".equals(hour)) {
            sb.append(hour).append("小时");
        }
        if (!"0".equals(min)) {
            sb.append(min).append("分");
        }
        if (!"0".equals(s)) {
            sb.append(s).append("秒");
        }
        return sb.toString();
    }

}
