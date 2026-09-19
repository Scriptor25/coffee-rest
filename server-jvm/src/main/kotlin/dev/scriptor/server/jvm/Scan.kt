package dev.scriptor.server.jvm

import dev.scriptor.reflect.getType
import dev.scriptor.server.*
import dev.scriptor.server.converter.Converter
import dev.scriptor.server.http.Method
import dev.scriptor.server.http.Server
import dev.scriptor.server.jvm.annotation.*
import dev.scriptor.server.jvm.scanner.Scanner
import dev.scriptor.server.request.Request
import dev.scriptor.server.result.Result
import dev.scriptor.server.security.SecurityPolicy
import dev.scriptor.server.security.SecurityRequirement
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

    finalizeInstances(server.provider, instances)

    server.check()
}

fun scan(provider: Provider, packageName: String? = null) {
    val instances = mutableListOf<Any>()

    for (klass in Scanner(packageName)) {
        val instance = scan(provider, klass) ?: continue
        instances += instance
    }

    finalizeInstances(provider, instances)
}

private fun finalizeInstances(provider: Provider, instances: List<Any>) {
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
                            hasValue = type in provider
                            value = provider[type]
                            break
                        }

                        is InjectNamed -> {
                            val name = annotation.value
                            hasValue = name in provider
                            value = provider[name]
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
}

private fun scan(provider: Provider, klass: KClass<*>): Any? {
    if (klass.isFinal && klass.isSubclassOf(Converter::class)) {
        val klass = klass as KClass<Converter<Any?, Any?>>

        val superclass = klass
            .allSupertypes
            .first { it.classifier == Converter::class }

        val (fst, snd) = superclass.arguments

        val src = fst.type ?: typeOf<Any?>()
        val dst = snd.type ?: typeOf<Any?>()

        val instance = createInstance(provider, klass)

        provider[getType(src) to getType(dst)] = { instance(it) }
        return instance
    }

    for (annotation in klass.annotations) {
        when (annotation) {
            is Context -> {
                val instance = createInstance(provider, klass)

                provider[klass] = instance
                return instance
            }
        }
    }

    return null
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

        val instance = createInstance(server.provider, klass)

        server.provider[getType(src) to getType(dst)] = { instance(it) }
        return instance
    }

    val public = klass.hasAnnotation<Public>()

    val requirements = if (public) {
        emptySet()
    } else {
        val requireAuth = klass.hasAnnotation<RequireAuth>()
        val requireRole = klass.findAnnotations<RequireRole>()

        buildSet {
            if (requireAuth) {
                add(SecurityRequirement.Authenticated)
            }

            for (requirement in requireRole) {
                for (role in requirement.value) {
                    add(SecurityRequirement.Role(role))
                }
            }
        }
    }

    for (annotation in klass.annotations) {
        when (annotation) {
            is Context -> {
                val instance = createInstance(server.provider, klass)

                server.provider[klass] = instance
                return instance
            }

            is Controller -> {
                val base = annotation.path
                val instance = createInstance(server.provider, klass)

                for (callee in klass.memberFunctions) {
                    scan(server, base, requirements, instance, callee)
                }

                return instance
            }
        }
    }

    for (callee in klass.staticFunctions) {
        scan(server, "/", setOf(), null, callee)
    }

    return null
}

private fun <T : Any> createInstance(provider: Provider, klass: KClass<T>): T {
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

private fun scan(
    server: Server,
    basePath: String,
    baseRequirements: Set<SecurityRequirement>,
    instance: Any?,
    callee: KCallable<*>,
) {
    when (val handle = callee.findAnnotation<Handle>()) {
        null -> Unit
        else -> {
            val signals = setOf(*handle.value)

            server.register(basePath, signals, instance, callee)
            return
        }
    }

    val public = callee.hasAnnotation<Public>()

    val requirements = if (public) {
        emptySet()
    } else {
        val requireAuth = callee.hasAnnotation<RequireAuth>()
        val requireRole = callee.findAnnotations<RequireRole>()

        buildSet {
            addAll(baseRequirements)

            if (requireAuth) {
                add(SecurityRequirement.Authenticated)
            }

            for (requirement in requireRole) {
                for (role in requirement.value) {
                    add(SecurityRequirement.Role(role))
                }
            }
        }
    }

    var route: Route? = null
    for (annotation in callee.annotations) {
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
        basePath,
        requirements,
        instance,
        callee,
        route ?: return,
    )
}

private fun Server.register(
    basePath: String,
    signals: Set<KClass<out Signal>>,
    instance: Any?,
    callee: KCallable<*>,
) {
    when (val classifier = callee.returnType.classifier) {
        is KClass<*> -> {
            if (!classifier.isSubclassOf(Result::class)) {
                error("invalid signal handler return type '${callee.returnType}': $classifier is not a subclass of ${Result::class}")
            }

            if (callee.returnType.isMarkedNullable) {
                error("invalid signal handler return type '${callee.returnType}': type must not be nullable")
            }
        }

        else -> error("invalid signal handler return type '${callee.returnType}'")
    }

    when (val count = callee.valueParameters.size) {
        2 -> Unit
        else -> error("invalid signal handler parameter count $count")
    }

    val ins = callee.instanceParameter

    if (ins != null) {
        if (instance == null) {
            error("invalid signal handler instance parameter value: value must not be null")
        }

        if (ins.type.classifier != instance::class) {
            error("invalid signal handler instance parameter value type: '${instance::class.starProjectedType}' is not assignable to ${ins.type}")
        }
    }

    val fst = callee.valueParameters[0]
    val snd = callee.valueParameters[1]

    when (val classifier = fst.type.classifier) {
        Request::class -> Unit
        else -> error("invalid signal handler first parameter type '${fst.type}': $classifier is not ${Request::class}")
    }

    when (val classifier = snd.type.classifier) {
        is KClass<*> -> {
            val incompatible = buildSet {
                for (signal in signals) {
                    if (!classifier.isSuperclassOf(signal)) {
                        add(signal)
                    }
                }
            }

            if (incompatible.isNotEmpty()) {
                error("invalid signal handler second parameter type '${fst.type}': $classifier is not a superclass of ${incompatible.joinToString()}")
            }
        }

        else -> error("invalid signal handler second parameter type '${fst.type}'")
    }

    for (signal in signals) {
        register(basePath, signal, object : SignalHandler<Signal> {

            override fun handle(request: Request, signal: Signal): Result {
                val args = buildMap {
                    if (ins != null) {
                        this[ins] = instance
                    }

                    this[fst] = request
                    this[snd] = signal
                }

                return callee.callBy(args) as Result
            }
        })
    }
}

@OptIn(ExperimentalContextParameters::class)
private fun Server.register(
    basePath: String,
    baseRequirements: Set<SecurityRequirement>,
    instance: Any?,
    callee: KCallable<*>,
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
        Path(basePath, route.path),
        route.accept.ifBlank { null },
        route.result.ifBlank { null },
        SecurityPolicy(baseRequirements),
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
