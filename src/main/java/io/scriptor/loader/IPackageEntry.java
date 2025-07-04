package io.scriptor.loader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface IPackageEntry {

    record ClassEntry(@NotNull Class<?> type) implements IPackageEntry {
    }

    record IterableEntry(@NotNull List<IPackageEntry> entries) implements IPackageEntry, Iterable<IPackageEntry> {

        public IterableEntry() {
            this(new ArrayList<>());
        }

        @Override
        public @NotNull Iterator<IPackageEntry> iterator() {
            return entries.iterator();
        }
    }

    record FutureEntry(@NotNull ResourceClassLoader classLoader, @NotNull String packageName)
            implements IPackageEntry {
    }
}
