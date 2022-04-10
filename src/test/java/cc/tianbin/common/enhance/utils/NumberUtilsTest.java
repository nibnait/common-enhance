package cc.tianbin.common.enhance.utils;

import cc.tianbin.common.utils.NumberUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by nibnait on 2021/11/09
 */
public class NumberUtilsTest {

    /************************** Byte ******************************/
    @Test
    public void notNullAndGreaterThanOrEqualsZero_Byte() {
        Byte b = null;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));

        b = 1;
        Assert.assertTrue(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));

        b = 0;
        Assert.assertTrue(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));

        b = -1;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));
    }

    @Test
    public void notNullAndGreaterThanZero_Byte() {
        Byte b = null;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanZero(b));

        b = 1;
        Assert.assertTrue(NumberUtils.notNullAndGreaterThanZero(b));

        b = 0;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanZero(b));

        b = -1;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanZero(b));
    }

    @Test
    public void nullOrLessThanOrEqualsZero_Byte() {
        Byte b = null;
        Assert.assertTrue(NumberUtils.nullOrLessThanOrEqualsZero(b));

        b = 1;
        Assert.assertFalse(NumberUtils.nullOrLessThanOrEqualsZero(b));

        b = 0;
        Assert.assertTrue(NumberUtils.nullOrLessThanOrEqualsZero(b));

        b = -1;
        Assert.assertTrue(NumberUtils.nullOrLessThanOrEqualsZero(b));
    }

    @Test
    public void nullOrLessThanZero_Byte() {
        Byte b = null;
        Assert.assertTrue(NumberUtils.nullOrLessThanZero(b));

        b = 1;
        Assert.assertFalse(NumberUtils.nullOrLessThanZero(b));

        b = 0;
        Assert.assertFalse(NumberUtils.nullOrLessThanZero(b));

        b = -1;
        Assert.assertTrue(NumberUtils.nullOrLessThanZero(b));
    }

    /************************** Integer ******************************/
    @Test
    public void notNullAndGreaterThanOrEqualsZero_Integer() {
        Integer b = null;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));

        b = 1;
        Assert.assertTrue(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));

        b = 0;
        Assert.assertTrue(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));

        b = -1;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));
    }

    @Test
    public void notNullAndGreaterThanZero_Integer() {
        Integer b = null;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanZero(b));

        b = 1;
        Assert.assertTrue(NumberUtils.notNullAndGreaterThanZero(b));

        b = 0;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanZero(b));

        b = -1;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanZero(b));
    }

    @Test
    public void nullOrLessThanOrEqualsZero_Integer() {
        Integer b = null;
        Assert.assertTrue(NumberUtils.nullOrLessThanOrEqualsZero(b));

        b = 1;
        Assert.assertFalse(NumberUtils.nullOrLessThanOrEqualsZero(b));

        b = 0;
        Assert.assertTrue(NumberUtils.nullOrLessThanOrEqualsZero(b));

        b = -1;
        Assert.assertTrue(NumberUtils.nullOrLessThanOrEqualsZero(b));
    }

    @Test
    public void nullOrLessThanZero_Integer() {
        Integer b = null;
        Assert.assertTrue(NumberUtils.nullOrLessThanZero(b));

        b = 1;
        Assert.assertFalse(NumberUtils.nullOrLessThanZero(b));

        b = 0;
        Assert.assertFalse(NumberUtils.nullOrLessThanZero(b));

        b = -1;
        Assert.assertTrue(NumberUtils.nullOrLessThanZero(b));
    }

    /************************** Long ******************************/
    @Test
    public void notNullAndGreaterThanOrEqualsZero_Long() {
        Long b = null;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));

        b = 1L;
        Assert.assertTrue(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));

        b = 0L;
        Assert.assertTrue(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));

        b = -1L;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));
    }

    @Test
    public void notNullAndGreaterThanZero_Long() {
        Long b = null;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanZero(b));

        b = 1L;
        Assert.assertTrue(NumberUtils.notNullAndGreaterThanZero(b));

        b = 0L;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanZero(b));

        b = -1L;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanZero(b));
    }

    @Test
    public void nullOrLessThanOrEqualsZero_Long() {
        Long b = null;
        Assert.assertTrue(NumberUtils.nullOrLessThanOrEqualsZero(b));

        b = 1L;
        Assert.assertFalse(NumberUtils.nullOrLessThanOrEqualsZero(b));

        b = 0L;
        Assert.assertTrue(NumberUtils.nullOrLessThanOrEqualsZero(b));

        b = -1L;
        Assert.assertTrue(NumberUtils.nullOrLessThanOrEqualsZero(b));
    }

    @Test
    public void nullOrLessThanZero_Long() {
        Long b = null;
        Assert.assertTrue(NumberUtils.nullOrLessThanZero(b));

        b = 1L;
        Assert.assertFalse(NumberUtils.nullOrLessThanZero(b));

        b = 0L;
        Assert.assertFalse(NumberUtils.nullOrLessThanZero(b));

        b = -1L;
        Assert.assertTrue(NumberUtils.nullOrLessThanZero(b));
    }

    /************************** BigDecimal ******************************/
    @Test
    public void notNullAndGreaterThanOrEqualsZero_BigDecimal() {
        BigDecimal b = null;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));

        b = BigDecimal.valueOf(1);
        Assert.assertTrue(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));

        b = BigDecimal.valueOf(0);
        Assert.assertTrue(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));

        b = BigDecimal.valueOf(-1);
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanOrEqualsZero(b));
    }

    @Test
    public void notNullAndGreaterThanZero_BigDecimal() {
        BigDecimal b = null;
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanZero(b));

        b = BigDecimal.valueOf(1);
        Assert.assertTrue(NumberUtils.notNullAndGreaterThanZero(b));

        b = BigDecimal.valueOf(0);
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanZero(b));

        b = BigDecimal.valueOf(-1);
        Assert.assertFalse(NumberUtils.notNullAndGreaterThanZero(b));
    }

    @Test
    public void nullOrLessThanOrEqualsZero_BigDecimal() {
        BigDecimal b = null;
        Assert.assertTrue(NumberUtils.nullOrLessThanOrEqualsZero(b));

        b = BigDecimal.valueOf(1);
        Assert.assertFalse(NumberUtils.nullOrLessThanOrEqualsZero(b));

        b = BigDecimal.valueOf(0);
        Assert.assertTrue(NumberUtils.nullOrLessThanOrEqualsZero(b));

        b = BigDecimal.valueOf(-1);
        Assert.assertTrue(NumberUtils.nullOrLessThanOrEqualsZero(b));
    }

    @Test
    public void nullOrLessThanZero_BigDecimal() {
        BigDecimal b = null;
        Assert.assertTrue(NumberUtils.nullOrLessThanZero(b));

        b = BigDecimal.valueOf(1);
        Assert.assertFalse(NumberUtils.nullOrLessThanZero(b));

        b = BigDecimal.valueOf(0);
        Assert.assertFalse(NumberUtils.nullOrLessThanZero(b));

        b = BigDecimal.valueOf(-1);
        Assert.assertTrue(NumberUtils.nullOrLessThanZero(b));
    }

    @Test
    public void lessThanOrEquals() {
        Integer o1 = 1;
        Integer o2 = null;
        Assert.assertFalse(NumberUtils.lessThanOrEquals(o1, o2));

        o2 = 2;
        Assert.assertTrue(NumberUtils.lessThanOrEquals(o1, o2));
    }

    @Test
    public void lessThan() {
        Integer o1 = 1;
        Integer o2 = null;
        Assert.assertFalse(NumberUtils.lessThan(o1, o2));

        o2 = 2;
        Assert.assertTrue(NumberUtils.lessThan(o1, o2));
    }


    @Test
    public void greaterThanOrEquals() {
        Integer o1 = 1;
        Integer o2 = null;
        Assert.assertFalse(NumberUtils.greaterThanOrEquals(o1, o2));

        o2 = 2;
        Assert.assertFalse(NumberUtils.greaterThanOrEquals(o1, o2));
    }


    @Test
    public void greaterThan() {
        Integer o1 = 1;
        Integer o2 = null;
        Assert.assertFalse(NumberUtils.greaterThan(o1, o2));

        o2 = 2;
        Assert.assertFalse(NumberUtils.greaterThan(o1, o2));
    }

    @Test
    public void getPriceYuan_Integer() {
        Integer fen = 100;
        BigDecimal result = NumberUtils.getPriceYuan(fen);
        BigDecimal expected = new BigDecimal("1.00");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void getPriceYuan_Long() {
        Long fen = 100L;
        BigDecimal result = NumberUtils.getPriceYuan(fen);
        BigDecimal expected = new BigDecimal("1.00");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void getPriceYuan_BigDecimal() {
        BigDecimal fen = BigDecimal.valueOf(100);
        BigDecimal result = NumberUtils.getPriceYuan(fen);
        BigDecimal expected = new BigDecimal("1.00");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void divide_default() {
        BigDecimal dividend = null;
        BigDecimal divisor = new BigDecimal("3.14");
        BigDecimal result = NumberUtils.divide(dividend, divisor);
        BigDecimal expected = BigDecimal.ZERO;
        Assert.assertEquals(expected, result);

        dividend = new BigDecimal("2.13");
        divisor = new BigDecimal("2");
        result = NumberUtils.divide(dividend, divisor);
        expected = new BigDecimal("1.06");
        Assert.assertEquals(expected, result);

    }

    @Test
    public void divide() {
        BigDecimal dividend = null;
        BigDecimal divisor = new BigDecimal("3.14");
        BigDecimal result = NumberUtils.divide(dividend, divisor, 2, RoundingMode.HALF_UP);
        BigDecimal expected = BigDecimal.ZERO;
        Assert.assertEquals(expected, result);

        dividend = new BigDecimal("2.13");
        divisor = new BigDecimal("2");
        result = NumberUtils.divide(dividend, divisor, 2, RoundingMode.HALF_UP);
        expected = new BigDecimal("1.07");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void getPriceFen_default() {
        BigDecimal yuan = null;
        BigDecimal result = NumberUtils.getPriceFen(yuan);
        BigDecimal expected = null;
        Assert.assertEquals(expected, result);

        yuan = new BigDecimal("1.111");
        result = NumberUtils.getPriceFen(yuan);
        expected = new BigDecimal("111.100");
        Assert.assertEquals(expected, result);

        int resultInt = result.intValue();
        int expectedInt = new BigDecimal("111").intValue();
        Assert.assertEquals(expectedInt, resultInt);
    }

    @Test
    public void getPriceFen() {
        Double yuan = null;
        BigDecimal result = NumberUtils.getPriceFen(yuan);
        BigDecimal expected = null;
        Assert.assertEquals(expected, result);

        yuan = 1.111;
        result = NumberUtils.getPriceFen(yuan);
        expected = new BigDecimal("111.100");
        Assert.assertEquals(expected, result);
    }

}
