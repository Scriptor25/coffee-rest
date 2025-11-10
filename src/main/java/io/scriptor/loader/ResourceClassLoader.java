package io.scriptor.loader;

import io.scriptor.log.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.scriptor.loader.IPackageEntry.*;

public class ResourceClassLoader extends ClassLoader {

    public ResourceClassLoader(final @Nullable ClassLoader parentLoader) {
        super(parentLoader);
    }

    public @NotNull IterableEntry getClassPackage(final @NotNull String packageName)
            throws IOException, ClassNotFoundException {
        final var fileName = packageName.replace('.', File.separatorChar);
        try (final var packageStream = getResourceAsStream(fileName)) {
            if (packageStream == null) {
                return new IterableEntry();
            }

            final List<IPackageEntry> entries = new ArrayList<>();

            final var reader = new BufferedReader(new InputStreamReader(packageStream));
            for (String line; (line = reader.readLine()) != null; ) {
                final var lineNoClass = line.replaceAll("\\.class", "");
                final var entryName = packageName.isEmpty()
                                      ? lineNoClass
                                      : "%s.%s".formatted(packageName, lineNoClass);

                if (!line.endsWith(".class")) {
                    entries.add(new FutureEntry(this, entryName));
                    continue;
                }

                entries.add(new ClassEntry(loadClass(entryName)));
            }

            return new IterableEntry(entries);
        }
    }

    @Override
    protected @NotNull Class<?> findClass(final @NotNull String className) throws ClassNotFoundException {
        Log.info("query class %s", className);
        final var fileName = className.replace('.', File.separatorChar) + ".class";
        try (final var classStream = getResourceAsStream(fileName)) {
            final var classBytes = Objects.requireNonNull(classStream).readAllBytes();
            return defineClass(className, classBytes, 0, classBytes.length);
        } catch (final IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
    }
}
