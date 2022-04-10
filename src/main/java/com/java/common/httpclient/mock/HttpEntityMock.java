package com.java.common.httpclient.mock;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.io.*;

public class HttpEntityMock implements HttpEntity {

    private final String moonBoxUniqueId;

    public HttpEntityMock(String moonBoxUniqueId) {
        this.moonBoxUniqueId = moonBoxUniqueId;
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public Header getContentType() {
        return null;
    }

    @Override
    public Header getContentEncoding() {
        return null;
    }

    /**
     * 立即响应回放
     */
    @Override
    public InputStream getContent() throws IOException, UnsupportedOperationException {
        return new BufferedInputStream(new ByteArrayInputStream("default mock".getBytes()));
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {

    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public void consumeContent() throws IOException {

    }

    public String fetchMoonBoxUniqueId() {
        return this.moonBoxUniqueId;
    }
}
