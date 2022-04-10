package cc.tianbin.common.enhance.utils;

import cc.tianbin.common.utils.DataUtils;
import cc.tianbin.common.utils.compare.CompareUtils;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by nibnait on 2021/09/14
 */
public class DataUtilsTest {

    @Test
    public void addElementToList() {
        List<Integer> list = Lists.newArrayList();

        Integer i1 = null;
        list = DataUtils.addToList(i1, list);
        Integer i2 = 1;
        list = DataUtils.addToList(i2, list);
        Integer i3 = 1;
        list = DataUtils.addToList(i3, list);
        Assert.assertTrue(CompareUtils.matchObject(Lists.newArrayList(1), list));

        Integer i4 = 2;
        list = DataUtils.addToList(i4, list);
        Assert.assertTrue(CompareUtils.matchObject(Lists.newArrayList(1, 2), list));
    }

    @Test
    public void addListToList() {
        List<Integer> subList = Lists.newArrayList(1, 2, 3);
        List<Integer> list = Lists.newArrayList(1, 2, 3);
//        List<Integer> list = Lists.newArrayList(4, 5, 6);
        List<Integer> integers = DataUtils.addToList(subList, list);
        System.out.println(integers);
    }

    @Test
    public void removeElement() {

        List<Integer> list = Lists.newArrayList(11, 22, 33);
        List<Integer> result = DataUtils.removeElement(11, list);
        List<Integer> expect = Lists.newArrayList(22, 33);
        Assert.assertTrue(CompareUtils.match(result, expect));

        list = Lists.newArrayList(11, 22, 33);
        result = DataUtils.removeElement(22, list);
        expect = Lists.newArrayList(11, 33);
        Assert.assertTrue(CompareUtils.match(result, expect));

        list = Lists.newArrayList(11, 22, 33);
        result = DataUtils.removeElement(33, list);
        expect = Lists.newArrayList(11, 22);
        Assert.assertTrue(CompareUtils.match(result, expect));

        list = Lists.newArrayList(11, 22, 33);
        result = DataUtils.removeElement(3, list);
        expect = Lists.newArrayList(11, 22, 33);
        Assert.assertTrue(CompareUtils.match(result, expect));
    }
}
