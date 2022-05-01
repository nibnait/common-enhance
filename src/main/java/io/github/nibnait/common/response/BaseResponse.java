package io.github.nibnait.common.response;

import io.github.nibnait.common.enums.ErrorCode;
import io.github.nibnait.common.utils.DataUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by nibnait on 2022/04/03
 */
@Data
public class BaseResponse<T> implements Serializable {

    private Long code;

    private String message;

    private T data;

    // ------------------ success ---------------------------//
    public static BaseResponse success() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ErrorCode.SUCCESS.getCode());
        baseResponse.setMessage(ErrorCode.SUCCESS.getMessage());
        return baseResponse;
    }

    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setCode(ErrorCode.SUCCESS.getCode());
        baseResponse.setMessage(ErrorCode.SUCCESS.getMessage());
        baseResponse.setData(data);
        return baseResponse;
    }

    public static BaseResponse successMsg(String format, Object... args) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ErrorCode.SUCCESS.getCode());
        baseResponse.setMessage(DataUtils.format(format, args));
        return baseResponse;
    }

    public static <T> BaseResponse successRes(String msg, T data) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setCode(ErrorCode.SUCCESS.getCode());
        baseResponse.setMessage(msg);
        baseResponse.setData(data);
        return baseResponse;
    }

    // ------------------ fail ---------------------------//
    public static BaseResponse fail() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ErrorCode.SERVICE_ERROR.getCode());
        baseResponse.setMessage(ErrorCode.SERVICE_ERROR.getMessage());
        return baseResponse;
    }

    public static BaseResponse fail(ErrorCode errorCode) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(errorCode.getCode());
        baseResponse.setMessage(errorCode.getMessage());
        return baseResponse;
    }

    public static BaseResponse failMsg(String format, Object... args) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ErrorCode.SERVICE_ERROR.getCode());
        baseResponse.setMessage(DataUtils.format(format, args));
        return baseResponse;
    }

    public static <T> BaseResponse failRes(String msg, T data) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setCode(ErrorCode.SERVICE_ERROR.getCode());
        baseResponse.setMessage(msg);
        baseResponse.setData(data);
        return baseResponse;
    }
}

