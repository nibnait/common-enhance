package io.github.nibnait.common.utils.http;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by nibnait on 2022/04/16
 */
@Slf4j
public class InetAddressUtils {

    /**
     * 获取域名
     *
     * @return 域名字符串
     */
    public static String getHostAddress() {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("InetAddressUtils getHostAddress error ", e);
            return "";
        }
        return address.getHostAddress();
    }

}
