package cc.tianbin.common.utils;

import cc.tianbin.common.annotation.Business;
import cc.tianbin.common.annotation.EsPlatQuery;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author seu1tyz
 * 2021-12-23 哔哩哔哩 ES平台工具类
 * <p>
 * 公司的ES平台对原生ES封装了一层查询协议, 这层查询的协议与原生ES完全不同, 但底层仍然是原生ES。
 * 查询开始考虑的是sql的方案，但是平台(包括原生ES)不支持DB那种复杂的sql查询
 * 所以还是改成query的方式: 基于注解、反射的面向对象的方式，对后续的扩展更友好。
 * 非Nested: In, Not in 测试无误 Range经修改后无误 NESTED_RANGE也无误
 * 加入重要的Combo类型结构 实现OR功能 TODO 目前满天星需要会员购的 (A or B) and 其他字段条件 and (C or D) 这种能力
 * <p>
 * TODO: NESTED NOT IN 需要跟平台同事确认Nested的结构，以及这种非同组OR的情况 (A && B) OR (C && D) 需要加额外的groupNo来注解区分组号 -> (20220121 确认这种场景满天星不需要)
 * TODO: 后续仍存在高度定制利用ES查询的场景，需要进一步优化，比如高度利用搜索引擎的能力这种
 */
public class EsPlatUtils {

    private static final String COMMA_LEFT = "(,";
    private static final String COMMA_RIGHT = ",)";
    private static final String EQ = "eq";
    private static final String LIKE = "like";
    private static final String RANGE = "range";
    private static final String COMBO_MIN = "min";
    private static final String COMBO_NESTEDS = "nesteds";
    private static final String IN = "in";
    private static final String NOT = "not";
    private static final Logger LOGGER = LoggerFactory.getLogger(EsPlatUtils.class);

    public static <T> EsPlatRequest build(T queryparams, Class<T> clazz) {
        Business annotation = clazz.getAnnotation(Business.class);
        EsPlatRequest request = EsPlatRequest.build(annotation.indexName());

        Field[] fields = clazz.getDeclaredFields();
        Field[] superFields = clazz.getSuperclass().getDeclaredFields();
        Field[] allFields = (Field[]) ArrayUtils.addAll(fields, superFields);

        HashMap<String, Integer> comboUtilsMap = buildGroupName2ArrayIndexMap(allFields, request);

        for (Field field : allFields) {
            try {
                field.setAccessible(true);
                EsPlatQuery fieldAnnotation = field.getDeclaredAnnotation(EsPlatQuery.class);
                Object value = field.get(queryparams);
                if (null == fieldAnnotation || null == value) {
                    continue;
                }

                String fieldPath = StringUtils.isEmpty(fieldAnnotation.field()) ? field.getName() : fieldAnnotation.field();
                String groupName = StringUtils.isEmpty(fieldAnnotation.groupName()) ? null : fieldAnnotation.groupName();

                int comboListIndex = 0;
                if (groupName != null) {
                    comboListIndex = comboUtilsMap.get(groupName);
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.PAGE) {
                    request.setPn(Integer.parseInt(String.valueOf(value)));
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.PAGE_SIZE) {
                    request.setPs(Integer.parseInt(String.valueOf(value)));
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.EQ) {
                    request.where().eq().put(fieldPath, value);
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.NOT_EQ) {
                    request.where().eq().put(fieldPath, value);
                    request.where().not().putIfAbsent(EQ, new HashMap<>(16));
                    request.where().not().get(EQ).put(fieldPath, true);
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.IN) {
                    request.where().in().put(fieldPath, castList(value, Object.class));
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.NOT_IN) {
                    request.where().in().put(fieldPath, castList(value, Object.class));
                    request.where().not().putIfAbsent(IN, new HashMap<>(16));
                    request.where().not().get(IN).put(fieldPath, true);
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.ASC) {
                    List<Object> objects = castList(value, Object.class);
                    for (Object o : objects) {
                        Map<Object, String> map = new HashMap<>(4);
                        map.put(o, "asc");
                        request.order().add(map);
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.DESC) {
                    List<Object> objects = castList(value, Object.class);
                    for (Object o : objects) {
                        Map<Object, String> map = new HashMap<>(4);
                        map.put(o, "desc");
                        request.order().add(map);
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.RANGE_GT) {
                    String s = request.where().range().getOrDefault(fieldPath, ""); // s -> "(,6)"
                    if (s.startsWith(COMMA_LEFT)) {
                        request.where().range().put(fieldPath, String.format("(%s,", object2Str(value)) + s.substring(2));
                    } else {
                        request.where().range().put(fieldPath, String.format("(%s,)", object2Str(value)));
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.RANGE_GTE) {
                    String s = request.where().range().getOrDefault(fieldPath, "");
                    if (s.startsWith(COMMA_LEFT)) {
                        request.where().range().put(fieldPath, String.format("[%s,", object2Str(value)) + s.substring(2));
                    } else {
                        request.where().range().put(fieldPath, String.format("[%s,)", object2Str(value)));
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.RANGE_LT) {
                    String s = request.where().range().getOrDefault(fieldPath, ""); // s -> (3,)
                    if (s.endsWith(COMMA_RIGHT)) {
                        request.where().range().put(fieldPath, (s.substring(0, s.indexOf(COMMA_RIGHT))) + String.format(",%s)", object2Str(value)));
                    } else {
                        request.where().range().put(fieldPath, String.format("(,%s)", object2Str(value)));
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.RANGE_LTE) {
                    String s = request.where().range().getOrDefault(fieldPath, "");
                    if (s.endsWith(COMMA_RIGHT)) {
                        request.where().range().put(fieldPath, (s.substring(0, s.indexOf(COMMA_RIGHT))) + String.format(",%s]", object2Str(value)));
                    } else {
                        request.where().range().put(fieldPath, String.format("(,%s]", object2Str(value)));
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.LIKE) {
                    if (value instanceof List) {
                        request.where().like().add(Like.build(Lists.newArrayList(fieldPath), castList(value, String.class)));
                    } else {
                        request.where().like().add(Like.build(Lists.newArrayList(fieldPath), Lists.newArrayList(String.valueOf(value))));
                    }
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.NESTED_EQ) {
                    if (fieldPath.contains(".")) { // 表示的是嵌套的查询操作
                        String path = fieldPath.split("\\.")[0];
                        request.where().nested().putIfAbsent(path, new HashMap<>(16));
                        request.where().nested().get(path).putIfAbsent(EQ, new HashMap<>(16));
                        request.where().nested().get(path).get(EQ).put(fieldPath, value);
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.NESTED_NOT_EQ) {
                    if (fieldPath.contains(".")) {
                        String path = fieldPath.split("\\.")[0];
                        request.where().nested().putIfAbsent(path, new HashMap<>(16));
                        request.where().nested().get(path).putIfAbsent(NOT, new HashMap<>(16));
                        request.where().nested().get(path).get(NOT).put(fieldPath, value);
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.NESTED_IN) {
                    if (fieldPath.contains(".")) { // 表示的是嵌套的查询操作
                        String path = fieldPath.split("\\.")[0];
                        request.where().nested().putIfAbsent(path, new HashMap<>(16));
                        request.where().nested().get(path).putIfAbsent(IN, new HashMap<>(16));
                        request.where().nested().get(path).get(IN).put(fieldPath, castList(value, Object.class));
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.NESTED_RANGE_GT) {
                    if (fieldPath.contains(".")) {
                        String path = fieldPath.split("\\.")[0];
                        request.where().nested().putIfAbsent(path, new HashMap<>(16));
                        request.where().nested().get(path).putIfAbsent(RANGE, new HashMap<>(16));
                        String s = (String) request.where().nested().get(path).get(RANGE).getOrDefault(fieldPath, ""); // s -> "(,6)"
                        if (s.startsWith(COMMA_LEFT)) {
                            request.where().nested().get(path).get(RANGE).put(fieldPath, String.format("(%s,", object2Str(value)) + s.substring(2));
                        } else {
                            request.where().nested().get(path).get(RANGE).put(fieldPath, String.format("(%s,)", object2Str(value)));
                        }
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.NESTED_RANGE_GTE) {
                    if (fieldPath.contains(".")) {
                        String path = fieldPath.split("\\.")[0];
                        request.where().nested().putIfAbsent(path, new HashMap<>(16));
                        request.where().nested().get(path).putIfAbsent(RANGE, new HashMap<>(16));
                        String s = (String) request.where().nested().get(path).get(RANGE).getOrDefault(fieldPath, ""); // s -> "(,6)"
                        if (s.startsWith(COMMA_LEFT)) {
                            request.where().nested().get(path).get(RANGE).put(fieldPath, String.format("[%s,", object2Str(value)) + s.substring(2));
                        } else {
                            request.where().nested().get(path).get(RANGE).put(fieldPath, String.format("[%s,)", object2Str(value)));
                        }
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.NESTED_RANGE_LT) {
                    if (fieldPath.contains(".")) {
                        String path = fieldPath.split("\\.")[0];
                        request.where().nested().putIfAbsent(path, new HashMap<>(16));
                        request.where().nested().get(path).putIfAbsent(RANGE, new HashMap<>(16));
                        String s = (String) request.where().nested().get(path).get(RANGE).getOrDefault(fieldPath, ""); // s -> "(,6)"
                        if (s.endsWith(COMMA_RIGHT)) {
                            request.where().nested().get(path).get(RANGE).put(fieldPath, (s.substring(0, s.indexOf(COMMA_RIGHT))) + String.format(",%s)", object2Str(value)));
                        } else {
                            request.where().nested().get(path).get(RANGE).put(fieldPath, String.format("(,%s)", object2Str(value)));
                        }
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.NESTED_RANGE_LTE) {
                    if (fieldPath.contains(".")) {
                        String path = fieldPath.split("\\.")[0];
                        request.where().nested().putIfAbsent(path, new HashMap<>(16));
                        request.where().nested().get(path).putIfAbsent(RANGE, new HashMap<>(16));
                        String s = (String) request.where().nested().get(path).get(RANGE).getOrDefault(fieldPath, ""); // s -> "(,6)"
                        if (s.endsWith(COMMA_RIGHT)) {
                            request.where().nested().get(path).get(RANGE).put(fieldPath, (s.substring(0, s.indexOf(COMMA_RIGHT))) + String.format(",%s]", object2Str(value)));
                        } else {
                            request.where().nested().get(path).get(RANGE).put(fieldPath, String.format("(,%s]", object2Str(value)));
                        }
                    }
                    continue;
                }

                // 下面是OR的场景 (参考会员购业务, 需要实现 -> (A or B) and (C or D) 这种基础业务能力)
                if (fieldAnnotation.type() == EsPlatQuery.Type.OR_EQ) {
                    Map<String, Object> directOrEqMap = new HashMap<>();
                    directOrEqMap.put(fieldPath, value);
                    request.where().combo().get(comboListIndex).eq().add(directOrEqMap);
                    request.where().combo().get(comboListIndex).min().putIfAbsent(EQ, 1);
                    request.where().combo().get(comboListIndex).min().putIfAbsent(COMBO_MIN, 1);
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.OR_LIKE) {
                    Like like = null;
                    if (value instanceof List) {
                        like = Like.build(Lists.newArrayList(fieldPath), castList(value, String.class));
                    } else {
                        like = Like.build(Lists.newArrayList(fieldPath), Lists.newArrayList(String.valueOf(value)));
                    }

                    request.where().combo().get(comboListIndex).like().add(like);
                    request.where().combo().get(comboListIndex).min().putIfAbsent(LIKE, 1);
                    request.where().combo().get(comboListIndex).min().putIfAbsent(COMBO_MIN, 1);
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.OR_IN) {
                    Map<String, List<Object>> directOrInMap = new HashMap<>();
                    directOrInMap.put(fieldPath, castList(value, Object.class));
                    request.where().combo().get(comboListIndex).in().add(directOrInMap);
                    request.where().combo().get(comboListIndex).min().putIfAbsent(IN, 1);
                    request.where().combo().get(comboListIndex).min().putIfAbsent(COMBO_MIN, 1);
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.OR_RANGE_GT) {
                    Map<String, String> directOrGtMap = new HashMap<>();
                    directOrGtMap.put(fieldPath, String.format("(%s,)", object2Str(value)));
                    request.where().combo().get(comboListIndex).range().add(directOrGtMap);
                    request.where().combo().get(comboListIndex).min().putIfAbsent(RANGE, 1);
                    request.where().combo().get(comboListIndex).min().putIfAbsent(COMBO_MIN, 1);
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.OR_RANGE_GTE) {
                    Map<String, String> directOrGteMap = new HashMap<>();
                    directOrGteMap.put(fieldPath, String.format("[%s,)", object2Str(value)));
                    request.where().combo().get(comboListIndex).range().add(directOrGteMap);
                    request.where().combo().get(comboListIndex).min().putIfAbsent(RANGE, 1);
                    request.where().combo().get(comboListIndex).min().putIfAbsent(COMBO_MIN, 1);
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.OR_NESTED_EQ) {
                    if (fieldPath.contains(".")) { // 表示的是嵌套的查询操作
                        String path = fieldPath.split("\\.")[0];
                        Map<String, Object> nestedDirectEqMap = new HashMap<>();
                        nestedDirectEqMap.put(fieldPath, value);
                        Map<String, Map<String, Map<String, Object>>> map = new HashMap<>();
                        Map<String, Map<String, Object>> tempMap = new HashMap<>();
                        tempMap.put(EQ, nestedDirectEqMap);
                        map.put(path, tempMap);
                        request.where().combo().get(comboListIndex).nesteds().add(map);
                        request.where().combo().get(comboListIndex).min().putIfAbsent(COMBO_NESTEDS, 1);
                        request.where().combo().get(comboListIndex).min().putIfAbsent(COMBO_MIN, 1);
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.OR_NESTED_IN) {
                    if (fieldPath.contains(".")) { // 表示的是嵌套的查询操作
                        String path = fieldPath.split("\\.")[0];
                        Map<String, Object> nestedDirectInMap = new HashMap<>();
                        nestedDirectInMap.put(fieldPath, castList(value, Object.class));
                        Map<String, Map<String, Map<String, Object>>> map = new HashMap<>();
                        Map<String, Map<String, Object>> tempMap = new HashMap<>();
                        tempMap.put(IN, nestedDirectInMap);
                        map.put(path, tempMap);
                        request.where().combo().get(comboListIndex).nesteds().add(map);
                        request.where().combo().get(comboListIndex).min().putIfAbsent(COMBO_NESTEDS, 1);
                        request.where().combo().get(comboListIndex).min().putIfAbsent(COMBO_MIN, 1);
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.OR_NESTED_RANGE_GT) {
                    if (fieldPath.contains(".")) { // 表示的是嵌套的查询操作
                        String path = fieldPath.split("\\.")[0];
                        Map<String, Object> nestedDirectGtMap = new HashMap<>();
                        nestedDirectGtMap.put(fieldPath, String.format("(%s,)", object2Str(value)));
                        Map<String, Map<String, Map<String, Object>>> map = new HashMap<>();
                        Map<String, Map<String, Object>> tempMap = new HashMap<>();
                        tempMap.put(RANGE, nestedDirectGtMap);
                        map.put(path, tempMap);
                        request.where().combo().get(comboListIndex).nesteds().add(map);
                        request.where().combo().get(comboListIndex).min().putIfAbsent(COMBO_NESTEDS, 1);
                        request.where().combo().get(comboListIndex).min().putIfAbsent(COMBO_MIN, 1);
                    }
                    continue;
                }

                if (fieldAnnotation.type() == EsPlatQuery.Type.OR_NESTED_RANGE_GTE) {
                    if (fieldPath.contains(".")) { // 表示的是嵌套的查询操作
                        String path = fieldPath.split("\\.")[0];
                        Map<String, Object> nestedDirectGteMap = new HashMap<>();
                        nestedDirectGteMap.put(fieldPath, String.format("[%s,)", object2Str(value)));
                        Map<String, Map<String, Map<String, Object>>> map = new HashMap<>();
                        Map<String, Map<String, Object>> tempMap = new HashMap<>();
                        tempMap.put(RANGE, nestedDirectGteMap);
                        map.put(path, tempMap);
                        request.where().combo().get(comboListIndex).nesteds().add(map);
                        request.where().combo().get(comboListIndex).min().putIfAbsent(COMBO_NESTEDS, 1);
                        request.where().combo().get(comboListIndex).min().putIfAbsent(COMBO_MIN, 1);
                    }
                    continue;
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("get field fail with reflect, fieldName={}", field.getName());
            }
        }

        return request;
    }

    private static HashMap<String, Integer> buildGroupName2ArrayIndexMap(Field[] allFields, EsPlatRequest request) {
        HashMap<String, Integer> map = new HashMap<>();
        int arrayIndex = 0;
        for (Field field : allFields) {
            field.setAccessible(true);
            EsPlatQuery fieldAnnotation = field.getDeclaredAnnotation(EsPlatQuery.class);
            if (fieldAnnotation == null) { // 防止Query未被注释的情况
                continue;
            }
            String groupName = StringUtils.isEmpty(fieldAnnotation.groupName()) ? null : fieldAnnotation.groupName();
            if (groupName != null && !map.containsKey(groupName)) {
                map.put(groupName, arrayIndex++);
            }
        }

        for (int i = 0; i < arrayIndex; i++) {
            request.where().combo().add(new Combo());
        }

        return map;
    }

    // 对传入的(value -> List)做in的query拼接
    private static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    private static String object2Str(Object value) {
        if (value instanceof Date) {
            return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(value);
        } else {
            return String.valueOf(value);
        }
    }

    @Data
    public static class EsPlatRequest {
        // 查询的字段名称
        private List<String> fields;

        // 查询哪些索引，多个用逗号隔开
        private String from;

        // 页码
        private Integer pn;

        // 一页数量
        private Integer ps;

        // 排序规则
        private List<Map<Object, String>> order;

        // 查询条件
        private Where where;

        public static EsPlatRequest build(String from) {
            EsPlatRequest request = new EsPlatRequest();
            request.setFrom(from);

            return request;
        }

        private Where where() {
            if (null == where) {
                where = new Where();
            }

            return where;
        }

        private List<Map<Object, String>> order() {
            if (null == order) {
                order = new ArrayList<>();
            }

            return order;
        }
    }

    // Combo结构抽象类 -> 这样写是对的，而不是抽象出来data的写法，后续的扩展性也更高
    @Data
    public static class Combo {

        private static Combo combo = null;

        private Combo() {
        }

        private Map<String, Integer> min;

        private List<Map<String, Object>> eq;

        private List<Map<String, List<Object>>> in;

        private List<Map<String, Map<String, Map<String, Object>>>> nesteds;

        private List<Map<String, String>> range;

        private List<Like> like;

        private List<Like> like() {
            if(null == like) {
                like = new ArrayList<>();
            }
            return like;
        }

        private List<Map<String, Object>> eq() {
            if (null == eq) {
                eq = new ArrayList<>();
            }
            return eq;
        }

        public List<Map<String, List<Object>>> in() {
            if (null == in) {
                in = new ArrayList<>();
            }
            return in;
        }

        public List<Map<String, String>> range() {
            if (null == range) {
                range = new ArrayList<>();
            }
            return range;
        }

        public Map<String, Integer> min() {
            if (null == min) {
                min = new HashMap<>();
            }
            return min;
        }

        public List<Map<String, Map<String, Map<String, Object>>>> nesteds() {
            if (null == nesteds) {
                nesteds = new ArrayList<>();
            }
            return nesteds;
        }
    }

    @Data
    public static class Where {
        // =
        private Map<String, Object> eq;

        // in 最多100个值，超过时分多次请求
        private Map<String, List<Object>> in;

        // 范围
        private Map<String, String> range;

        // 模糊查询
        private List<Like> like;

        // 针对eq in条件 取反
        private Map<String, Map<String, Boolean>> not;

        // 嵌套查询
        private Map<String, Map<String, Map<String, Object>>> nested;

        private List<Combo> combo; // 必须要传singleList否则远端服务器会认为是非法的Json

        private List<Combo> combo() {
            if (null == combo) {
                combo = new ArrayList<>();
            }
            return combo;
        }

        private Map<String, Map<String, Map<String, Object>>> nested() {
            if (null == nested) {
                nested = new HashMap<>(16);
            }
            return nested;
        }

        private Map<String, Object> eq() {
            if (null == eq) {
                eq = new HashMap<>(16);
            }

            return eq;
        }

        private Map<String, List<Object>> in() {
            if (null == in) {
                in = new HashMap<>(16);
            }

            return in;
        }

        private Map<String, String> range() {
            if (null == range) {
                range = new HashMap<>(16);
            }

            return range;
        }

        private List<Like> like() {
            if (null == like) {
                like = new ArrayList<>();
            }

            return like;
        }

        private Map<String, Map<String, Boolean>> not() {
            if (null == not) {
                not = new HashMap<>(16);
            }

            return not;
        }
    }

    @Data
    public static class Like {
        private List<String> kw_fields;
        private List<String> kw;
        private String level = "low";
        private boolean or = true;

        public static Like build(List<String> kwFields, List<String> kw) {
            Like like = new Like();
            like.kw_fields = kwFields;
            like.kw = kw;

            return like;
        }
    }

    @Data
    public static class EsPlatBaseResponse<T> implements Serializable {
        private Long code;

        private String message;

        private EsPlatResponse<T> data;
    }

    @Data
    public static class EsPlatResponse<T> implements Serializable {
        private List<T> result;

        private Page page;
    }

    @Data
    public static class Page {
        private Long total;

        private Integer num;

        private Integer size;
    }
}