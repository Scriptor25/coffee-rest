package io.scriptor.http;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record HTTPRequestMessage(
        @NotNull HTTPMethod method,
        @NotNull String path,
        @NotNull Map<String, List<String>> query,
        @NotNull String protocol,
        @NotNull Map<String, String> headers,
        @NotNull InputStream body
) {
    private static @Nullable String readLine(final @NotNull InputStream stream) throws IOException {
        final var string = new StringBuilder();

        var c = stream.read();
        if (c < 0)
            return null;

        if (c == '\n')
            return "";

        do
            string.append((char) c);
        while ((c = stream.read()) > 0 && c != '\n');

        return string.toString();
    }

    public static @Nullable HTTPRequestMessage read(final @NotNull InputStream stream) throws IOException {

        var line = readLine(stream);
        if (line == null)
            return null;

        line = line.trim();

        final var request  = line.split("\\s+");
        final var method   = HTTPMethod.valueOf(request[0]);
        final var uri      = URI.create(request[1]);
        final var path     = uri.getPath();
        final var protocol = request[2];

        final Map<String, List<String>> query = new HashMap<>();

        if (uri.getQuery() != null) {
            final var params = uri.getQuery().split("&+");
            for (final var param : params) {
                if (param.contains("=")) {
                    final var pair = param.split("=", 2);
                    query.computeIfAbsent(pair[0], _ -> new ArrayList<>()).add(pair[1]);
                }
            }
        }

        final Map<String, String> headers = new HashMap<>();

        while (true) {
            line = readLine(stream);
            if (line == null)
                break;

            line = line.trim();
            if (line.isEmpty())
                break;

            final var header = line.split(":\\s*");
            headers.put(header[0].toLowerCase(), header[1]);
        }

        return new HTTPRequestMessage(method, path, query, protocol, headers, stream);
    }
}
