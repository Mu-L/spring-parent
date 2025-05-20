package com.emily.infrastructure.date.test;

import com.emily.infrastructure.date.DateConvertUtils;
import com.emily.infrastructure.date.DatePatternInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 单元测试类
 *
 * @author Emily
 * @since Created in 2023/5/14 3:24 PM
 */
public class DateConvertUtilsTest {
    @Test
    public void format() {
        Assertions.assertEquals(DateConvertUtils.format("20230514", DatePatternInfo.YYYYMMDD, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-05-14 00:00:00");

        Date date = DateConvertUtils.toDate("2023-05-14 12:52:56", DatePatternInfo.YYYY_MM_DD_HH_MM_SS);
        Assertions.assertEquals(DateConvertUtils.format(date, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-05-14 12:52:56");

        LocalDateTime localDateTime = LocalDateTime.of(2023, 06, 01, 8, 52, 53);
        Assertions.assertEquals(DateConvertUtils.format(localDateTime, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-06-01 08:52:53");
        Assertions.assertEquals(DateConvertUtils.format(localDateTime, DatePatternInfo.YYYY_MM_DD_HH_MM_SS, ZoneId.of("America/New_York")), "2023-05-31 20:52:53");

        LocalDate localDate = LocalDate.of(2023, 06, 01);
        Assertions.assertEquals(DateConvertUtils.format(localDate, DatePatternInfo.YYYY_MM_DD), "2023-06-01");
        Assertions.assertEquals(DateConvertUtils.format(localDate, DatePatternInfo.YYYY_MM_DD, ZoneId.of("America/New_York")), "2023-05-31");

        LocalTime localTime = LocalTime.of(8, 52, 53);
        Assertions.assertEquals(DateConvertUtils.format(localTime, DatePatternInfo.HH_MM_SS), "08:52:53");
    }

    @Test
    public void toLocalDateTime() {
        Assertions.assertNotNull(DateConvertUtils.toDate("2023-05-14 12:52:56", DatePatternInfo.YYYY_MM_DD_HH_MM_SS));
        Assertions.assertNotNull(DateConvertUtils.toDate(LocalDate.now()));
        Assertions.assertNotNull(DateConvertUtils.toDate(LocalDateTime.now()));

        LocalDateTime localDateTime6 = DateConvertUtils.toLocalDateTime("2023-05-14 12:52:56", DatePatternInfo.YYYY_MM_DD_HH_MM_SS, ZoneId.of("America/New_York"));
        Assertions.assertEquals(DateConvertUtils.format(localDateTime6, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-05-14 00:52:56");
        LocalDateTime localDateTime7 = DateConvertUtils.toLocalDateTime("2023-05-14 12:52:56", DatePatternInfo.YYYY_MM_DD_HH_MM_SS);
        Assertions.assertNotNull(DateConvertUtils.format(localDateTime7, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-05-14 12:52:56");

        LocalDateTime localDateTime8 = DateConvertUtils.toLocalDateTime("2023-05-14 12:52:56", DatePatternInfo.YYYY_MM_DD_HH_MM_SS);
        Date date = Date.from(localDateTime8.atZone(ZoneId.systemDefault()).toInstant());
        Assertions.assertEquals(DateConvertUtils.format(date, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-05-14 12:52:56");
        LocalDateTime localDateTime9 = DateConvertUtils.toLocalDateTime(date, ZoneId.of("America/New_York"));
        Assertions.assertEquals(DateConvertUtils.format(localDateTime9, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-05-14 00:52:56");
        LocalDateTime localDateTime10 = DateConvertUtils.toLocalDateTime(date);
        Assertions.assertEquals(DateConvertUtils.format(localDateTime10, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-05-14 12:52:56");

        LocalDate localDate = LocalDate.of(2023, 05, 06);
        Assertions.assertEquals(DateConvertUtils.format(localDate, DatePatternInfo.YYYY_MM_DD), "2023-05-06");
        LocalDateTime localDateTime11 = DateConvertUtils.toLocalDateTime(localDate, ZoneId.of("America/New_York"));
        Assertions.assertEquals(DateConvertUtils.format(localDateTime11, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-05-05 12:00:00");
        LocalDateTime localDateTime12 = DateConvertUtils.toLocalDateTime(localDate);
        Assertions.assertEquals(DateConvertUtils.format(localDateTime12, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-05-06 00:00:00");

        LocalDateTime localDateTime = DateConvertUtils.toLocalDateTime(LocalDate.of(2023, 3, 14), LocalTime.of(12, 12, 12), ZoneId.of("America/New_York"));
        Assertions.assertEquals(localDateTime.format(DateTimeFormatter.ofPattern(DatePatternInfo.YYYY_MM_DD_HH_MM_SS)), "2023-03-14 00:12:12");
        LocalDateTime localDateTime1 = DateConvertUtils.toLocalDateTime(LocalDate.of(2023, 3, 14), LocalTime.of(12, 12, 12));
        Assertions.assertEquals(localDateTime1.format(DateTimeFormatter.ofPattern(DatePatternInfo.YYYY_MM_DD_HH_MM_SS)), "2023-03-14 12:12:12");
    }

    @Test
    public void toLocalDate() {
        Date date1 = Date.from(LocalDate.of(2023, 05, 06).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Assertions.assertEquals(DateConvertUtils.format(date1, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-05-06 00:00:00");
        LocalDate localDate1 = DateConvertUtils.toLocalDate(date1, ZoneId.of("America/New_York"));
        Assertions.assertEquals(DateConvertUtils.format(localDate1, DatePatternInfo.YYYY_MM_DD), "2023-05-05");
        LocalDate localDate2 = DateConvertUtils.toLocalDate(date1);
        Assertions.assertEquals(DateConvertUtils.format(localDate2, DatePatternInfo.YYYY_MM_DD), "2023-05-06");

        LocalDateTime localDateTime13 = LocalDateTime.of(2023, 05, 06, 11, 52, 53);
        Assertions.assertEquals(DateConvertUtils.format(localDateTime13, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-05-06 11:52:53");
        LocalDate localDate3 = DateConvertUtils.toLocalDate(localDateTime13, ZoneId.of("America/New_York"));
        Assertions.assertEquals(DateConvertUtils.format(localDate3, DatePatternInfo.YYYY_MM_DD), "2023-05-05");
        LocalDate localDate4 = DateConvertUtils.toLocalDate(localDateTime13);
        Assertions.assertEquals(DateConvertUtils.format(localDate4, DatePatternInfo.YYYY_MM_DD), "2023-05-06");

        LocalDate localDate5 = DateConvertUtils.toLocalDate("2023-05-14", DatePatternInfo.YYYY_MM_DD);
        Assertions.assertEquals(DateConvertUtils.format(localDate5, DatePatternInfo.YYYY_MM_DD), "2023-05-14");
        LocalDate localDate6 = DateConvertUtils.toLocalDate("2023-05-14", DatePatternInfo.YYYY_MM_DD, ZoneId.of("America/New_York"));
        Assertions.assertEquals(DateConvertUtils.format(localDate6, DatePatternInfo.YYYY_MM_DD), "2023-05-13");
    }

    @Test
    public void toLocalTime() {
        LocalTime localTime1 = DateConvertUtils.toLocalTime("12:52:56", DatePatternInfo.HH_MM_SS);
        Assertions.assertEquals(DateConvertUtils.format(localTime1, DatePatternInfo.HHMMSS), "125256");

        LocalDateTime localDateTime1 = LocalDateTime.of(2023, 05, 06, 13, 14, 25);
        LocalTime localTime3 = DateConvertUtils.toLocalTime(localDateTime1);
        Assertions.assertEquals(DateConvertUtils.format(localTime3, DatePatternInfo.HHMMSS), "131425");

        LocalDateTime localDateTime2 = LocalDateTime.of(2023, 05, 06, 13, 14, 25);
        Date date = Date.from(localDateTime2.atZone(ZoneId.systemDefault()).toInstant());
        LocalTime localTime5 = DateConvertUtils.toLocalTime(date);
        Assertions.assertEquals(DateConvertUtils.format(localTime5, DatePatternInfo.HHMMSS), "131425");

    }

    @Test
    public void combine() {
        LocalDateTime localDateTime = DateConvertUtils.combine("20230506", DatePatternInfo.YYYYMMDD, "05:23:21", DatePatternInfo.HH_MM_SS);
        String s = LocalDateTime.of(localDateTime.toLocalDate(), localDateTime.toLocalTime()).format(DateTimeFormatter.ofPattern(DatePatternInfo.YYYY_MM_DD_HH_MM_SS));
        Assertions.assertEquals(s, "2023-05-06 05:23:21");
        Assertions.assertNotNull(DateConvertUtils.combine(LocalDate.now(), LocalTime.now()));
    }

    @Test
    public void dateToInt() {
        LocalDateTime localDateTime = LocalDateTime.of(2023, 06, 01, 8, 52, 53);
        Assertions.assertEquals(DateConvertUtils.dateToInt(localDateTime, DatePatternInfo.YYYYMMDDHHMMSS), 20230601085253L);
        Assertions.assertEquals(DateConvertUtils.dateToInt(localDateTime.toLocalDate(), null), 20230601);
        Assertions.assertEquals(DateConvertUtils.dateToInt(localDateTime.toLocalDate(), DatePatternInfo.YYYYMMDD), 20230601);
        Assertions.assertEquals(DateConvertUtils.dateToInt(localDateTime.toLocalTime(), null), 85253);
        Assertions.assertEquals(DateConvertUtils.dateToInt(localDateTime.toLocalTime(), DatePatternInfo.HHMMSS), 85253);
        Assertions.assertEquals(DateConvertUtils.dateToInt(localDateTime.toLocalTime(), DatePatternInfo.HHMM), 852);

        Date date = DateConvertUtils.toDate("20230201", DatePatternInfo.YYYYMMDD);
        Assertions.assertEquals(DateConvertUtils.dateToInt(date, DatePatternInfo.YYYYMMDD), 20230201);
    }

    @Test
    public void timestamp() {
        Date date = DateConvertUtils.toDate(1685353612112L);
        Assertions.assertEquals(DateConvertUtils.format(date, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-05-29 17:46:52");

        Date date1 = DateConvertUtils.toDate(0);
        Assertions.assertEquals(DateConvertUtils.format(date1, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "1970-01-01 08:00:00");
        System.out.println(date1.getTime());

        LocalDateTime localDateTime = DateConvertUtils.toLocalDateTime(1685353612112L, ZoneId.systemDefault());
        Assertions.assertEquals(DateConvertUtils.format(localDateTime, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-05-29 17:46:52");

        LocalDateTime localDateTime1 = DateConvertUtils.toLocalDateTime(0);
        Assertions.assertEquals(DateConvertUtils.format(localDateTime1, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "1970-01-01 08:00:00");

        LocalDateTime localDateTime2 = DateConvertUtils.toLocalDateTime(1685353612112L, ZoneId.of("America/New_York"));
        Assertions.assertEquals(DateConvertUtils.format(localDateTime2, DatePatternInfo.YYYY_MM_DD_HH_MM_SS), "2023-05-29 05:46:52");
    }

    @Test
    public void zoneId() {
        LocalDateTime localDateTime = LocalDateTime.of(2023, 05, 12, 13, 12, 12);
        //System.out.println(DateConvertUtils.format(localDateTime, DatePatternInfo.YYYY_MM_DD_HH_MM_SS));
        String s = DateConvertUtils.toLocalDateTime(localDateTime, ZoneId.of("America/New_York")).format(DateTimeFormatter.ofPattern(DatePatternInfo.YYYY_MM_DD_HH_MM_SS));
        Assertions.assertEquals(s, "2023-05-12 01:12:12");

    }

    @Test
    public void toInstant() {
        Instant instant = DateConvertUtils.toInstant("2023-01-01T12:00:00Z");
        Assertions.assertEquals(instant.toEpochMilli(), 1672574400000L);

        LocalDateTime localDateTime = LocalDateTime.of(2023, 05, 12, 13, 12, 12);
        Instant instant1 = DateConvertUtils.toInstant(localDateTime, ZoneId.of("America/New_York"));
        Assertions.assertEquals(instant1.toEpochMilli(), 1683911532000L);
    }
}
