package com.emily.infrastructure.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

/**
 * 日期比较工具类，比较两个日期大小
 *
 * @author Emily
 * @since Created in 2023/5/14 5:14 PM
 */
public class DateCompareUtils {
    /**
     * 比较日期大小，firstDate大于等于secondDate 返回true,否则返回false
     *
     * @param date1   日期字符串
     * @param date2   日期字符串
     * @param pattern 日期格式
     * @return 1:date1&gt;date2、0:date1=date2 -1:date1&lt;date2
     */
    public static int compareTo(String date1, String date2, String pattern) {
        DateAssert.illegalArgument(date1, "非法参数");
        DateAssert.illegalArgument(date2, "非法参数");
        DateAssert.illegalArgument(pattern, "非法参数");
        try {
            DateFormat sdf = new SimpleDateFormat(pattern);
            Date first = sdf.parse(date1);
            Date second = sdf.parse(date2);
            return first.compareTo(second);
        } catch (ParseException e) {
            throw new IllegalArgumentException("非法参数");
        }
    }

    /**
     * 比较日期大小，firstDate大于等于secondDate 返回true,否则返回false
     *
     * @param date1 日期字符串
     * @param date2 日期字符串
     * @return 1:date1&gt;date2、0:date1=date2 -1:date1&lt;date2
     */
    public static int compareTo(Date date1, Date date2) {
        DateAssert.illegalArgument(date1, "非法参数");
        DateAssert.illegalArgument(date2, "非法参数");
        return date1.compareTo(date2);
    }

    /**
     * 日期大小比较
     *
     * @param date1 日期对象
     * @param date2 日期对象
     * @return 1:date1&gt;date2、0:date1=date2 -1:date1&lt;date2
     */
    public static int compareTo(LocalDateTime date1, LocalDateTime date2) {
        DateAssert.illegalArgument(date1, "非法参数");
        DateAssert.illegalArgument(date2, "非法参数");
        return date1.compareTo(date2);
    }

    /**
     * 比较两个日期的大小
     * 如：2023-05-14 12:56:28比2023-05-14 12:56:29小
     *
     * @param date1 日期对象
     * @param date2 日期对象
     * @return true-date1小于date2,false-date1小于date2
     */
    public static boolean isBefore(LocalDateTime date1, LocalDateTime date2) {
        DateAssert.illegalArgument(date1, "非法参数");
        DateAssert.illegalArgument(date2, "非法参数");
        return date1.isBefore(date2);
    }

    /**
     * 比较两个日期的大小
     * 如：2023-05-14 12:56:29比2023-05-14 12:56:28大
     *
     * @param date1 日期对象
     * @param date2 日期对象
     * @return true-date1大于date2, false-date1小于date2
     */
    public static boolean isAfter(LocalDateTime date1, LocalDateTime date2) {
        DateAssert.illegalArgument(date1, "非法参数");
        DateAssert.illegalArgument(date2, "非法参数");
        return date1.isAfter(date2);
    }

    /**
     * 比较两个日期是否相等
     * 如：2023-05-14 12:56:29 和 2023-05-14 12:56:29 两个日期相等
     *
     * @param date1 日期对象
     * @param date2 日期对象
     * @return true-date1等于date2,false-date1不等于date2
     */
    public static boolean isEqual(LocalDateTime date1, LocalDateTime date2) {
        DateAssert.illegalArgument(date1, "非法参数");
        DateAssert.illegalArgument(date2, "非法参数");
        return date1.isEqual(date2);
    }

    /**
     * 日期大小比较
     *
     * @param date1 日期对象
     * @param date2 日期对象
     * @return 1:date1&gt;date2、0:date1=date2 -1:date1&lt;date2
     */
    public static int compareTo(LocalDate date1, LocalDate date2) {
        DateAssert.illegalArgument(date1, "非法参数");
        DateAssert.illegalArgument(date2, "非法参数");
        return date1.compareTo(date2);
    }

    /**
     * 日期大小比较
     *
     * @param date1 日期对象
     * @param date2 日期对象
     * @return 1:date1&gt;date2、0:date1=date2 -1:date1&lt;date2
     */
    public static int compareTo(LocalTime date1, LocalTime date2) {
        DateAssert.illegalArgument(date1, "非法参数");
        DateAssert.illegalArgument(date2, "非法参数");
        return date1.compareTo(date2);
    }

    /**
     * Duration对象类型比较大小
     *
     * @param duration1 日期对象
     * @param duration2 日期对象
     * @return 1:duration1&gt;duration2，0:duration1=duration2，-1:duration1&lt;duration2
     */
    public static int compareTo(Duration duration1, Duration duration2) {
        DateAssert.illegalArgument(duration1, "非法参数");
        DateAssert.illegalArgument(duration2, "非法参数");
        return duration1.compareTo(duration2);
    }

    /**
     * Instant对象类型比较大小
     *
     * @param instant1 日期对象
     * @param instant2 日期对象
     * @return 1：instant1&gt;instant2，0：instant1=instant2，-1：instant1&lt;instant2
     */
    public static int compareTo(Instant instant1, Instant instant2) {
        DateAssert.illegalArgument(instant1, "非法参数");
        DateAssert.illegalArgument(instant2, "非法参数");
        return instant1.compareTo(instant2);
    }
}
