package io.scriptor.http;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class HTTPRoute {

    private final Pattern pattern;
    private final List<String> parameters = new ArrayList<>();

    public HTTPRoute(final @NotNull String endpoint, final @NotNull String resource) {
        this("%s/%s".formatted(endpoint, resource).replaceAll("/+", "/"));
    }

    public HTTPRoute(final @NotNull String path) {
        final var lowerPath = path.toLowerCase();

        final var matcher = Pattern.compile("\\[(.*?)]").matcher(lowerPath);
        final var route   = new StringBuilder().append("^");

        int end = 0;
        while (matcher.find()) {
            final var staticPart = lowerPath.substring(end, matcher.start());
            route.append(Pattern.quote(staticPart));

            final var paramPart = matcher.group(1).trim();
            parameters.add(paramPart);
            route.append("([^\\/]+)");

            end = matcher.end();
        }

        route.append(Pattern.quote(lowerPath.substring(end)));
        route.append("$");

        pattern = Pattern.compile(route.toString());
    }

    public boolean matches(final @NotNull String path) {
        return pattern.matcher(path.toLowerCase()).matches();
    }

    public @Nullable String get(final @NotNull String path, final @NotNull String parameter) {
        final var index = parameters.indexOf(parameter);
        if (index < 0) {
            return null;
        }
        final var matcher = pattern.matcher(path);
        if (!matcher.matches()) {
            return null;
        }
        return matcher.group(index + 1);
    }
}
