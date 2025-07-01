package io.scriptor.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public final class ResourceClassLoader extends ClassLoader {

    public ResourceClassLoader(final ClassLoader parent) {
        super(parent);
    }

    private byte[] loadClassBytes(final String className) throws ClassNotFoundException {
        final var fileName = className.replace('.', File.separatorChar) + ".class";
        try (final var classStream = getResourceAsStream(fileName)) {
            assert classStream != null;

            final var byteStream = new ByteArrayOutputStream();
            classStream.transferTo(byteStream);

            return byteStream.toByteArray();
        } catch (final IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        final var bytes = loadClassBytes(name);
        return defineClass(name, bytes, 0, bytes.length);
    }
}
