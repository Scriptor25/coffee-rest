package io.scriptor.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Stream;

public class Loader implements Iterable<Class<?>> {

    private static Class<?>[] load(final String packageName) throws IOException, ClassNotFoundException {
        final List<Class<?>> classes = new ArrayList<>();

        final var packagePath = packageName.replaceAll("\\.", "/");
        try (final var packageStream = ClassLoader.getSystemResourceAsStream(packagePath)) {
            if (packageStream == null)
                return new Class<?>[0];

            final var reader = new BufferedReader(new InputStreamReader(packageStream));
            for (String line; (line = reader.readLine()) != null; ) {
                if (!line.endsWith(".class")) {
                    classes.addAll(Arrays.asList(load("%s.%s".formatted(packageName, line))));
                    continue;
                }

                final var className = "%s.%s".formatted(packageName, line.replaceAll("\\.class", ""));
                classes.add(ClassLoader.getSystemClassLoader().loadClass(className));
            }
        }

        return classes.toArray(Class<?>[]::new);
    }

    private final Class<?>[] classes;

    public Loader(final String packageName) throws IOException, ClassNotFoundException {
        classes = load(packageName);
    }

    public Stream<Class<?>> stream() {
        return Arrays.stream(classes);
    }

    @Override
    public Iterator<Class<?>> iterator() {
        return new Iterator<>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < classes.length;
            }

            @Override
            public Class<?> next() {
                if (index >= classes.length)
                    throw new NoSuchElementException();
                return classes[index++];
            }
        };
    }
}
