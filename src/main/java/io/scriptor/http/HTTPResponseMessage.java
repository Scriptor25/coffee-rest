package io.scriptor.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public record HTTPResponseMessage(
        String protocol,
        int statusCode,
        String statusText,
        Map<String, String> headers,
        InputStream body,
        boolean isChunked
) {

    private static void writeString(final OutputStream stream, final String value) throws IOException {
        for (final var b : value.getBytes())
            stream.write(b);
    }

    private static void writeInt(final OutputStream stream, final int value) throws IOException {
        final var string = Integer.toString(value);
        writeString(stream, string);
    }

    public void write(final OutputStream stream) throws IOException {
        writeString(stream, "%s %s %s\r\n".formatted(protocol, statusCode, statusText));
        for (final var entry : headers.entrySet())
            writeString(stream, "%s: %s\r\n".formatted(entry.getKey(), entry.getValue()));
        writeString(stream, "\r\n");
        stream.flush();

        if (body != null) {
            if (!isChunked) {
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
