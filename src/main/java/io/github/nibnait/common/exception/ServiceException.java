package io.github.nibnait.common.exception;

import io.github.nibnait.common.enums.AbstractErrorCode;
import io.github.nibnait.common.utils.DataUtils;
import lombok.Data;

/**
 * Created by nibnait on 2022/04/03
 */
@Data
public class ServiceException extends RuntimeException {

    private Long code;

    private String message;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Object... args) {
        super(DataUtils.format(message, args));
    }

    public ServiceException(Long code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ServiceException(Long code, String message, Object... args) {
        super(DataUtils.format(message, args));
        this.code = code;
        this.message = DataUtils.format(message, args);
    }

    public ServiceException(AbstractErrorCode errorCode, String message, Object... args) {
        super(DataUtils.format(message, args));
        this.code = errorCode.getCode();
        this.message = DataUtils.format(message, args);
    }

    public ServiceException(AbstractErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

}
