package io.scriptor.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public record HTTPRequestMessage(
        HTTPMethod method,
        String path,
        String protocol,
        Map<String, String> headers,
        InputStream body
) {

    public static HTTPRequestMessage read(final InputStream stream) throws IOException {
        final var reader = new BufferedReader(new InputStreamReader(stream));

        var line = reader.readLine();
        if (line == null)
            throw new IOException();

        final var request  = line.split("\\s+");
        final var method   = HTTPMethod.valueOf(request[0]);
        final var path     = request[1];
        final var protocol = request[2];

        final Map<String, String> headers = new HashMap<>();
        while (!(line = reader.readLine()).isEmpty()) {
            final var header = line.split(":\\s*");
            headers.put(header[0], header[1]);
        }

        return new HTTPRequestMessage(method, path, protocol, headers, stream);
    }
}
