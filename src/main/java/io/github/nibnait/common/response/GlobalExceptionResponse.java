package io.github.nibnait.common.response;

import io.github.nibnait.common.enums.ErrorCode;
import io.github.nibnait.common.utils.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by nibnait on 2022/04/09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalExceptionResponse implements Serializable {

    private Long code;

    private String message;

    public static GlobalExceptionResponse build(ErrorCode errorCode) {
        return new GlobalExceptionResponse(errorCode.getCode(), errorCode.getMessage());
    }

    public static GlobalExceptionResponse fail(String format, Object... args) {
        return new GlobalExceptionResponse(ErrorCode.SERVICE_ERROR.getCode(), DataUtils.format(format, args));
    }

}
