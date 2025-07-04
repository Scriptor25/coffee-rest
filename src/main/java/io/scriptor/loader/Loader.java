package io.scriptor.loader;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Loader implements Iterable<Class<?>> {

    private static @NotNull List<Class<?>> load(
            final @NotNull ClassLoader classLoader,
            final @NotNull String packageName
    )
            throws IOException, ClassNotFoundException {
        final List<Class<?>> classes = new ArrayList<>();

        final var packagePath = packageName.replaceAll("\\.", "/");
        try (final var packageStream = classLoader.getResourceAsStream(packagePath)) {
            if (packageStream == null)
                return new ArrayList<>();

            final var reader = new BufferedReader(new InputStreamReader(packageStream));
            for (String line; (line = reader.readLine()) != null; ) {
                if (!line.endsWith(".class")) {
                    classes.addAll(load(classLoader, "%s.%s".formatted(packageName, line)));
                    continue;
                }

                final var className = "%s.%s".formatted(packageName, line.replaceAll("\\.class", ""));
                classes.add(classLoader.loadClass(className));
            }
        }

        return classes;
    }

    private final ClassLoader classLoader;
    private final List<Class<?>> classes;

    public Loader(final @NotNull String packageName) throws IOException, ClassNotFoundException {
        this.classLoader = new ResourceClassLoader(ClassLoader.getSystemClassLoader());
        this.classes = load(this.classLoader, packageName);
    }

    public @NotNull ClassLoader getClassLoader() {
        return classLoader;
    }

    public @NotNull Stream<Class<?>> stream() {
        return classes.stream();
    }

    @Override
    public @NotNull Iterator<Class<?>> iterator() {
        return classes.iterator();
    }
}
