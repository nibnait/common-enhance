package io.github.nibnait.common.httpclient.adapter;

import org.apache.http.HttpResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureAdapter<T> implements Future<T> {

    private final Future<T> delegate;
    private final String moonBoxUniqueId;

    public FutureAdapter(Future<T> delegate, String moonBoxUniqueId) {
        this.delegate = delegate;
        this.moonBoxUniqueId = moonBoxUniqueId;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return delegate.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return delegate.isCancelled();
    }

    @Override
    public boolean isDone() {
        return delegate.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        T t = delegate.get();
        if (t instanceof HttpResponse) {
            return (T) new HttpResponseAdapter((HttpResponse) t, moonBoxUniqueId);
        } else {
            return t;
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        T t = delegate.get(timeout, unit);
        if (t instanceof HttpResponse) {
            return (T) new HttpResponseAdapter((HttpResponse) t, moonBoxUniqueId);
        } else {
            return t;
        }
    }

    public String fetchMoonBoxUniqueId() {
        return this.moonBoxUniqueId;
    }
}
