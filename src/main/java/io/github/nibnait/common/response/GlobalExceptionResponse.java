package io.github.nibnait.common.response;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by nibnait on 2022/04/09
 */
@Data
public class GlobalExceptionResponse implements Serializable {

    public Long code;

    public String message;
}
