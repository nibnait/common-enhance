package io.github.nibnait.common.utils.date;

import io.github.nibnait.common.utils.compare.CompareUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * Created by nibnait on 2021/08/23
 */
public class DateTimeCalcUtilsTest {

    @Test
    public void calcTimeBetween() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusMinutes(1);
        DateTimeCalcUtils.TimeBetween timeBetween = DateTimeCalcUtils.calcTimeBetween(start, now);
        Assert.assertTrue(CompareUtils.match(timeBetween, getExpect1()));

        start = now.plusDays(1).minusMinutes(2);
        timeBetween = DateTimeCalcUtils.calcTimeBetween(start, now);
        Assert.assertTrue(CompareUtils.match(timeBetween, getExpect2()));
    }

    private DateTimeCalcUtils.TimeBetween getExpect2() {
        DateTimeCalcUtils.TimeBetween expect = new DateTimeCalcUtils.TimeBetween();
        expect.setYears(0L);
        expect.setMounths(0L);
        expect.setDays(0L);
        expect.setHours(23L);
        expect.setMinutes(1438L);
        expect.setSeconds(86280L);
        expect.setMillis(86280000L);
        return expect;
    }

    private DateTimeCalcUtils.TimeBetween getExpect1() {
        DateTimeCalcUtils.TimeBetween expect = new DateTimeCalcUtils.TimeBetween();
        expect.setYears(0L);
        expect.setMounths(0L);
        expect.setDays(0L);
        expect.setHours(0L);
        expect.setMinutes(-1L);
        expect.setSeconds(-60L);
        expect.setMillis(-60000L);
        return expect;
    }

}
