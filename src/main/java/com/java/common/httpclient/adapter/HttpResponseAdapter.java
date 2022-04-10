package com.java.common.httpclient.adapter;

import org.apache.http.*;
import org.apache.http.params.HttpParams;

import java.util.Locale;

public class HttpResponseAdapter implements HttpResponse {

    private final HttpResponse delegate;
    private final String moonBoxUniqueId;

    public HttpResponseAdapter(HttpResponse delegate, String moonBoxUniqueId) {
        this.delegate = delegate;
        this.moonBoxUniqueId = moonBoxUniqueId;
    }

    @Override
    public StatusLine getStatusLine() {
        return delegate.getStatusLine();
    }

    @Override
    public void setStatusLine(StatusLine statusline) {
        delegate.setStatusLine(statusline);
    }

    @Override
    public void setStatusLine(ProtocolVersion ver, int code) {
        delegate.setStatusLine(ver, code);
    }

    @Override
    public void setStatusLine(ProtocolVersion ver, int code, String reason) {
        delegate.setStatusLine(ver, code, reason);
    }

    @Override
    public void setStatusCode(int code) throws IllegalStateException {
        delegate.setStatusCode(code);
    }

    @Override
    public void setReasonPhrase(String reason) throws IllegalStateException {
        delegate.setReasonPhrase(reason);
    }

    @Override
    public HttpEntity getEntity() {
        return new HttpEntityAdapter(moonBoxUniqueId, delegate.getEntity());
    }

    @Override
    public void setEntity(HttpEntity entity) {
        delegate.setEntity(entity);
    }

    @Override
    public Locale getLocale() {
        return delegate.getLocale();
    }

    @Override
    public void setLocale(Locale loc) {
        delegate.setLocale(loc);
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return delegate.getProtocolVersion();
    }

    @Override
    public boolean containsHeader(String name) {
        return delegate.containsHeader(name);
    }

    @Override
    public Header[] getHeaders(String name) {
        return delegate.getHeaders(name);
    }

    @Override
    public Header getFirstHeader(String name) {
        return delegate.getFirstHeader(name);
    }

    @Override
    public Header getLastHeader(String name) {
        return delegate.getLastHeader(name);
    }

    @Override
    public Header[] getAllHeaders() {
        return delegate.getAllHeaders();
    }

    @Override
    public void addHeader(Header header) {
        delegate.addHeader(header);
    }

    @Override
    public void addHeader(String name, String value) {
        delegate.addHeader(name, value);
    }

    @Override
    public void setHeader(Header header) {
        delegate.setHeader(header);
    }

    @Override
    public void setHeader(String name, String value) {
        delegate.setHeader(name, value);
    }

    @Override
    public void setHeaders(Header[] headers) {
        delegate.setHeaders(headers);
    }

    @Override
    public void removeHeader(Header header) {
        delegate.removeHeader(header);
    }

    @Override
    public void removeHeaders(String name) {
        delegate.removeHeaders(name);
    }

    @Override
    public HeaderIterator headerIterator() {
        return delegate.headerIterator();
    }

    @Override
    public HeaderIterator headerIterator(String name) {
        return delegate.headerIterator(name);
    }

    @Override
    public HttpParams getParams() {
        return delegate.getParams();
    }

    @Override
    public void setParams(HttpParams params) {
        delegate.setParams(params);
    }
}
