package io.scriptor.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;

public interface IConverter<S, D> {

    static boolean isInBounds(
            final @NotNull Type type,
            final @NotNull Type @Nullable [] upper,
            final @NotNull Type @Nullable [] lower
    ) {
        if (upper != null)
            for (final var u : upper)
                if (!isAssignable(u, type))
                    return false;
        if (lower != null)
            for (final var l : lower)
                if (!isAssignable(type, l))
                    return false;
        return true;
    }

    static boolean isAssignable(final @NotNull Type dst, final @NotNull Type src) {
        if (dst == src)
            return true;

        return switch (dst) {
            case Class<?> dc -> switch (src) {
                case Class<?> sc -> dc.isAssignableFrom(sc);
                case ParameterizedType sp -> dc.isAssignableFrom((Class<?>) sp.getRawType());
                case GenericArrayType sa -> {
                    if (dc.isArray())
                        yield isAssignable(dc.getComponentType(), sa.getGenericComponentType());
                    yield false;
                }
                case TypeVariable<?> sv -> isInBounds(dst, sv.getBounds(), null);
                case WildcardType sw -> isInBounds(dst, sw.getUpperBounds(), sw.getLowerBounds());
                default -> false;
            };
            case ParameterizedType dp -> switch (src) {
                case Class<?> sc -> dp.getRawType() instanceof Class<?> toRawC && toRawC.isAssignableFrom(sc);
                case ParameterizedType sp -> {
                    if (!isAssignable(dp.getRawType(), sp.getRawType()))
                        yield false;

                    final var dArgs = dp.getActualTypeArguments();
                    final var sArgs = sp.getActualTypeArguments();

                    if (dArgs.length != sArgs.length)
                        yield false;

                    for (int i = 0; i < dArgs.length; ++i) {
                        final var dArg = dArgs[i];
                        final var sArg = sArgs[i];

                        if (dArg == sArg)
                            continue;

                        if (dArg instanceof WildcardType dwArg
                            && isInBounds(sArg, dwArg.getUpperBounds(), dwArg.getLowerBounds()))
                            continue;

                        yield false;
                    }

                    yield true;
                }
                case GenericArrayType sa -> {
                    if (dp.getRawType() instanceof Class<?> toRawC && toRawC.isArray())
                        yield isAssignable(toRawC.getComponentType(), sa.getGenericComponentType());
                    yield false;
                }
                case TypeVariable<?> sv -> isInBounds(dst, sv.getBounds(), null);
                case WildcardType sw -> isInBounds(dst, sw.getUpperBounds(), sw.getLowerBounds());
                default -> false;
            };
            case GenericArrayType da -> switch (src) {
                case Class<?> sc -> {
                    if (sc.isArray())
                        yield isAssignable(da.getGenericComponentType(), sc.getComponentType());
                    yield false;
                }
                case ParameterizedType sp -> {
                    if (sp.getRawType() instanceof Class<?> fromRawC && fromRawC.isArray())
                        yield isAssignable(da.getGenericComponentType(), fromRawC.getComponentType());
                    yield false;
                }
                case GenericArrayType sa -> isAssignable(da.getGenericComponentType(), sa.getGenericComponentType());
                case TypeVariable<?> sv -> isInBounds(dst, sv.getBounds(), null);
                case WildcardType sw -> isInBounds(dst, sw.getUpperBounds(), sw.getLowerBounds());
                default -> false;
            };
            case TypeVariable<?> dv -> switch (src) {
                case TypeVariable<?> sv -> {
                    for (final var db : dv.getBounds())
                        for (final var sb : sv.getBounds())
                            if (!isAssignable(db, sb))
                                yield false;
                    yield true;
                }
                default -> isInBounds(src, dv.getBounds(), null);
            };
            case WildcardType dw -> switch (src) {
                case WildcardType sw -> {
                    for (final var du : dw.getUpperBounds())
                        for (final var su : sw.getUpperBounds())
                            if (!isAssignable(du, su))
                                yield false;

                    for (final var dl : dw.getLowerBounds())
                        for (final var sl : sw.getLowerBounds())
                            if (!isAssignable(sl, dl))
                                yield false;

                    yield true;
                }
                default -> isInBounds(src, dw.getUpperBounds(), dw.getLowerBounds());
            };
            default -> false;
        };
    }

    @NotNull D from(final @Nullable S source);
}
