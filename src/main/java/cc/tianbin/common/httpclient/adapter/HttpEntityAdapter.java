package cc.tianbin.common.httpclient.adapter;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.springframework.util.StreamUtils;

import java.io.*;

public class HttpEntityAdapter implements HttpEntity {

    private final String moonBoxUniqueId;
    private final HttpEntity httpEntity;
    private byte[] body;

    public HttpEntityAdapter(String moonBoxUniqueId, HttpEntity httpEntity) {
        this.moonBoxUniqueId = moonBoxUniqueId;
        this.httpEntity = httpEntity;
    }

    @Override
    public boolean isRepeatable() {
        return httpEntity.isRepeatable();
    }

    @Override
    public boolean isChunked() {
        return httpEntity.isChunked();
    }

    @Override
    public long getContentLength() {
        return httpEntity.getContentLength();
    }

    @Override
    public Header getContentType() {
        return httpEntity.getContentType();
    }

    @Override
    public Header getContentEncoding() {
        return httpEntity.getContentEncoding();
    }

    @Override
    public InputStream getContent() throws IOException, UnsupportedOperationException {
        if (this.body == null) {
            this.body = StreamUtils.copyToByteArray(httpEntity.getContent());
        }
        return new BufferedInputStream(new ByteArrayInputStream(this.body));
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        httpEntity.writeTo(outstream);
    }

    @Override
    public boolean isStreaming() {
        return httpEntity.isStreaming();
    }

    @Override
    public void consumeContent() throws IOException {
        httpEntity.consumeContent();
    }

    public String fetchMoonBoxUniqueId() {
        return this.moonBoxUniqueId;
    }

    public String fetchContent() {
        return new String(this.body);
    }
}
