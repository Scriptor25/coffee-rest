package io.scriptor.loader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class ResourceClassLoader extends ClassLoader {

    public ResourceClassLoader(final @Nullable ClassLoader parent) {
        super(parent);
    }

    private byte[] loadClassBytes(final @NotNull String className) throws ClassNotFoundException {
        final var fileName = className.replace('.', File.separatorChar) + ".class";
        try (final var classStream = getResourceAsStream(fileName)) {
            final var byteStream = new ByteArrayOutputStream();
            Objects.requireNonNull(classStream).transferTo(byteStream);

            return byteStream.toByteArray();
        } catch (final IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
    }

    @Override
    protected @NotNull Class<?> findClass(final @NotNull String name) throws ClassNotFoundException {
        final var bytes = loadClassBytes(name);
        return defineClass(name, bytes, 0, bytes.length);
    }
}
