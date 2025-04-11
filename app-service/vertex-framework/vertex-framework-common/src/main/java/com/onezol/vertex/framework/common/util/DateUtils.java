package com.onezol.vertex.framework.common.util;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public final class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private DateUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    /**
     * 时区 - 默认
     */
    public static final String TIME_ZONE_DEFAULT = "GMT+8";

    /**
     * 将 LocalDateTime 转换成 Date
     *
     * @param date LocalDateTime
     * @return LocalDateTime
     */
    public static Date of(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        // 将此日期时间与时区相结合以创建 ZonedDateTime
        ZonedDateTime zonedDateTime = date.atZone(ZoneId.systemDefault());
        // 本地时间线 LocalDateTime 到即时时间线 Instant 时间戳
        Instant instant = zonedDateTime.toInstant();
        // UTC时间(世界协调时间,UTC + 00:00)转北京(北京,UTC + 8:00)时间
        return Date.from(instant);
    }

    /**
     * 将 Date 转换成 LocalDateTime
     *
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime of(Date date) {
        if (date == null) {
            return null;
        }
        // 转为时间戳
        Instant instant = date.toInstant();
        // UTC时间(世界协调时间,UTC + 00:00)转北京(北京,UTC + 8:00)时间
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 添加时间
     *
     * @param duration 时间间隔
     * @return 时间
     */
    public static Date addTime(Duration duration) {
        return new Date(System.currentTimeMillis() + duration.toMillis());
    }

    /**
     * 传入时间与当前时间比较, 判断是否过期
     *
     * @param time 时间
     * @return 是否过期
     */
    public static boolean isExpired(Date time) {
        return System.currentTimeMillis() > time.getTime();
    }

    /**
     * 传入时间与当前时间比较, 判断是否过期
     *
     * @param localDateTime 时间
     * @return 是否过期
     */
    public static boolean isExpired(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(localDateTime);
    }

    public static long diff(Date endTime, Date startTime) {
        return endTime.getTime() - startTime.getTime();
    }

    /**
     * 创建指定时间
     *
     * @param year  年
     * @param mouth 月
     * @param day   日
     * @return 指定时间
     */
    public static Date buildDate(int year, int mouth, int day) {
        return buildDate(year, mouth, day, 0, 0, 0);
    }

    /**
     * 创建指定时间
     *
     * @param year   年
     * @param mouth  月
     * @param day    日
     * @param hour   小时
     * @param minute 分钟
     * @param second 秒
     * @return 指定时间
     */
    public static Date buildDate(int year, int mouth, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, mouth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date max(Date a, Date b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.compareTo(b) > 0 ? a : b;
    }

    public static LocalDateTime max(LocalDateTime a, LocalDateTime b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.isAfter(b) ? a : b;
    }

    /**
     * 计算当期时间相差的日期
     *
     * @param field  日历字段.<br/>eg:Calendar.MONTH,Calendar.DAY_OF_MONTH,<br/>Calendar.HOUR_OF_DAY等.
     * @param amount 相差的数值
     * @return 计算后的日志
     */
    public static Date addDate(int field, int amount) {
        return addDate(null, field, amount);
    }

    /**
     * 计算当期时间相差的日期
     *
     * @param date   设置时间
     * @param field  日历字段 例如说，{@link Calendar#DAY_OF_MONTH} 等
     * @param amount 相差的数值
     * @return 计算后的日志
     */
    public static Date addDate(Date date, int field, int amount) {
        if (amount == 0) {
            return date;
        }
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        c.add(field, amount);
        return c.getTime();
    }

    /**
     * 传入时间是否是今天
     *
     * @param date 日期
     * @return 是否
     */
    public static boolean isToday(Date date) {
        return isToday(of(date));
    }

    /**
     * 传入时间是否是今天
     *
     * @param date 日期
     * @return 是否
     */
    public static boolean isToday(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = LocalDateTime.of(now.toLocalDate(), LocalTime.MIN);
        LocalDateTime tomorrow = today.plusDays(1);
        return date.isAfter(today) && date.isBefore(tomorrow);
    }

    /**
     * 格式化时间
     * <p>
     *
     * @param date    日期
     * @param pattern 格式
     * @return 格式化后的时间
     */
    public static String format(LocalDateTime date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("date is null");
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime add(Duration duration) {
        return LocalDateTime.now().plus(duration);
    }

    public static LocalDateTime minus(Duration duration) {
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
    public static LocalDateTime buildLocalDateTime(int year, int mouth, int day) {
        return LocalDateTime.of(year, mouth, day, 0, 0, 0);
    }


    /**
     * 创建指定时间范围
     *
     * @param year1  年
     * @param mouth1 月
     * @param day1   日
     * @param year2  年
     * @param mouth2 月
     * @param day2   日
     * @return 时间范围
     */
    public static LocalDateTime[] buildLocalDateTimeRange(int year1, int mouth1, int day1, int year2, int mouth2, int day2) {
        return new LocalDateTime[]{buildLocalDateTime(year1, mouth1, day1), buildLocalDateTime(year2, mouth2, day2)};
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
        String difference = timeDifference(startTime, endTime);
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
