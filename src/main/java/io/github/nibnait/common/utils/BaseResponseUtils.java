package io.github.nibnait.common.utils;

import io.github.nibnait.common.enums.ErrorCode;
import io.github.nibnait.common.exception.ServiceException;
import io.github.nibnait.common.response.BaseResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@UtilityClass
public class BaseResponseUtils {

    public static <T> T getDataStrongly(BaseResponse<T> baseResponse) {
        if (!ErrorCode.SUCCESS.getCode().equals(baseResponse.getCode())) {
            throw new ServiceException(baseResponse.getCode(), baseResponse.getMessage());
        }
        return baseResponse.getData();
    }

    public static <T> T getDataStrongly(BaseResponse<T> baseResponse, String format, Object... args) {
        if (!ErrorCode.SUCCESS.getCode().equals(baseResponse.getCode())) {
            log.error(baseResponse.getMessage());
            throw new ServiceException(baseResponse.getCode(), MessageFormatter.arrayFormat(format, args).getMessage());
        }
        return baseResponse.getData();
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
            if (!ErrorCode.SUCCESS.getCode().equals(baseResponse.getCode())) {
                log.error(baseResponse.getMessage());
                return null;
            }
            return baseResponse.getData();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> Optional<T> getDataWeaklyOptional(Supplier<BaseResponse<T>> supplier) {
        try {
            BaseResponse<T> baseResponse = supplier.get();
            if (!ErrorCode.SUCCESS.getCode().equals(baseResponse.getCode())) {
                log.error(baseResponse.getMessage());
                return Optional.empty();
            }
            return Optional.ofNullable(baseResponse.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public static boolean isSuccess(BaseResponse response) {
        return response != null && ErrorCode.SUCCESS.getCode().equals(response.getCode());
    }

}
