package io.scriptor.http.result;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;

public class HTTPResultChannel extends HTTPResult<ReadableByteChannel> {

    public HTTPResultChannel(final int statusCode) {
        super(statusCode);
    }

    public HTTPResultChannel(final int statusCode, final @NotNull String statusText) {
        super(statusCode, statusText);
    }

    public HTTPResultChannel(
            final int statusCode,
            final @NotNull String statusText,
            final @NotNull ReadableByteChannel value
    ) {
        super(statusCode, statusText, value);
    }

    public HTTPResultChannel(final int statusCode, final @NotNull Map<String, String> headers) {
        super(statusCode, headers);
    }

    public HTTPResultChannel(
            final int statusCode,
            final @NotNull String statusText,
            final @NotNull Map<String, String> headers
    ) {
        super(statusCode, statusText, headers);
    }

    public HTTPResultChannel(
            final int statusCode,
            final @NotNull String statusText,
            final @NotNull Map<String, String> headers,
            final @NotNull ReadableByteChannel value
    ) {
        super(statusCode, statusText, headers, value);
    }

    @Override
    public int getSize() {
        return -1;
    }

    @Override
    public @NotNull InputStream getStream() {
        return new InputStream() {

            @Override
            public int read(final byte @NotNull [] b, final int off, final int len) throws IOException {
                if (len == 0) {
                    return 0;
                }

                if (getBody().isOpen()) {
                    final var buffer = ByteBuffer.wrap(b).limit(off + len).position(off);
                    return getBody().read(buffer);
                }

                return -1;
            }

            @Override
            public long transferTo(final @NotNull OutputStream out) throws IOException {
                long count = 0;

                while (getBody().isOpen()) {
                    final var buffer = ByteBuffer.wrap(new byte[1024]);
                    final var read   = getBody().read(buffer);

                    if (read < 0) {
                        break;
                    }

                    out.write(buffer.array(), 0, read);
                    count += read;
                }

                return count;
            }

            @Override
            public int read() throws IOException {
                if (getBody().isOpen()) {
                    final var buffer = ByteBuffer.allocate(1);
                    getBody().read(buffer);
                    return buffer.get(0);
                }

                return -1;
            }
        };
    }
}
