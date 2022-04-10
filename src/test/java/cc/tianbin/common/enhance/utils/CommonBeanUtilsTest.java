package cc.tianbin.common.enhance.utils;

import cc.tianbin.common.mock.Person;
import cc.tianbin.common.utils.CommonBeanUtils;
import cc.tianbin.common.utils.compare.CompareUtils;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nibnait on 2021/11/01
 */
public class CommonBeanUtilsTest {

    @Test
    public void copyProperties() {

        Person person = getSourceP();
        Person result = CommonBeanUtils.copyProperties(person, Person::new);

        Assert.assertTrue(CompareUtils.match(result, person));

    }

    @Test
    public void copyListProperties() {

        ArrayList<Person> people = Lists.newArrayList(getSourceP());
        List<Person> result = CommonBeanUtils.copyListProperties(people, Person::new);

        Assert.assertTrue(CompareUtils.match(result, people));

    }

    private Person getSourceP() {
        Person person = new Person();
        person.setName("123");
        person.setAge(11);

        Person.Car car = new Person.Car();
        car.setColor("红色");
        car.setBrand("法拉利");

        person.setCarList(Lists.newArrayList(car));

        return person;
    }

}
