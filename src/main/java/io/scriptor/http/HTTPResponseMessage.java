package io.scriptor.http;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public record HTTPResponseMessage(
        @NotNull String protocol,
        int statusCode,
        @NotNull String statusText,
        @NotNull Map<String, String> headers,
        @Nullable InputStream body,
        boolean chunked
) {

    private static void writeString(final @NotNull OutputStream stream, final @NotNull String value)
            throws IOException {
        for (final var b : value.getBytes())
            stream.write(b);
    }

    public void write(final @NotNull OutputStream stream) throws IOException {
        writeString(stream, "%s %s %s\r\n".formatted(protocol, statusCode, statusText));
        for (final var entry : headers.entrySet())
            writeString(stream, "%s: %s\r\n".formatted(entry.getKey(), entry.getValue()));
        writeString(stream, "\r\n");
        stream.flush();

        if (body != null) {
            if (!chunked) {
                body.transferTo(stream);
                stream.flush();
            } else {
                for (int n; (n = body.available()) > 0; ) {
                    writeString(stream, "%x\r\n".formatted(n));
                    stream.write(body.readNBytes(n));
                    writeString(stream, "\r\n");
                    stream.flush();
                }
                writeString(stream, "0\r\n\r\n");
                stream.flush();
            }
        }
    }
}
