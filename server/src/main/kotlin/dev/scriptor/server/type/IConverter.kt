package dev.scriptor.server.type

import java.lang.reflect.*

interface IConverter<S, D> {

    fun from(source: S): D?

    companion object {
        fun isInBounds(
            type: Type,
            upper: Array<Type>? = null,
            lower: Array<Type>? = null
        ): Boolean {
            if (upper != null) {
                for (u in upper) {
                    if (!isAssignable(u, type)) {
                        return false
                    }
                }
            }
            if (lower != null) {
                for (l in lower) {
                    if (!isAssignable(type, l)) {
                        return false
                    }
                }
            }
            return true
        }

        fun isAssignable(dst: Type, src: Type): Boolean {
            if (dst == src) {
                return true
            }

            return when (dst) {
                is Class<*> -> when (src) {
                    is Class<*> -> dst.isAssignableFrom(src)
                    is ParameterizedType -> dst.isAssignableFrom(src.rawType as Class<*>)
                    is GenericArrayType -> dst.isArray && isAssignable(dst.componentType, src.genericComponentType)
                    is TypeVariable<*> -> isInBounds(dst, src.bounds)
                    is WildcardType -> isInBounds(dst, src.upperBounds, src.lowerBounds)
                    else -> false
                }

                is ParameterizedType -> when (src) {
                    is Class<*> -> (dst.rawType as? Class<*>)?.isAssignableFrom(src) ?: false
                    is ParameterizedType -> {
                        if (!isAssignable(dst.rawType, src.rawType)) {
                            return false
                        }

                        val dArgs = dst.actualTypeArguments
                        val sArgs = src.actualTypeArguments

                        if (dArgs.size != sArgs.size) {
                            return false
                        }

                        for (i in dArgs.indices) {
                            val dArg = dArgs[i]
                            val sArg: Type = sArgs[i]

                            if (dArg === sArg) {
                                continue
                            }

                            if (dArg is WildcardType && isInBounds(sArg, dArg.upperBounds, dArg.lowerBounds)
                            ) {
                                continue
                            }

                            return false
                        }

                        true
                    }

                    is GenericArrayType -> (dst.rawType as? Class<*>)?.let {
                        it.isArray && isAssignable(
                            it.componentType,
                            src.genericComponentType
                        )
                    } ?: false

                    is TypeVariable<*> -> isInBounds(dst, src.bounds, null)
                    is WildcardType -> isInBounds(dst, src.upperBounds, src.lowerBounds)
                    else -> false
                }

                is GenericArrayType -> when (src) {
                    is Class<*> -> src.isArray && isAssignable(dst.genericComponentType, src.componentType)
                    is ParameterizedType -> (src.rawType as? Class<*>)?.let {
                        it.isArray && isAssignable(
                            dst.genericComponentType,
                            it.componentType
                        )
                    } ?: false

                    is GenericArrayType -> isAssignable(dst.genericComponentType, src.genericComponentType)
                    is TypeVariable<*> -> isInBounds(dst, src.bounds, null)
                    is WildcardType -> isInBounds(dst, src.upperBounds, src.lowerBounds)
                    else -> false
                }

                is TypeVariable<*> -> when (src) {
                    is TypeVariable<*> -> {
                        for (db in dst.bounds) {
                            for (sb in src.bounds) {
                                if (!isAssignable(db, sb)) {
                                    return false
                                }
                            }
                        }
                        true
                    }

                    else -> isInBounds(src, dst.bounds, null)
                }

                is WildcardType -> when (src) {
                    is WildcardType -> {
                        for (du in dst.upperBounds) {
                            for (su in src.upperBounds) {
                                if (!isAssignable(du, su)) {
                                    return false
                                }
                            }
                        }

                        for (dl in dst.lowerBounds) {
                            for (sl in src.lowerBounds) {
                                if (!isAssignable(sl, dl)) {
                                    return false
                                }
                            }
                        }

                        true
                    }

                    else -> isInBounds(src, dst.upperBounds, dst.lowerBounds)
                }

                else -> false
            }
        }
    }
}
