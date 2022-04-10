package io.github.nibnait.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by nibnait on 2022/04/03
 */
@AllArgsConstructor
@Getter
public enum DeleteType {

    IS_DEFAULT((byte) 0, "未删除"),
    IS_DELETE((byte) 1, "已删除");

    private final Byte code;
    private final String description;
}
