package io.github.nibnait.common.utils;

import java.util.UUID;

public class UUIDUtils {

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String uuidTrim() {
        return UUIDUtils.uuid().replace("-", "");
    }

}
