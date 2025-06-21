package io.scriptor.http;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Route {

    private final Pattern pattern;
    private final List<String> parameters = new ArrayList<>();

    public Route(final String endpoint, final String resource) {
        this(endpoint + resource);
    }

    public Route(final String path) {
        final var parts = path.split("[\\[\\]]");

        final var route = new StringBuilder().append("^");
        for (int i = 0; i < parts.length; ++i) {
            if (i % 2 == 0) {
                route.append(Pattern.quote(parts[i]));
            } else {
                parameters.add(parts[i]);
                route.append("([^\\/]+)");
            }
        }
        route.append("$");

        pattern = Pattern.compile(route.toString());
    }

    public boolean matches(final String path) {
        return pattern.matcher(path).matches();
    }

    public String get(final String path, final String parameter) {
        final var index = parameters.indexOf(parameter);
        if (index < 0)
            return null;
        final var matcher = pattern.matcher(path);
        if (!matcher.matches())
            return null;
        return matcher.group(index + 1);
    }
}
