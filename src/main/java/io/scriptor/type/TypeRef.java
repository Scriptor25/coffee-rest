package io.scriptor.type;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeRef<T> {

    private final Type type;

    protected TypeRef() {
        final var superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType parameterized)
            this.type = parameterized.getActualTypeArguments()[0];
        else
            throw new IllegalStateException();
    }

    public @NotNull Type getType() {
        return type;
    }
}
