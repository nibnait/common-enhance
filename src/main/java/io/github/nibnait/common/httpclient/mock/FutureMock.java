package io.github.nibnait.common.httpclient.mock;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureMock<T> implements Future<T> {

    private final String moonBoxUniqueId;

    public FutureMock(String moonBoxUniqueId) {
        this.moonBoxUniqueId = moonBoxUniqueId;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return (T) new HttpResponseMock(moonBoxUniqueId);
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return (T) new HttpResponseMock(moonBoxUniqueId);
    }
}
