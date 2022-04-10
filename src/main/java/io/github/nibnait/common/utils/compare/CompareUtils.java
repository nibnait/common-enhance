package io.github.nibnait.common.utils.compare;

import io.github.nibnait.common.bo.compare.CompareBO;
import io.github.nibnait.common.bo.compare.CompareResultDTO;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Set;

@Slf4j
public class CompareUtils {

    /**
     * 对比两个对象的所有字段是否一致
     * @param actual 实际值
     * @param expect 期望值
     */
    public static <T> boolean match(T actual, T expect) {
        return matchExcudeFields(actual, expect, Sets.newHashSet());
    }

    /**
     * 对比两个对象的所有字段是否一致，排除某些字段
     * @param actual 实际值
     * @param expect 期望值
     * @param excludeFields 排除的字段 list
     */
    public static <T> boolean matchExcudeFields(T actual, T expect, Set<String> excludeFields) {
        CompareBO compareBO = CompareBO.newNoOptimizeCompareBO();
        compareBO.setExcludeFields(excludeFields);

        CompareResultDTO compareResultDTO = JsonUtils.diffStr(JSON.toJSONString(actual), JSON.toJSONString(expect), compareBO);
        if (!compareResultDTO.isMatch()) {
            log.info("差异字段: {}", JSON.toJSONString(compareResultDTO));
        }
        return compareResultDTO.isMatch();
    }

    /**
     * 仅对比两个对象中 focusFields list 字段是否一致
     * @param actual 实际值
     * @param expect 期望值
     * @param focusFields 想要对比的字段 list
     */
    public static <T> boolean matchFocusFields(T actual, T expect, Set<String> focusFields) {
        CompareBO compareBO = CompareBO.newNoOptimizeCompareBO();
        compareBO.setFocusFields(focusFields);

        CompareResultDTO compareResultDTO = JsonUtils.diffStr(JSON.toJSONString(actual), JSON.toJSONString(expect), compareBO);
        if (!compareResultDTO.isMatch()) {
            log.info("差异字段: {}", JSON.toJSONString(compareResultDTO));
        }
        return compareResultDTO.isMatch();
    }

    /**
     * 对比两个 PageInfo对象 的所有字段是否一致
     * @param actual 实际值
     * @param expect 期望值
     */
    public static <T> boolean matchPage(T actual, T expect) {
        return matchPageExculdeFields(actual, expect, Sets.newHashSet());
    }

    /**
     * 对比两个 PageInfo对象 的所有字段是否一致，排除某些字段
     * @param actual 实际值
     * @param expect 期望值
     * @param excludeFields 排除的字段 list
     */
    public static <T> boolean matchPageExculdeFields(T actual, T expect, Set<String> excludeFields) {
        CompareBO compareBO = CompareBO.newNoOptimizeCompareBO();
        compareBO.setOptimizations(Sets.newHashSet(CompareBO.OptimizationType.PAGE_HELPER));
        compareBO.setExcludeFields(excludeFields);

        CompareResultDTO compareResultDTO = JsonUtils.diffStr(JSON.toJSONString(actual), JSON.toJSONString(expect), compareBO);
        if (!compareResultDTO.isMatch()) {
            log.info("差异字段: {}", JSON.toJSONString(compareResultDTO));
        }
        return compareResultDTO.isMatch();
    }

    /**
     * 仅对比两个 PageInfo对象 中 focusFields list 字段是否一致
     * @param actual 实际值
     * @param expect 期望值
     * @param focusFields 想要对比的字段 list
     */
    public static <T> boolean matchPageFocusFields(T actual, T expect, Set<String> focusFields) {
        CompareBO compareBO = CompareBO.newNoOptimizeCompareBO();
        compareBO.setOptimizations(Sets.newHashSet(CompareBO.OptimizationType.PAGE_HELPER));
        compareBO.setFocusFields(focusFields);

        CompareResultDTO compareResultDTO = JsonUtils.diffStr(JSON.toJSONString(actual), JSON.toJSONString(expect), compareBO);
        if (!compareResultDTO.isMatch()) {
            log.info("差异字段: {}", JSON.toJSONString(compareResultDTO));
        }
        return compareResultDTO.isMatch();
    }

    /**
     * 直接调用两个 object 的 .equals 方法，对比
     * @param actualObj 实际值
     * @param expectObj 期望值
     */
    public static boolean matchObject(Object actualObj, Object expectObj) {

        if ((expectObj == null && actualObj != null) || (expectObj != null && actualObj == null)) {
            return false;
        }

        if (expectObj == null) {
            return true;
        }

        if (!expectObj.getClass().equals(actualObj.getClass())) {
            return false;
        }

        if (expectObj instanceof Number && expectObj instanceof Comparable) {
            return ((Comparable) expectObj).compareTo(actualObj) == 0;
        }

        if (expectObj instanceof Date && expectObj instanceof Comparable) {
            return ((Comparable) expectObj).compareTo(actualObj) == 0;
        }

        return expectObj.equals(actualObj);
    }

}
