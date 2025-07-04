package io.scriptor.http.result;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Map;

public class HTTPResultVoid extends HTTPResult<Void> {

    public HTTPResultVoid(final int statusCode) {
        super(statusCode);
    }

    public HTTPResultVoid(final int statusCode, final @NotNull String statusText) {
        super(statusCode, statusText);
    }

    public HTTPResultVoid(
            final int statusCode,
            final @NotNull String statusText,
            final @NotNull Map<String, String> headers
    ) {
        super(statusCode, statusText, headers);
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public @Nullable InputStream getStream() {
        return null;
    }
}
