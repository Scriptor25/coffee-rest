package io.scriptor.loader;

import io.scriptor.loader.IPackageEntry.IterableEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DirectoryClassLoader extends ResourceClassLoader {

    private final File rootFile;

    public DirectoryClassLoader(final @Nullable ClassLoader parentLoader, final @NotNull File rootFile) {
        super(parentLoader);
        this.rootFile = rootFile;
    }

    @Override
    public @NotNull IterableEntry getClassPackage(final @NotNull String packageName)
            throws IOException, ClassNotFoundException {

        final var fileName = packageName.replace('.', File.separatorChar);
        final var file     = new File(rootFile, fileName);

        if (!file.exists() || !file.isDirectory())
            return new IterableEntry();

        final var files = file.listFiles();
        if (files == null)
            return new IterableEntry();

        final List<IPackageEntry> entries = new ArrayList<>();

        for (final var entry : files) {
            final var entryName = "%s.%s".formatted(packageName, entry.getName().replaceAll("\\.class", ""));

            if (!entry.getName().endsWith(".class")) {
                entries.add(new IPackageEntry.FutureEntry(this, entryName));
                continue;
            }

            entries.add(new IPackageEntry.ClassEntry(loadClass(entryName)));
        }

        return new IterableEntry(entries);
    }

    @Override
    public @Nullable InputStream getResourceAsStream(final @NotNull String fileName) {
        try {
            return new FileInputStream(new File(rootFile, fileName));
        } catch (final FileNotFoundException e) {
            return null;
        }
    }
}
