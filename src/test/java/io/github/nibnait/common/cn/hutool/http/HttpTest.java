package io.github.nibnait.common.cn.hutool.http;

import cn.hutool.http.HttpUtil;
import org.junit.Test;

/**
 * Created by nibnait on 2022/04/14
 */
public class HttpTest {

    @Test
    public void get() {

        String host = "https://upos-sz-staticks3.bilivideo.com";
        String mp4 = "mallboss/m220413qn18ov6h5ti67802y6phtqeyj.mp4";
        String url = String.format("%s/%s?fop=%s", host, mp4,"xcode_probe");
        System.out.println(url);

        String s = HttpUtil.get(url);


        System.out.println(s);
    }

}
