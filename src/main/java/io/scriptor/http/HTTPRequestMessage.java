package io.scriptor.http;

import io.scriptor.log.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public record HTTPRequestMessage(
        HTTPMethod method,
        String path,
        String protocol,
        Map<String, String> headers,
        InputStream body
) {
    private static String readLine(final InputStream stream) throws IOException {
        final var string = new StringBuilder();
        for (int c; (c = stream.read()) > 0 && c != '\n'; )
            string.append((char) c);
        return string.toString();
    }

    public static HTTPRequestMessage read(final InputStream stream) throws IOException {

        var line = readLine(stream).trim();

        final var request  = line.split("\\s+");
        final var method   = HTTPMethod.valueOf(request[0]);
        final var path     = request[1];
        final var protocol = request[2];

        final Map<String, String> headers = new HashMap<>();
        while (!(line = readLine(stream).trim()).isEmpty()) {
            final var header = line.split(":\\s*");
            headers.put(header[0], header[1]);
        }

        return new HTTPRequestMessage(method, path, protocol, headers, stream);
    }
}
