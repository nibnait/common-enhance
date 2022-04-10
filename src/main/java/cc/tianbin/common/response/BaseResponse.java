package cc.tianbin.common.response;

import cc.tianbin.common.enums.ErrorCode;

import java.io.Serializable;

/**
 * Created by nibnait on 2022/04/03
 */
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public Long code;

    public String message;

    public T data;

    public Integer errtag = 0;

    public BaseResponse() {
        code = ErrorCode.SUCCESS.getCode();
        message = ErrorCode.SUCCESS.getMessage();
    }

    /**
     * 通用返回
     *
     * @param code
     * @param message
     * @param data
     */
    public BaseResponse(Long code, String message, T data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 通用返回
     *
     * @param code
     * @param errtag
     * @param message
     * @param data
     */
    public BaseResponse(Long code, Integer errtag, String message, T data) {
        super();
        this.code = code;
        this.errtag = errtag;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回正常数据
     *
     * @param data
     */
    public BaseResponse(T data) {
        super();
        this.code = ErrorCode.SUCCESS.getCode();
        this.message = ErrorCode.SUCCESS.getMessage();
        this.data = data;
    }


    /**
     * 返回异常数据，错误号在ErrorCode中定义
     */
    public BaseResponse(ErrorCode e, T data) {
        super();
        this.code = e.getCode();
        this.message = e.getMessage();
        this.data = data;
    }

}

