package dev.scriptor.server.jvm

import dev.scriptor.reflect.getType
import dev.scriptor.server.Parameter
import dev.scriptor.server.ParameterAnnotation
import dev.scriptor.server.ParameterKind
import dev.scriptor.server.Provider
import dev.scriptor.server.converter.Converter
import dev.scriptor.server.http.Method
import dev.scriptor.server.http.Server
import dev.scriptor.server.jvm.annotation.*
import dev.scriptor.server.jvm.scanner.Scanner
import kotlin.io.path.Path
import kotlin.reflect.*
import kotlin.reflect.KParameter.Kind.*
import kotlin.reflect.full.*

fun scan(server: Server, packageName: String? = null) {
    val instances = mutableListOf<Any>()

    for (klass in Scanner(packageName)) {
        val instance = scan(server, klass) ?: continue
        instances += instance
    }

    for (instance in instances) {
        val klass = instance::class

        for (property in klass.memberProperties) {
            if (property is KMutableProperty<*>) {
                var hasValue = false
                var value: Any? = null
                for (annotation in property.annotations) {
                    when (annotation) {
                        is Inject -> {
                            val type = getType(property.returnType)
                            hasValue = type in server.provider
                            value = server.provider[type]
                            break
                        }

                        is InjectNamed -> {
                            val name = annotation.value
                            hasValue = name in server.provider
                            value = server.provider[name]
                            break
                        }
                    }
                }

                if (!hasValue && !property.returnType.isMarkedNullable) {
                    error("no value for non-optional property $property")
                }

                if (value == null && !property.returnType.isMarkedNullable) {
                    error("cannot assign null to non-nullable property $property")
                }

                property.setter.call(instance, value)
            }
        }
    }

    server.check()
}

private fun scan(server: Server, klass: KClass<*>): Any? {
    if (klass.isFinal && klass.isSubclassOf(Converter::class)) {
        val klass = klass as KClass<Converter<Any?, Any?>>

        val superclass = klass
            .allSupertypes
            .first { it.classifier == Converter::class }

        val (fst, snd) = superclass.arguments

        val src = fst.type ?: typeOf<Any?>()
        val dst = snd.type ?: typeOf<Any?>()

        val instance = createInstance(klass, server.provider)

        server.provider[getType(src) to getType(dst)] = { instance.invoke(it) }
        return instance
    }

    for (annotation in klass.annotations) {
        when (annotation) {
            is Context -> {
                val instance = createInstance(klass, server.provider)

                server.provider[klass] = instance
                return instance
            }

            is Controller -> {
                val base = annotation.path
                val instance = createInstance(klass, server.provider)

                for (function in klass.memberFunctions) {
                    scan(server, base, instance, function)
                }

                return instance
            }
        }
    }

    for (function in klass.staticFunctions) {
        scan(server, "/", null, function)
    }

    return null
}

private fun <T : Any> createInstance(klass: KClass<T>, provider: Provider): T {
    return when (val c = klass.primaryConstructor) {
        null -> klass.createInstance()

        else -> {
            val args = mutableMapOf<KParameter, Any?>()

            for (parameter in c.parameters) {
                var hasValue = false
                var value: Any? = null
                for (annotation in parameter.annotations) {
                    when (annotation) {
                        is Inject -> {
                            val type = getType(parameter.type)
                            hasValue = type in provider
                            value = provider[type]
                            break
                        }

                        is InjectNamed -> {
                            hasValue = annotation.value in provider
                            value = provider[annotation.value]
                            break
                        }
                    }
                }

                if (!hasValue) {
                    if (parameter.isOptional) {
                        continue
                    }
                    error("no value for non-optional parameter $parameter")
                }

                if (value == null) {
                    if (parameter.type.isMarkedNullable) {
                        args[parameter] = value
                        continue
                    }
                    error("cannot assign null to non-nullable parameter $parameter")
                }

                args[parameter] = value
            }

            c.callBy(args)
        }
    }
}

private fun scan(server: Server, base: String, instance: Any?, function: KFunction<*>) {
    var route: Route? = null
    for (annotation in function.annotations) {
        route = when (annotation) {
            is Route -> annotation

            is Delete -> Route(
                annotation.path,
                Method.DELETE,
                "",
                annotation.result,
            )

            is Get -> Route(
                annotation.path,
                Method.GET,
                "",
                annotation.result,
            )

            is Head -> Route(
                annotation.path,
                Method.HEAD,
                "",
                "",
            )

            is Patch -> Route(
                annotation.path,
                Method.PATCH,
                annotation.accept,
                annotation.result,
            )

            is Post -> Route(
                annotation.path,
                Method.POST,
                annotation.accept,
                annotation.result,
            )

            is Put -> Route(
                annotation.path,
                Method.PUT,
                annotation.accept,
                annotation.result,
            )

            else -> null
        } ?: continue
        break
    }

    server.register(
        instance,
        function,
        base,
        route ?: return,
    )
}

@OptIn(ExperimentalContextParameters::class)
private fun Server.register(
    instance: Any?,
    callee: KCallable<*>,
    base: String,
    route: Route,
) {
    val returns = getType(callee.returnType)
    val parameters = callee.parameters.mapNotNull {
        var annotation: ParameterAnnotation? = null
        for (a in it.annotations) {
            when (a) {
                is PathParameter -> {
                    annotation = ParameterAnnotation.Path(a.value.ifEmpty { null })
                    break
                }

                is QueryParameter -> {
                    annotation = ParameterAnnotation.Query((a.value.ifEmpty { null }))
                    break
                }

                is Header -> {
                    annotation = ParameterAnnotation.Header((a.value.ifEmpty { null }))
                    break
                }

                is Body -> {
                    annotation = ParameterAnnotation.Body
                    break
                }
            }
        }
        if (annotation == null) {
            annotation = ParameterAnnotation.None
        }

        val kind = it.kind
        if (kind == CONTEXT || kind == VALUE) {
            Parameter(
                it.index,
                it.name ?: "",
                getType(it.type),
                when (kind) {
                    CONTEXT -> ParameterKind.CONTEXT
                    VALUE -> ParameterKind.VALUE
                },
                it.isOptional,
                it.isVararg,
                annotation,
            )
        } else null
    }

    register(
        route.method,
        Path(base, route.path),
        route.accept.ifEmpty { null },
        route.result.ifEmpty { null },
        parameters,
        returns,
    ) {
        val args = mutableMapOf<KParameter, Any?>()

        for (parameter in callee.parameters) {
            when (parameter.kind) {
                INSTANCE, EXTENSION_RECEIVER -> {
                    args[parameter] = instance
                }

                else -> if (parameter.index in it)
                    args[parameter] = it[parameter.index]
            }
        }

        callee.callBy(args)
    }
}
