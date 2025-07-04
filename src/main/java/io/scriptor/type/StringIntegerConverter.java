package io.scriptor.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringIntegerConverter implements IConverter<String, Integer> {

    @Override
    public @NotNull Integer from(final @Nullable String source) {
        if (source == null)
            return 0;
        return Integer.parseInt(source);
    }
}
