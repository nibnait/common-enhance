package io.github.nibnait.common.utils;

import io.github.nibnait.common.exception.ClientViewException;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.MessageFormatter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nibnait on 2022/04/03
 */
@Slf4j
public class DataUtils {

    public static final Gson DEFAULT_GSON = new Gson();

    /**
     * 分批调rpc接口 查询数据
     * @param list 入参 eg: skuIdList
     * @param size 一批的数量 eg: 100
     * @param function 一个lambda表达式 eg: skuIdList -> itemsSkuService.batchQuery(skuIdList)
     * @return 将每一次的返回值，用 flatMap 打平 返回一个 list
     */
    public static <T, R> List<R> getDataPartition(List<T> list, Integer size, Function<List<T>, List<R>> function) {
        return Lists.partition(list, size).stream()
                .map(function)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 查询所有分页的数据，返回list
     */
    public static <T> List<T> getPageInfoTotalList(IntFunction<PageInfo<T>> intFunction) {
        List<T> list = Lists.newArrayList();
        int pageNum = 1;
        PageInfo<T> pageInfo;
        do {
            pageInfo = intFunction.apply(pageNum++);
            if (pageInfo == null) {
                break;
            }
            list.addAll(pageInfo.getList());
        } while (pageInfo.isHasNextPage());
        return list;
    }

    /**
     * 返回 list1 - (list1 ∩ list2)
     */
    public static <T> List<T> difference(Collection<T> list1, Collection<T> list2) {
        return Lists.newArrayList(Sets.difference(Sets.newHashSet(list1), Sets.newHashSet(list2)));
    }

    /**
     * 返回交集 list1 ∩ list2
     */
    public static <T> List<T> intersection(Collection<T> list1, Collection<T> list2) {
        return Lists.newArrayList(Sets.intersection(Sets.newHashSet(list1), Sets.newHashSet(list2)));
    }

    /**
     * @param format abc{}e
     * @param args   d
     * @return abcde
     */
    public static String format(String format, Object... args) {
        return MessageFormatter.arrayFormat(format, args).getMessage();
    }

    /**
     * string to Object
     */
    public static <T> T parseObject(String s, Class<T> clazz) {
        if (StringUtils.isBlank(s) || "null".equalsIgnoreCase(s)) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }
        try {
            return JSON.parseObject(s, clazz);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * string to Array
     */
    public static <T> List<T> parseArray(String s, Class<T> clazz) {
        if (StringUtils.isBlank(s) || "null".equalsIgnoreCase(s)) {
            return Lists.newArrayList();
        }
        return JSON.parseArray(s, clazz);
    }

    /**
     * object to string
     */
    public static String toJsonStringObject(Object o) {
        if (o == null) {
            return JSON.toJSONString(Maps.newHashMap());
        }
        return JSON.toJSONString(o);
    }

    /**
     * object to array string
     */
    public static String toJsonStringArray(Object o) {
        if (o == null) {
            return JSON.toJSONString(Lists.newArrayList());
        }
        return JSON.toJSONString(o);
    }

    /**
     * 将 element 加到 list 中
     */
    public static <T> List<T> addToList(T element, List<T> list) {
        list = CollectionUtils.isEmpty(list) ? Lists.newArrayList() : list;
        if (element != null) {
            list.add(element);
            return list.stream().distinct().collect(Collectors.toList());
        }
        return CollectionUtils.isEmpty(list) ? null : list;
    }

    /**
     * 将 element 加到 list 中
     */
    public static <T> List<T> addToList(List<T> element, List<T> list) {
        list = CollectionUtils.isEmpty(list) ? Lists.newArrayList() : list;
        if (CollectionUtils.isNotEmpty(element)) {
            list.addAll(element);
            return list.stream().distinct().collect(Collectors.toList());
        }
        return CollectionUtils.isEmpty(list) ? null : list;
    }

    /**
     * 将 element 从 list 中删除
     */
    public static <T> List<T> removeElement(T element, List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }

        CopyOnWriteArrayList<T> cowList = new CopyOnWriteArrayList<>(list);
        cowList.removeIf(t -> t.equals(element));
        return cowList;
    }

    /**
     * 返回 list中 重复的元素
     */
    public static <T> List<T> getDuplicateElements(List<T> list) {
        return list.stream()
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 判断 list 中 是否包含 t
     */
    public static <T> boolean contains(List<T> list, T t) {
        return CollectionUtils.isNotEmpty(list) && list.contains(t);
    }

    public static Integer getOrDefaultZero(Integer num) {
        return num == null ? 0 : num;
    }

    /**
     * @param list     only allow one size list
     * @param errorMsg errorMsg
     * @param <T>      <T>
     * @return T
     */
    public static <T> T getOneFromList(List<T> list, String errorMsg, Object... args) {
        if (list.size() > 1) {
            throw new ClientViewException(errorMsg, args);
        }
        return list.stream().findFirst().orElseThrow(() -> new ClientViewException(errorMsg, args));
    }

    public static Boolean isAllNull(Object... objects) {
        if (objects == null) {
            return true;
        }
        return Stream.of(objects).allMatch(Objects::isNull);
    }

    public static Boolean isAnyNull(Object... objects) {
        if (objects == null) {
            return true;
        }
        return Stream.of(objects).anyMatch(Objects::isNull);
    }

}
