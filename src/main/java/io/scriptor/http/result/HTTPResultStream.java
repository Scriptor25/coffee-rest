package io.scriptor.http.result;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.Map;

public class HTTPResultStream extends HTTPResult<InputStream> {

    public HTTPResultStream(final int statusCode) {
        super(statusCode);
    }

    public HTTPResultStream(final int statusCode, final @NotNull String statusText) {
        super(statusCode, statusText);
    }

    public HTTPResultStream(final int statusCode, final @NotNull String statusText, final @NotNull InputStream value) {
        super(statusCode, statusText, value);
    }

    public HTTPResultStream(final int statusCode, final @NotNull Map<String, String> headers) {
        super(statusCode, headers);
    }

    public HTTPResultStream(
            final int statusCode,
            final @NotNull String statusText,
            final @NotNull Map<String, String> headers
    ) {
        super(statusCode, statusText, headers);
    }

    public HTTPResultStream(
            final int statusCode,
            final @NotNull String statusText,
            final @NotNull Map<String, String> headers,
            final @NotNull InputStream value
    ) {
        super(statusCode, statusText, headers, value);
    }

    @Override
    public int getSize() {
        return -1;
    }

    @Override
    public @NotNull InputStream getStream() {
        return getBody();
    }
}
