package io.scriptor.type;

import io.scriptor.http.result.HTTPResult;
import io.scriptor.http.result.HTTPResultStream;
import io.scriptor.http.result.HTTPResultVoid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public class StreamResultConverter implements IConverter<InputStream, HTTPResult<?>> {

    @Override
    public @NotNull HTTPResult<?> from(final @Nullable InputStream source) {
        if (source == null)
            return new HTTPResultVoid(200, "OK");
        return new HTTPResultStream(200, "OK", source);
    }
}
