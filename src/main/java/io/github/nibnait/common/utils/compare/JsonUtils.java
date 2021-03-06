package io.github.nibnait.common.utils.compare;

import io.github.nibnait.common.bo.compare.CompareBO;
import io.github.nibnait.common.bo.compare.CompareResultDTO;
import io.github.nibnait.common.utils.DataUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.json.comparison.JsonCompare;
import com.json.comparison.JsonComparisonResult;
import com.json.comparison.comprator.model.api.FieldComparison;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class JsonUtils {

    public static CompareResultDTO diffJsonObject(JSONObject actualObject, JSONObject expectObject, Set<String> focusFields) {
        CompareResultDTO compareResultDTO = new CompareResultDTO();
        if (CollectionUtils.isEmpty(focusFields)) {
            compareResultDTO.setMatch(true);
            return compareResultDTO;
        }

        List<CompareResultDTO.FieldComparison> modifiedFields = Lists.newArrayList();
        for (String key : focusFields) {
            String actual = actualObject.getString(key);
            String expect = expectObject.getString(key);
            if (!CompareUtils.matchObject(actual, expect)) {
                addModifyFields(key, actual, expect, modifiedFields);
            }
        }

        compareResultDTO.setMatch(CollectionUtils.isEmpty(modifiedFields));
        compareResultDTO.setModifiedFields(modifiedFields);
        return compareResultDTO;
    }

    private static void addModifyFields(String key, String actual, String expect, List<CompareResultDTO.FieldComparison> modifiedFields) {
        CompareResultDTO.FieldComparison comparison = new CompareResultDTO.FieldComparison();
        comparison.setField(key);
        comparison.setExpected(expect);
        comparison.setActual(actual);

        modifiedFields.add(comparison);
    }

    public static CompareResultDTO diffStr(String actualStr, String expectStr, CompareBO compareBO) {
        // ????????????????????? json, ??? json ?????????, ?????????????????????
        if (!JacksonUtils.isJson(actualStr)) {
            try {
                actualStr = JSON.toJSONString(JSON.parseObject(actualStr), SerializerFeature.WriteMapNullValue);
            } catch (Exception e) {
                // ignore
            }
        }
        if (!JacksonUtils.isJson(expectStr)) {
            try {
                expectStr = JSON.toJSONString(JSON.parseObject(expectStr), SerializerFeature.WriteMapNullValue);
            } catch (Exception e) {
                // ignore
            }
        }
        return diffStr0(actualStr, expectStr, compareBO);
    }

    private static CompareResultDTO diffStr0(String actualStr, String expectStr, CompareBO compareBO) {
        CompareResultDTO compareResultDTO = new CompareResultDTO();
        compareResultDTO.setMatch(true);

        // ???????????? json ??????, ???????????????????????????
        if (!JacksonUtils.isJson(actualStr) || !JacksonUtils.isJson(expectStr)) {
            compareResultDTO.setMatch(actualStr.equals(expectStr));
        }
        Set<CompareBO.OptimizationType> optimizations = compareBO.getOptimizations();
        Set<String> focusFields = compareBO.getFocusFields();
        Set<String> excludeFields = compareBO.getExcludeFields();
        // 1. ?????????????????????: ???????????????????????????????????????
        for (CompareBO.OptimizationType optimization : optimizations) {
            // ?????????????????????, ???????????? data:list ????????????
            if (optimization == CompareBO.OptimizationType.PAGE_HELPER) {
                // ????????????????????????, ??????????????? navigatePages???pageSize ?????????
                if (expectStr.contains("navigatePages") && expectStr.contains("pageSize")) {
                    focusFields.add("list");
                }
            }
        }
        // 2. ????????????: ?????????????????????????????????
        if (excludeFields.size() > 0) {
            for (String excludeField : excludeFields) {
                actualStr = JacksonUtils.replace(actualStr, excludeField, null);
                expectStr = JacksonUtils.replace(expectStr, excludeField, null);
            }
        }
        // 3. ???????????????: ?????????????????????
        if (focusFields.size() <= 0) {
            return jsonEqual(actualStr, expectStr, compareBO);
        }
        // 4. ???????????????: ??????????????????
        ArrayList<CompareResultDTO.FieldComparison> modifiedFields = Lists.newArrayList();
        ArrayList<CompareResultDTO.FieldComparison> missingFields = Lists.newArrayList();
        ArrayList<CompareResultDTO.FieldComparison> newFields = Lists.newArrayList();
        for (String focusField : focusFields) {
            String actualValue = JacksonUtils.focus(actualStr, focusField);
            String expectValue = JacksonUtils.focus(expectStr, focusField);

            compareBO.setCurrentField(focusField);
            CompareResultDTO currentFieldCompareResult = jsonEqual(actualValue, expectValue, compareBO);

            if (!currentFieldCompareResult.isMatch()) {
                compareResultDTO.setMatch(false);
            }
            modifiedFields.addAll(currentFieldCompareResult.getModifiedFields());
            missingFields.addAll(currentFieldCompareResult.getMissingFields());
            newFields.addAll(currentFieldCompareResult.getNewFields());
        }
        // ??????????????????????????????, ???????????? true
        compareResultDTO.setModifiedFields(modifiedFields);
        compareResultDTO.setMissingFields(missingFields);
        compareResultDTO.setNewFields(newFields);

        return compareResultDTO;
    }

    public static CompareResultDTO jsonEqual(String actual, String expect, CompareBO compareBO) {
        CompareResultDTO compareResultDTO = new CompareResultDTO(false);
        // fix null ??? str
        if (StringUtils.isBlank(actual) || StringUtils.isBlank(expect)
                || !JacksonUtils.isJson(actual) || !JacksonUtils.isJson(expect)) {
            boolean equals = actual.equals(expect);
            compareResultDTO.setMatch(equals);
            if (!equals) {
                compareResultDTO.setModifiedFields(Lists.newArrayList(new CompareResultDTO.FieldComparison(compareBO.getCurrentField(), expect, actual)));
            }
            return compareResultDTO;
        }

        // 5. ?????? json ??????????????????
        JsonComparisonResult comparisonResult = compare(actual, expect, compareBO);

        if (comparisonResult == null) {
            return compareResultDTO;
        }

        compareResultDTO.setMatch(comparisonResult.isMatch());
        compareResultDTO.setModifiedFields(getFieldComparisonList(compareBO.getCurrentField(), comparisonResult.getModifiedFields()));
        compareResultDTO.setMissingFields(getFieldComparisonList(compareBO.getCurrentField(), comparisonResult.getMissingFields()));
        compareResultDTO.setNewFields(getFieldComparisonList(compareBO.getCurrentField(), comparisonResult.getNewFields()));

        // ????????????????????????, ???????????? true
        if (comparisonResult.isMatch()) {
            compareResultDTO.setMatch(true);
        }
        // 6. ?????????????????????, ??????????????????
        if (CollectionUtils.isNotEmpty(comparisonResult.getModifiedFields())) {
            compareResultDTO.setMatch(false);
            return compareResultDTO;
        }
        // 7. ??????????????????, ?????????????????????, ????????????????????????, ????????????????????????, ?????????????????????????????????????????????
        // 7.1 ??????????????????, ???????????? true
        if (compareBO.getOptimizations().contains(CompareBO.OptimizationType.IGNORE_NEW_FIELDS) &&
                compareBO.getOptimizations().contains(CompareBO.OptimizationType.IGNORE_MISSING_FIELDS)) {
            compareResultDTO.setMatch(true);
        }
        // 7.2 ???????????????????????????, ????????????????????????????????????????????????
        else if (compareBO.getOptimizations().contains(CompareBO.OptimizationType.IGNORE_NEW_FIELDS)) {
            compareResultDTO.setMatch(CollectionUtils.isEmpty(comparisonResult.getMissingFields()));
        }
        // 7.3 ???????????????????????????, ???????????????????????????????????????
        else {
            compareResultDTO.setMatch(CollectionUtils.isEmpty(comparisonResult.getNewFields()));
        }

        return compareResultDTO;
    }

    private static JsonComparisonResult compare(String actual, String expect, CompareBO compareBO) {
        JsonComparisonResult comparisonResult = null;
        try {
            comparisonResult = JsonCompare.builder().build().compare(expect, actual);
        } catch (Exception e) {
            log.error("JsonUtils.jsonEqual error", e);
        }

        if (comparisonResult == null || !comparisonResult.isMatch()) {
            log.info("JsonUtils.jsonEqual false compareBO:{}", JSON.toJSONString(compareBO));
            log.info("JsonUtils.jsonEqual false actual:{}", actual);
            log.info("JsonUtils.jsonEqual false expect:{}", expect);
        }
        return comparisonResult;
    }

    private static List<CompareResultDTO.FieldComparison> getFieldComparisonList(String currentField, Collection<FieldComparison> modifiedFieldList) {
        return modifiedFieldList.stream()
                .map(comparison -> convertToFieldComparison(currentField, comparison))
                .collect(Collectors.toList());
    }

    private static CompareResultDTO.FieldComparison convertToFieldComparison(String currentField, FieldComparison comparison) {
        CompareResultDTO.FieldComparison fieldComparison = new CompareResultDTO.FieldComparison();

        try {
            Integer[] indexArray = getIndexArray(comparison.getField());
            currentField = currentField.replace(JacksonUtils.ARRAY_SIGN, JacksonUtils.PLACEHOLDER_SIGN);
            fieldComparison.setField(DataUtils.format(currentField, indexArray));
        } catch (Exception e) {
            if (StringUtils.isBlank(currentField)) {
                currentField = comparison.getField();
            }
            if ("list".equals(currentField)) {
                currentField = currentField + comparison.getField();
            }
            fieldComparison.setField(currentField);
        }

        fieldComparison.setExpected(comparison.getExpected());
        fieldComparison.setActual(comparison.getActual());
        return fieldComparison;
    }

    private static Integer[] getIndexArray(String field) {
        JSONArray jsonArray;
        try {
            jsonArray = JSON.parseArray(field);
            Integer[] indexArray = new Integer[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                indexArray[i] = (int) jsonArray.get(i);
            }
            return indexArray;
        } catch (Exception e) {
            field = "[" + field + "]";
            jsonArray = JSON.parseArray(field);
            Integer[] indexArray = new Integer[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONArray arr = (JSONArray) jsonArray.get(i);
                indexArray[i] = (int) arr.get(0);
            }
            return indexArray;
        }


    }

}
