package cc.tianbin.common.exception;

import cc.tianbin.common.enums.AbstractErrorCode;
import cc.tianbin.common.utils.DataUtils;
import lombok.Data;

/**
 * Created by nibnait on 2022/04/03
 */
@Data
public class ClientViewException extends RuntimeException {

    private Long code;

    private String message;

    public ClientViewException() {
        super();
    }

    public ClientViewException(String message) {
        super(message);
    }

    public ClientViewException(String message, Object... args) {
        super(DataUtils.format(message, args));
    }

    public ClientViewException(Long code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ClientViewException(Long code, String message, Object... args) {
        super(DataUtils.format(message, args));
        this.code = code;
        this.message = DataUtils.format(message, args);
    }

    public ClientViewException(AbstractErrorCode errorCode, String message, Object... args) {
        super(DataUtils.format(message, args));
        this.code = errorCode.getCode();
        this.message = DataUtils.format(message, args);
    }

    public ClientViewException(AbstractErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

}
