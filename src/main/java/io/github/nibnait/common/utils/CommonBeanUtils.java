package io.github.nibnait.common.utils;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CommonBeanUtils {

    public static <S, T> T copyProperties(S source, Supplier<T> targetSupplier) {
        if (source == null) {
            return null;
        }
        T target = targetSupplier.get();

        BeanUtils.copyProperties(source, target);

        return target;
    }

    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> targetSupplier) {
        if (CollectionUtils.isEmpty(sources)) {
            return Lists.newArrayList();
        }
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T target = copyProperties(source, targetSupplier);
            list.add(target);
        }
        return list;
    }


}
