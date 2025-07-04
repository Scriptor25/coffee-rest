package io.scriptor.loader;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Iterator;

public class Loader implements Iterable<Class<?>> {

    private final ITree<Class<?>> classes;

    public Loader(final @NotNull String packageName) {
        this(new ResourceClassLoader(ClassLoader.getSystemClassLoader()), packageName);
    }

    public Loader(final @NotNull String fileName, final @NotNull String packageName) {
        this(new DirectoryClassLoader(ClassLoader.getSystemClassLoader(), new File(fileName)), packageName);
    }

    public Loader(final @NotNull ResourceClassLoader classLoader, final @NotNull String packageName) {
        this.classes = new ClassTree(classLoader, packageName);
    }

    @Override
    public @NotNull Iterator<Class<?>> iterator() {
        return this.classes.iterator();
    }
}
