package io.scriptor.http;

import java.io.*;
import java.util.Map;

public record HTTPResponseMessage(
        String protocol,
        int statusCode,
        String statusText,
        Map<String, String> headers,
        InputStream body
) {

    public void write(final OutputStream stream) throws IOException {
        final var writer = new BufferedWriter(new OutputStreamWriter(stream));

        writer.write("%s %s %s\r\n".formatted(protocol, statusCode, statusText));
        for (final var entry : headers.entrySet())
            writer.write("%s: %s\r\n".formatted(entry.getKey(), entry.getValue()));
        writer.write("\r\n");
        writer.flush();
        if (body != null)
            body.transferTo(stream);
        stream.flush();
    }
}
