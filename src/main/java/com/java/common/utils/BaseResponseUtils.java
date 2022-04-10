package com.java.common.utils;

import com.java.common.enums.ErrorCode;
import com.java.common.exception.ServiceException;
import com.java.common.response.BaseResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@UtilityClass
public class BaseResponseUtils {

    public static <T> T getDataStrongly(BaseResponse<T> baseResponse) {
        if (!ErrorCode.SUCCESS.getCode().equals(baseResponse.code)) {
            throw new ServiceException(baseResponse.code, baseResponse.message);
        }
        return baseResponse.data;
    }

    public static <T> T getDataStrongly(BaseResponse<T> baseResponse, String format, Object... args) {
        if (!ErrorCode.SUCCESS.getCode().equals(baseResponse.code)) {
            log.error(baseResponse.message);
            throw new ServiceException(baseResponse.code, MessageFormatter.arrayFormat(format, args).getMessage());
        }
        return baseResponse.data;
    }


    public static <T> T getSimpleDataWeakly(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T getDataWeakly(Supplier<BaseResponse<T>> supplier) {
        try {
            BaseResponse<T> baseResponse = supplier.get();
            if (!ErrorCode.SUCCESS.getCode().equals(baseResponse.code)) {
                log.error(baseResponse.message);
                return null;
            }
            return baseResponse.data;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> Optional<T> getDataWeaklyOptional(Supplier<BaseResponse<T>> supplier) {
        try {
            BaseResponse<T> baseResponse = supplier.get();
            if (!ErrorCode.SUCCESS.getCode().equals(baseResponse.code)) {
                log.error(baseResponse.message);
                return Optional.empty();
            }
            return Optional.ofNullable(baseResponse.data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public static boolean isSuccess(BaseResponse response) {
        return response != null && ErrorCode.SUCCESS.getCode().equals(response.code);
    }

    public static BaseResponse buildSuccessResMsg(String format, Object... args) {
        return new BaseResponse(ErrorCode.SUCCESS.getCode(), DataUtils.format(format, args), null);
    }

    public static <T> BaseResponse buildSuccessRes(String msg, T data) {
        return new BaseResponse(ErrorCode.SUCCESS.getCode(), msg, data);
    }

    public static BaseResponse buildFailedResMsg(String format, Object... args) {
        return new BaseResponse(ErrorCode.SERVICE_ERROR.getCode(), DataUtils.format(format, args), null);
    }

    public static <T> BaseResponse buildFailedRes(String msg, T data) {
        return new BaseResponse(ErrorCode.SERVICE_ERROR.getCode(), msg, data);
    }

}
