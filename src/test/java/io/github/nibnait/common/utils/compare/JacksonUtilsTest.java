package io.github.nibnait.common.utils.compare;

import org.junit.Test;

/**
 * Created by nibnait on 2022/05/06
 */
public class JacksonUtilsTest {

    @Test
    public void isJson() {
//        String actual = "1";
//        System.out.println(JacksonUtils.isJson(actual));
//        actual = "{\"asd\": {1:1}}";
//        System.out.println(JacksonUtils.isJson(actual));

        String actual = "{\"autoOnSaleTime\":0,\"commissionChannelList\":[1],\"commissionRatioMap\":{\"1\":0}}";
        System.out.println(JacksonUtils.isJson(actual));
        actual = "{\"autoOnSaleTime\":0,\"commissionChannelList\":[1],\"commissionRatioMap\":{1:0}}";
        System.out.println(JacksonUtils.isJson(actual));
    }

}
