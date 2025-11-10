package io.scriptor.loader;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public record ClassTree(@NotNull ResourceClassLoader classLoader, @NotNull String packageName)
        implements ITree<Class<?>> {

    private record ClassEntry(@NotNull Class<?> value) implements ITree<Class<?>> {

        @Override
        public Iterator<ITree<Class<?>>> children() {
            return Collections.emptyIterator();
        }

        @Override
        public boolean hasValue() {
            return true;
        }
    }

    @Override
    public Iterator<ITree<Class<?>>> children() {
        return new Iterator<>() {

            private final Iterator<IPackageEntry> iterator;

            {
                try {
                    iterator = classLoader.getClassPackage(packageName).iterator();
                } catch (final IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public ITree<Class<?>> next() {
                final var entry = iterator.next();
                if (entry instanceof IPackageEntry.ClassEntry(Class<?> type)) {
                    return new ClassEntry(type);
                }
                if (entry instanceof IPackageEntry.FutureEntry(ResourceClassLoader loader, String name)) {
                    return new ClassTree(loader, name);
                }
                throw new NoSuchElementException();
            }
        };
    }

    @Override
    public Class<?> value() {
        return null;
    }

    @Override
    public boolean hasValue() {
        return false;
    }
}
