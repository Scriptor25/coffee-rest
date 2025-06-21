package io.scriptor.result;

import java.io.IOException;

public abstract class ResultBase<T> {

    private final int statusCode;
    private final String statusText;

    private final T value;

    public ResultBase(final int statusCode) {
        this.statusCode = statusCode;
        this.statusText = null;
        this.value = null;
    }

    public ResultBase(final int statusCode, final String statusText) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.value = null;
    }

    public ResultBase(final int statusCode, final String statusText, final T value) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Result( statusCode=%d, statusText=%s, value=%s )".formatted(statusCode, statusText, value);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public T getValue() {
        return value;
    }

    public abstract byte[] getBytes() throws IOException;
}
