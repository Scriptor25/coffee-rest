package io.scriptor.http.result;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public class HTTPResultString extends HTTPResult<String> {

    public HTTPResultString(final int statusCode) {
        super(statusCode);
    }

    public HTTPResultString(final int statusCode, final @NotNull String statusText) {
        super(statusCode, statusText);
    }

    public HTTPResultString(final int statusCode, final @NotNull String statusText, final @NotNull String value) {
        super(statusCode, statusText, value);
    }

    public HTTPResultString(final int statusCode, final @NotNull Map<String, String> headers) {
        super(statusCode, headers);
    }

    public HTTPResultString(
            final int statusCode,
            final @NotNull String statusText,
            final @NotNull Map<String, String> headers
    ) {
        super(statusCode, statusText, headers);
    }

    public HTTPResultString(
            final int statusCode,
            final @NotNull String statusText,
            final @NotNull Map<String, String> headers,
            final @NotNull String value
    ) {
        super(statusCode, statusText, headers, value);
    }

    @Override
    public int getSize() {
        return getBody().getBytes().length;
    }

    @Override
    public @NotNull InputStream getStream() {
        return new ByteArrayInputStream(getBody().getBytes());
    }
}
