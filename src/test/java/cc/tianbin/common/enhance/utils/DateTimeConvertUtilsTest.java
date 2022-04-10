package cc.tianbin.common.enhance.utils;

import cc.tianbin.common.utils.DataUtils;
import cc.tianbin.common.utils.compare.CompareUtils;
import cc.tianbin.common.utils.date.DateTimeConvertUtils;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static cc.tianbin.common.utils.date.DateTimeConvertUtils.*;

/**
 * Created by nibnait on 2021/09/01
 */
public class DateTimeConvertUtilsTest {

    @Test
    public void toMilliTimeStamp() {
        long timeStamp = 123;
        try {
            DateTimeConvertUtils.toMilliTimeStamp(timeStamp);
        } catch (Exception e) {
            Assert.assertEquals(DataUtils.format("{} 非10位/13位时间戳", timeStamp), e.getMessage());
        }

        timeStamp = 1231231231;
        Long result = DateTimeConvertUtils.toMilliTimeStamp(timeStamp);
        long expect = 1231231231000L;
        Assert.assertTrue(CompareUtils.matchObject(result, expect));

        timeStamp = 0;
        result = DateTimeConvertUtils.toMilliTimeStamp(timeStamp);
        expect = 0;
        Assert.assertTrue(CompareUtils.matchObject(result, expect));
    }

    @Test
    public void toSecondTimeStamp() {
        long timeStamp = 123;
        try {
            DateTimeConvertUtils.toSecondTimeStamp(timeStamp);
        } catch (Exception e) {
            Assert.assertEquals("非10位/13位时间戳", e.getMessage());
        }

        timeStamp = 1231231231000L;
        Long result = DateTimeConvertUtils.toSecondTimeStamp(timeStamp);
        long expect = 1231231231;
        Assert.assertTrue(CompareUtils.matchObject(result, expect));

        timeStamp = 1231231231L;
        result = DateTimeConvertUtils.toSecondTimeStamp(timeStamp);
        expect = 1231231231;
        Assert.assertTrue(CompareUtils.matchObject(result, expect));

        timeStamp = 0;
        result = DateTimeConvertUtils.toSecondTimeStamp(timeStamp);
        expect = 0;
        Assert.assertTrue(CompareUtils.matchObject(result, expect));
    }

    /************************** String <==> timestamp ******************************/

    @Test
    public void timeStamp2String() {
        long timeStamp = 1635820523499L;
        String expect = "2021-11-02 10:35:23";

        String result = DateTimeConvertUtils.timeStamp2String(timeStamp, DATE_TIME_FORMAT);
        Assert.assertTrue(CompareUtils.matchObject(result, expect));

        result = DateTimeConvertUtils.timeStamp2DateTimeString(timeStamp);
        Assert.assertTrue(CompareUtils.matchObject(result, expect));

        expect = "2021-11-02";
        result = DateTimeConvertUtils.timeStamp2DateString(timeStamp);
        Assert.assertTrue(CompareUtils.matchObject(result, expect));

    }

    @Test
    public void string2TimeStamp() {
        String str = "2021-11-02 10:35:23";
        long result = DateTimeConvertUtils.string2MilliSecond(str);
        long expect = 1635820523000L;
        Assert.assertEquals(expect, result);

        result = DateTimeConvertUtils.string2MilliSecond(str, DATE_TIME_FORMAT);
        expect = 1635820523000L;
        Assert.assertEquals(expect, result);

        str = "2021-11-02 10:35:23 123";
        result = DateTimeConvertUtils.string2MilliSecond(str);
        expect = 1635820523000L;
        Assert.assertEquals(expect, result);

        result = DateTimeConvertUtils.string2MilliSecond(str, DATE_TIME_FORMAT);
        expect = 1635820523000L;
        Assert.assertEquals(expect, result);

        str = "2021-11-02 10:35:23:123";
        result = DateTimeConvertUtils.string2MilliSecond(str);
        expect = 1635820523000L;
        Assert.assertEquals(expect, result);

        result = DateTimeConvertUtils.string2MilliSecond(str, DATE_TIME_MILLI_FORMAT);
        expect = 1635820523123L;
        Assert.assertEquals(expect, result);

        // 异常覆盖
        str = "2021-11-02 10:35";
        try {
            DateTimeConvertUtils.string2MilliSecond(str);
        } catch (Exception e) {
            Assert.assertEquals(DataUtils.format("timeStr:{}, format:{} DateTimeConvertUtils.string2MilliSecond 转时间戳失败", str, DATE_TIME_FORMAT),
                    e.getMessage());
        }

        str = "2021-11-02 10:35:23 123";
        try {
            DateTimeConvertUtils.string2MilliSecond(str, DATE_TIME_MILLI_FORMAT);
        } catch (Exception e) {
            Assert.assertEquals(DataUtils.format("timeStr:{}, format:{} DateTimeConvertUtils.string2MilliSecond 转时间戳失败", str, DATE_TIME_MILLI_FORMAT),
                    e.getMessage());
        }

    }

    /************************** LocalDateTime <==> timestamp ******************************/
    @Test
    public void localDateTime2TimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        Long result = DateTimeConvertUtils.localDateTime2Second(now);
        long expect = now.toEpochSecond(ZoneOffset.of("+8"));
        Assert.assertTrue(CompareUtils.matchObject(result, expect));

        now = LocalDateTime.now();
        result = DateTimeConvertUtils.localDateTime2MillSecond(now);
        expect = now.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Assert.assertTrue(CompareUtils.matchObject(result, expect));
    }

    @Test
    public void timeStamp2LocalDate() {
        long timsStamp = 1635850615401L;
        LocalDate result = DateTimeConvertUtils.timeStamp2LocalDate(timsStamp);
        LocalDate expect = LocalDate.parse("2021-11-02 18:56:55", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        Assert.assertTrue(CompareUtils.matchObject(result, expect));
    }

    @Test
    public void timeStamp2LocalDateTime() {
        long timsStamp = 1635850615401L;
        LocalDateTime result = DateTimeConvertUtils.timeStamp2LocalDateTime(timsStamp);
        LocalDateTime expect = LocalDateTime.parse("2021-11-02 18:56:55:401", DateTimeFormatter.ofPattern(DATE_TIME_MILLI_FORMAT));
        Assert.assertTrue(CompareUtils.matchObject(result, expect));
    }

    /************************** String <==> Date ********************************************/
    @Test
    public void string2Date() {
        String dateString = "2021-11-02 18:56:55";
        Date result = DateTimeConvertUtils.string2Date(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2021);
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date expect = calendar.getTime();
        Assert.assertTrue(CompareUtils.matchObject(result, expect));

        dateString = "2021-11-02 18:56:55";
        result = DateTimeConvertUtils.string2Date(dateString, DATE_TIME_FORMAT);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2021);
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 56);
        calendar.set(Calendar.SECOND, 55);
        calendar.set(Calendar.MILLISECOND, 0);
        expect = calendar.getTime();
        Assert.assertTrue(CompareUtils.matchObject(result, expect));

        // 异常情况
        dateString = "2021/11/02 18:56:55";
        try {
            DateTimeConvertUtils.string2Date(dateString);
        } catch (Exception e) {
            Assert.assertEquals(DataUtils.format("timeStr:{}, format:{} DateTimeConvertUtils.string2Date error", dateString, DATE_FORMAT),
                    e.getMessage());
        }

        dateString = "2021/11/02 18:56:55";
        try {
            DateTimeConvertUtils.string2Date(dateString, DATE_TIME_FORMAT);
        } catch (Exception e) {
            Assert.assertEquals(DataUtils.format("timeStr:{}, format:{} DateTimeConvertUtils.string2Date error", dateString, DATE_TIME_FORMAT),
                    e.getMessage());
        }

    }

    @Test
    public void date2String() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2021);
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        String result = DateTimeConvertUtils.date2String(date);
        String expect = "2021-11-02";
        Assert.assertEquals(result, expect);

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2021);
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 56);
        calendar.set(Calendar.SECOND, 55);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        result = DateTimeConvertUtils.date2String(date, DATE_TIME_FORMAT);
        expect = "2021-11-02 18:56:55";
        Assert.assertEquals(result, expect);
    }

    /************************** String <==> LocalDate ********************************************/
    @Test
    public void string2LocalDate() {
        String dateString = "2021-11-02";
        LocalDate result = DateTimeConvertUtils.string2LocalDate(dateString);
        LocalDate expect = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DATE_FORMAT));
        Assert.assertTrue(CompareUtils.matchObject(result, expect));

        dateString = "2021-11-02 18:56:55";
        result = DateTimeConvertUtils.string2LocalDate(dateString, DATE_TIME_FORMAT);
        expect = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        Assert.assertTrue(CompareUtils.matchObject(result, expect));
    }


    @Test
    public void localDate2String() {
        String expect = "2021-11-02";
        LocalDate localDate = LocalDate.parse(expect, DateTimeFormatter.ofPattern(DATE_FORMAT));

        String result = DateTimeConvertUtils.localDate2String(localDate);
        Assert.assertTrue(CompareUtils.matchObject(result, expect));
    }

    /************************** String <==> LocalDateTime ********************************************/
    @Test
    public void string2LocalDateTime() {
        String dateString = "2021-11-02 18:56:55";
        LocalDateTime result = DateTimeConvertUtils.string2LocalDateTime(dateString);
        LocalDateTime expect = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        Assert.assertTrue(CompareUtils.matchObject(result, expect));

        dateString = "2021-11-02";
        result = DateTimeConvertUtils.dateString2LocalDateTime(dateString);
        LocalDateTime expectDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DATE_FORMAT)).atStartOfDay();
        Assert.assertTrue(CompareUtils.matchObject(result, expectDate));

        dateString = "2021-11-02 18:56:55";
        result = DateTimeConvertUtils.dateTimeString2LocalDateTime(dateString);
        expect = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        Assert.assertTrue(CompareUtils.matchObject(result, expect));
    }

    @Test
    public void localDateTime2String() {
        String expect = "2021-11-02 18:56:55";
        LocalDateTime localDateTime = LocalDateTime.parse(expect, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

        String result = DateTimeConvertUtils.localDateTime2String(localDateTime);
        Assert.assertTrue(CompareUtils.matchObject(result, expect));
    }

    /************************** Date ==> LocalDate ********************************************/
    @Test
    public void dateToLocalDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2021);
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        LocalDate result = DateTimeConvertUtils.dateToLocalDate(date);

        LocalDate expect = DateTimeConvertUtils.string2LocalDate("2021-11-02");
        Assert.assertEquals(result, expect);
    }

    /************************** Date ==> LocalDateTime ****************************************/
    @Test
    public void dateToLocalDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2021);
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 56);
        calendar.set(Calendar.SECOND, 55);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        LocalDateTime result = DateTimeConvertUtils.dateToLocalDateTime(date);

        LocalDateTime expect = DateTimeConvertUtils.string2LocalDateTime("2021-11-02 18:56:55");
        Assert.assertEquals(result, expect);
    }

    /******************* timestamp, java.sql.Date, LocalDate, LocalDateTime ==> Date ********************/

    @Test
    public void toUtilDate() {
        Date date = null;
        Date result = DateTimeConvertUtils.toUtilDate(date);
        Date expect = null;
        Assert.assertEquals(expect, result);

        Integer timeStampSecond = 1634683724;
        result = DateTimeConvertUtils.toUtilDate(timeStampSecond);
        expect = DateTimeConvertUtils.string2Date("2021-10-20 06:48:44", DATE_TIME_FORMAT);
        Assert.assertEquals(expect, result);

        Long timeStampMillSecond = 1634683724000L;
        result = DateTimeConvertUtils.toUtilDate(timeStampMillSecond);
        expect = DateTimeConvertUtils.string2Date("2021-10-20 06:48:44", DATE_TIME_FORMAT);
        Assert.assertEquals(expect, result);


        expect = DateTimeConvertUtils.string2Date("2021-11-02 18:56:55", DATE_TIME_FORMAT);

        Timestamp timestamp = Timestamp.valueOf("2021-11-02 18:56:55");
        result = DateTimeConvertUtils.toUtilDate(timestamp);
        Assert.assertEquals(expect, result);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2021);
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 56);
        calendar.set(Calendar.SECOND, 55);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        result = DateTimeConvertUtils.toUtilDate(date);
        Assert.assertEquals(expect, result);

        LocalDate localDate = DateTimeConvertUtils.string2LocalDate("2021-11-02");
        result = DateTimeConvertUtils.toUtilDate(localDate);
        Date localDateExpect = DateTimeConvertUtils.string2Date("2021-11-02", DATE_FORMAT);
        Assert.assertEquals(localDateExpect, result);

        LocalDateTime localDateTime = DateTimeConvertUtils.string2LocalDateTime("2021-11-02 18:56:55");
        result = DateTimeConvertUtils.toUtilDate(localDateTime);
        Assert.assertEquals(expect, result);

        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        result = DateTimeConvertUtils.toUtilDate(zonedDateTime);
        Assert.assertEquals(expect, result);

        Instant instant = Instant.ofEpochSecond(1635850615);
        result = DateTimeConvertUtils.toUtilDate(instant);
        Assert.assertEquals(expect, result);

        try {
            DateTimeConvertUtils.toUtilDate((byte) 1);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Don't know hot to convert"));
        }
    }
}
