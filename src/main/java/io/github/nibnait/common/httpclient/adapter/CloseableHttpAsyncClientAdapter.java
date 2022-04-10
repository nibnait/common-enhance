package io.github.nibnait.common.httpclient.adapter;

import io.github.nibnait.common.utils.UUIDUtils;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.concurrent.Future;

public class CloseableHttpAsyncClientAdapter extends CloseableHttpAsyncClient {

    private final CloseableHttpAsyncClient delegate;

    public CloseableHttpAsyncClientAdapter(CloseableHttpAsyncClient delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isRunning() {
        return delegate.isRunning();
    }

    @Override
    public void start() {
        delegate.start();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

    /**
     * 流量录制/回放拦截点
     * 录制返回 return new FutureAdapter<>(future, moonBoxUniqueId);
     * 回放返回 return new FutureMock<>(moonBoxUniqueId);
     */
    @Override
    public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpContext context, FutureCallback<T> callback) {
        String moonBoxUniqueId = UUIDUtils.uuidTrim();
        Future<T> future = delegate.execute(requestProducer, responseConsumer, context, callback);
        // 录制
        return new FutureAdapter<>(future, moonBoxUniqueId);
    }

}
