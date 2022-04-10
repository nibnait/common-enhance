package com.java.common.utils;

import com.java.common.mock.Person;
import com.java.common.utils.compare.CompareUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by nibnait on 2021/10/17
 */
public class CompareUtilsTest {

    @Test
    public void match() {
        Person actual = getActual();
        Person expect = getActual();

        boolean result = CompareUtils.match(actual, expect);
        Assert.assertTrue(result);
    }

    @Test
    public void matchExcudeFields() {
        Person actual = getActual();
        Person expect = getExpect();

        boolean result = CompareUtils.matchExcudeFields(actual, expect, Sets.newHashSet(
                "carList:*:color"
        ));
        Assert.assertTrue(result);
    }

    private Person getActual() {
        Person actual = new Person();
        actual.setName("123");
        actual.setAge(1);

        Person.Car car = new Person.Car();
        car.setColor("红色");
        car.setBrand("奔驰");
        actual.setCarList(Lists.newArrayList());
        return actual;
    }

    private Person getExpect() {
        Person expect = new Person();
        expect.setName("123");
        expect.setAge(1);

        Person.Car car = new Person.Car();
        car.setColor("黑色");
        car.setBrand("奔驰");
        expect.setCarList(Lists.newArrayList());
        return expect;
    }
}
