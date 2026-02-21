package io.scriptor.type;

import io.scriptor.http.result.HTTPResult;
import io.scriptor.http.result.HTTPResultChannel;
import io.scriptor.http.result.HTTPResultVoid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.channels.ReadableByteChannel;

public class ChannelResultConverter implements IConverter<ReadableByteChannel, HTTPResult<?>> {

    @Override
    public @NotNull HTTPResult<?> from(final @Nullable ReadableByteChannel source) {
        if (source == null) {
            return new HTTPResultVoid(200, "OK");
        }
        return new HTTPResultChannel(200, "OK", source);
    }
}
