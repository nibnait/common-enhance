package com.java.common.utils.date;

import java.time.LocalDateTime;
import java.util.Date;

public class DateUtils {

    public static Date now() {
        return new Date();
    }

    public static int currentTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static Long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static Long nanoTime() {
        return System.nanoTime();
    }

    public static LocalDateTime now8() {
        return LocalDateTime.now();
    }

}