package io.scriptor.http.result;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class HTTPResult<T> {

    private final int statusCode;
    private final String statusText;

    private final Map<String, String> headers;

    private final T body;

    public HTTPResult(final int statusCode) {
        this(statusCode, "", new HashMap<>());
    }

    public HTTPResult(final int statusCode, final @NotNull String statusText) {
        this(statusCode, statusText, new HashMap<>());
    }

    public HTTPResult(final int statusCode, final @NotNull String statusText, final @NotNull T body) {
        this(statusCode, statusText, new HashMap<>(), body);
    }

    public HTTPResult(final int statusCode, final @NotNull Map<String, String> headers) {
        this(statusCode, "", headers);
    }

    public HTTPResult(
            final int statusCode,
            final @NotNull String statusText,
            final @NotNull Map<String, String> headers
    ) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.headers = new HashMap<>(headers);
        this.body = null;
    }

    public HTTPResult(
            final int statusCode,
            final @NotNull String statusText,
            final @NotNull Map<String, String> headers,
            final @NotNull T body
    ) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.headers = new HashMap<>(headers);
        this.body = body;
    }

    @Override
    public String toString() {
        return "Result( statusCode=%d, statusText=%s, value=%s )".formatted(statusCode, statusText, body);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public @NotNull T getBody() {
        if (body == null) {
            throw new IllegalStateException();
        }
        return body;
    }

    public abstract int getSize();

    public abstract @Nullable InputStream getStream();
}
