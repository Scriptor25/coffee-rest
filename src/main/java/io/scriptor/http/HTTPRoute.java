package io.scriptor.http;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HTTPRoute implements Comparable<HTTPRoute> {

    public record Parameter(int index, boolean collecting) {
    }

    private final Path path;

    private final int priority;
    private final int index;

    private final Pattern pattern;
    private final Map<String, Parameter> parameters = new HashMap<>();

    public HTTPRoute(final @NotNull String endpoint, final @NotNull String resource) {
        this(Path.of(endpoint, resource));
    }

    public HTTPRoute(final @NotNull Path path) {
        this.path = path.toAbsolutePath();

        final var pathname = this.path.toString().toLowerCase();

        final var matcher = Pattern.compile("\\[(.*?)]").matcher(pathname);
        final var route   = new StringBuilder().append("^");

        int end = 0;

        int segmentCount = 0;

        int staticChars = 0;
        int staticCount = 0;
        int staticFirst = Integer.MAX_VALUE;

        int parameterCount  = 0;
        int collectingCount = 0;

        while (matcher.find()) {
            final var staticPart = pathname.substring(end, matcher.start());

            if (!staticPart.isEmpty()) {
                route.append(Pattern.quote(staticPart));

                final var parts = staticPart.split("/");
                for (final var part : parts) {
                    if (!part.isEmpty()) {
                        staticChars += part.length();
                        staticCount++;

                        if (staticFirst == Integer.MAX_VALUE) {
                            staticFirst = segmentCount;
                        }

                        segmentCount++;
                    }
                }
            }

            final var parameter = matcher.group(1).trim();

            final var collecting = parameter.endsWith("+");
            final var name       = collecting ? parameter.substring(0, parameter.length() - 1) : parameter;

            parameters.put(name, new Parameter(parameterCount, collecting));

            route.append(collecting ? "(.+)" : "([^\\/]+)");

            if (collecting) {
                collectingCount++;
            }

            parameterCount++;
            segmentCount++;

            end = matcher.end();
        }

        final var tail = pathname.substring(end);
        route.append(Pattern.quote(tail));
        route.append("$");

        if (!tail.isEmpty()) {
            final var parts = tail.split("/");

            for (final var part : parts) {
                if (!part.isEmpty()) {
                    staticChars += part.length();
                    staticCount++;

                    if (staticFirst == Integer.MAX_VALUE) {
                        staticFirst = segmentCount;
                    }

                    segmentCount++;
                }
            }
        }

        pattern = Pattern.compile(route.toString());

        int score = 0;

        score += staticCount * 100;
        score -= collectingCount * 50;
        score += staticChars;
        score += segmentCount * 5;

        priority = score;
        index = staticFirst;
    }

    public boolean matches(final @NotNull String path) {
        return pattern.matcher(path.toLowerCase()).matches();
    }

    public @Nullable Object get(final @NotNull String path, final @NotNull String name) {
        if (!parameters.containsKey(name)) {
            return null;
        }

        final var matcher = pattern.matcher(path);
        if (!matcher.matches()) {
            return null;
        }

        final var parameter = parameters.get(name);

        final var value = matcher.group(parameter.index() + 1);
        if (!parameter.collecting()) {
            return value;
        }

        return value.split("/");
    }

    @Override
    public int compareTo(final @NotNull HTTPRoute other) {
        if (this.priority != other.priority) {
            return Integer.compare(this.priority, other.priority);
        }
        return Integer.compare(other.index, this.index);
    }

    @Override
    public @NotNull String toString() {
        return path.toString();
    }
}
