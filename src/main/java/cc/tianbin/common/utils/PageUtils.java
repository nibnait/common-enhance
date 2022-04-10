package cc.tianbin.common.utils;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

@UtilityClass
public class PageUtils {

    public static <T, R> PageInfo<R> convertPageInfo(PageInfo<T> sourcePage, List<R> list) {
        if (sourcePage == null) {
            return new PageInfo<>();
        }

        PageInfo<R> newPageInfo = CommonBeanUtils.copyProperties(sourcePage, PageInfo::new);
        if (newPageInfo == null || CollectionUtils.isEmpty(list)) {
            newPageInfo.setList(list);
            return newPageInfo;
        }
        newPageInfo.setList(list);
        return newPageInfo;
    }

    public static <T, R> PageInfo<R> convertPageInfo(PageInfo<T> sourcePage, Function<T, R> function) {
        if (sourcePage == null) {
            return new PageInfo<>();
        }

        PageInfo<R> newPageInfo = CommonBeanUtils.copyProperties(sourcePage, PageInfo::new);
        if (newPageInfo == null || sourcePage.getList() == null) {
            return newPageInfo;
        }
        newPageInfo.setList(sourcePage.getList().stream().map(function).collect(Collectors.toList()));
        return newPageInfo;
    }

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

    public static <T> List<T> getPageInfoTotalList(IntFunction<PageInfo<T>> intFunction, int totalAmount) {
        List<T> list = Lists.newArrayList();
        int pageNum = 1;
        PageInfo<T> pageInfo;
        do {
            pageInfo = intFunction.apply(pageNum++);
            if (pageInfo == null) {
                break;
            }
            list.addAll(pageInfo.getList());
        } while (list.size() <= totalAmount);
        return list;
    }

}
