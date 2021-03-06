package io.github.nibnait.common.utils.date;

import io.github.nibnait.common.constants.CommonConstants;
import io.github.nibnait.common.exception.ClientViewException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
public class DateTimeConvertUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT1 = "yyyyMMdd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_MILLI_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();
    private static final ZoneOffset BEIJING_ZONE_OFFSET = ZoneOffset.of("+8");
    private static final int MILLI_TIMESTAMP_LENGTH = 13;
    private static final int SECOND_TIMESTAMP_LENGTH = 10;

    public static Long toMilliTimeStamp(long timeStamp) {
        if (timeStamp == 0) {
            return 0L;
        }
        int length = String.valueOf(timeStamp).length();
        if (MILLI_TIMESTAMP_LENGTH == length) {
            return timeStamp;
        }
        if (SECOND_TIMESTAMP_LENGTH == length) {
            return timeStamp * 1000;
        }
        throw new ClientViewException("{} 非10位/13位时间戳", timeStamp);
    }

    public static Long toSecondTimeStamp(Date date) {
        if (date == null) {
            return 0L;
        }

        return toSecondTimeStamp(date.getTime());
    }

    public static Long toSecondTimeStamp(long timeStamp) {
        if (timeStamp == 0) {
            return 0L;
        }
        int length = String.valueOf(timeStamp).length();
        if (MILLI_TIMESTAMP_LENGTH == length) {
            return timeStamp / 1000;
        }
        if (SECOND_TIMESTAMP_LENGTH == length) {
            return timeStamp;
        }
        throw new ClientViewException("非10位/13位时间戳");
    }

    /************************** String <==> timestamp ******************************/
    public static String timeStamp2String(long timeStamp) {
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(toMilliTimeStamp(timeStamp));
    }

    public static String timeStamp2String(long timeStamp, String format) {
        return new SimpleDateFormat(format).format(toMilliTimeStamp(timeStamp));
    }

    public static String timeStamp2DateString(long timeStamp) {
        return new SimpleDateFormat(DATE_FORMAT).format(toMilliTimeStamp(timeStamp));
    }

    public static String timeStamp2DateTimeString(long timeStamp) {
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(toMilliTimeStamp(timeStamp));
    }

    public static Long string2MilliSecond(String time) {
        return string2MilliSecond(time, DATE_TIME_FORMAT);
    }

    public static Long string2MilliSecond(String time, String format) {
        if (StringUtils.isBlank(time)) {
            return 0L;
        }

        try {
            return new SimpleDateFormat(format).parse(time).getTime();
        } catch (ParseException e) {
            throw new ClientViewException("timeStr:{}, format:{} DateTimeConvertUtils.string2MilliSecond 转时间戳失败", time, format);
        }
    }

    /************************** LocalDateTime <==> timestamp ******************************/
    public static Long localDateTime2Second(LocalDateTime localDateTime) {
        return localDateTime.toEpochSecond(BEIJING_ZONE_OFFSET);
    }

    public static Long localDateTime2MillSecond(LocalDateTime localDateTime) {
        return localDateTime.toInstant(BEIJING_ZONE_OFFSET).toEpochMilli();
    }

    public static LocalDate timeStamp2LocalDate(long timeStamp) {
        return Instant.ofEpochMilli(toMilliTimeStamp(timeStamp)).atZone(DEFAULT_ZONE_ID).toLocalDate();
    }

    public static LocalDateTime timeStamp2LocalDateTime(long timeStamp) {
        return Instant.ofEpochMilli(toMilliTimeStamp(timeStamp)).atZone(DEFAULT_ZONE_ID).toLocalDateTime();
    }

    /************************** String <==> Date ********************************************/
    public static Date string2Date(String dateString) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(dateString);
        } catch (Exception e) {
            throw new ClientViewException("timeStr:{}, format:{} DateTimeConvertUtils.string2Date error", dateString, DATE_FORMAT);
        }
    }

    public static Date string2Date(String dateString, String dateFormat) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            return simpleDateFormat.parse(dateString);
        } catch (Exception e) {
            throw new ClientViewException("timeStr:{}, format:{} DateTimeConvertUtils.string2Date error", dateString, dateFormat);
        }
    }

    public static String date2String(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static String date2String(Date date, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }

    /************************** String <==> LocalDate ********************************************/
    public static LocalDate string2LocalDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static LocalDate string2LocalDate(String dateString, String dateFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        return LocalDate.parse(dateString, dateTimeFormatter);
    }

    public static String localDate2String(LocalDate localDate) {
        if (localDate == null) {
            return CommonConstants.EMPTY_STRING;
        }
        return localDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static String localDate2String(LocalDate localDate, String format) {
        if (localDate == null) {
            return CommonConstants.EMPTY_STRING;
        }
        return localDate.format(DateTimeFormatter.ofPattern(format));
    }

    /************************** String <==> LocalDateTime ********************************************/
    public static LocalDateTime string2LocalDateTime(String str, String format) {
        if (StringUtils.isBlank(str) || StringUtils.isBlank(format)) {
            return null;
        }
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime dateTimeString2LocalDateTime(String dateString) {
        return string2LocalDateTime(dateString, DATE_TIME_FORMAT);
    }

    public static LocalDateTime dateString2LocalDateTime(String dateString) {
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DATE_FORMAT));
        return localDate.atStartOfDay();
    }

    public static String localDateTime2String(LocalDateTime localDateTime) {
        return localDateTime2String(localDateTime, DATE_TIME_FORMAT);
    }

    public static String localDateTime2String(LocalDateTime localDateTime, String format) {
        if (localDateTime == null) {
            return CommonConstants.EMPTY_STRING;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String localDateTime2DateString(LocalDateTime localDateTime) {
        return localDateTime2String(localDateTime, DATE_FORMAT);
    }

    /************************** Date ==> LocalDate ********************************************/
    public static LocalDate dateToLocalDate(Date date) {
        ZonedDateTime zonedDateTime = date.toInstant().atZone(DEFAULT_ZONE_ID);
        return zonedDateTime.toLocalDate();
    }

    /************************** Date ==> LocalDateTime ****************************************/
    public static LocalDateTime dateToLocalDateTime(Date date) {
        ZonedDateTime zonedDateTime = date.toInstant().atZone(DEFAULT_ZONE_ID);
        return zonedDateTime.toLocalDateTime();
    }

    /******************* timestamp, java.sql.Date, LocalDate, LocalDateTime ==> Date ********************/
    public static Date toUtilDate(Object date) {
        if (date == null) {
            return null;
        }
        if (date instanceof Integer) {
            return new Date(toMilliTimeStamp((Integer) date));
        }
        if (date instanceof Long) {
            return new Date(toMilliTimeStamp((Long) date));
        }
        if (date instanceof java.sql.Date || date instanceof java.sql.Timestamp) {
            return new Date(((Date) date).getTime());
        }
        if (date instanceof Date) {
            return (Date) date;
        }
        if (date instanceof LocalDate) {
            return Date.from(((LocalDate) date).atStartOfDay(DEFAULT_ZONE_ID).toInstant());
        }
        if (date instanceof LocalDateTime) {
            return Date.from(((LocalDateTime) date).atZone(DEFAULT_ZONE_ID).toInstant());
        }
        if (date instanceof ZonedDateTime) {
            return Date.from(((ZonedDateTime) date).toInstant());
        }
        if (date instanceof Instant) {
            return Date.from((Instant) date);
        }

        throw new UnsupportedOperationException("Don't know hot to convert " + date.getClass().getName() + " to java.util.Date");
    }

}
