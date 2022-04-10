package io.github.nibnait.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ES平台字段查询信息的注解类
 * Balance工具类SDK与公司平台Query协议
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EsPlatQuery {

    /**
     * ES字段名称
     */
    String field() default "";

    /**
     * 字段查询类型
     */
    Type type() default Type.EQ;

    /**
     * Combo查询分组字段
     * groupName而不是用groupNo
     * @return
     */
    String groupName() default "";

    /**
     * 返回查询取值类型 这里的OR都是指的同组 (nested 同组 或者 非nested同组)
     */
    enum Type {
        // =
        EQ,
        // in
        IN,
        // >=
        RANGE_GTE,
        // >
        RANGE_GT,
        // <=
        RANGE_LTE,
        // <
        RANGE_LT,
        // order by field asc
        ASC,
        // order by field desc
        DESC,
        // like
        LIKE,
        // not in
        NOT_IN,
        // !=
        NOT_EQ,
        // 页码
        PAGE,
        // 一页大小
        PAGE_SIZE,
        // 嵌套equal查询
        NESTED_EQ,
        // 嵌套not equal查询
        NESTED_NOT_EQ,
        // 嵌套 >=
        NESTED_RANGE_GTE,
        // 嵌套 >
        NESTED_RANGE_GT,
        // 嵌套 <=
        NESTED_RANGE_LTE,
        // 嵌套 <
        NESTED_RANGE_LT,
        // 嵌套 in
        NESTED_IN,
        // or ==
        OR_EQ,
        // or like
        OR_LIKE,
        // or in
        OR_IN,
        // or >=
        OR_RANGE_GTE,
        // or >
        OR_RANGE_GT,
        // 嵌套 or ==
        OR_NESTED_EQ,
        // 嵌套 or in
        OR_NESTED_IN,
        // 嵌套 or >
        OR_NESTED_RANGE_GT,
        // 嵌套 or >=
        OR_NESTED_RANGE_GTE
        // TODO: 更复杂的SDK的场景
    }
}