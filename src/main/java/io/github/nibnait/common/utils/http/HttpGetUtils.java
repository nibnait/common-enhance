package io.github.nibnait.common.utils.http;

import io.github.nibnait.common.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class HttpGetUtils {

    /**
     * 通过get请求得到读取器响应数据的数据流
     */
    public static InputStream getInputStreamByGet(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                return inputStream;
            }

        } catch (IOException e) {
            log.error(DataUtils.format("HttpFileUtils getInputStreamByGet {} error", url), e);
        }

        return null;
    }

}
