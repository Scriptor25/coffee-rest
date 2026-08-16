package dev.scriptor.server

import dev.scriptor.reflect.getType
import dev.scriptor.server.annotation.*
import dev.scriptor.server.converter.Converter
import dev.scriptor.server.http.Method
import dev.scriptor.server.http.Server
import dev.scriptor.server.scanner.Scanner
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.*
import kotlin.reflect.typeOf

fun scan(server: Server, packageName: String? = null) {
    for (klass in Scanner(packageName)) {
        scan(server, klass)
    }

    server.check()
}

private fun scan(server: Server, klass: KClass<*>) {
    if (klass.isFinal && klass.isSubclassOf(Converter::class)) {
        val superclass = klass
            .allSupertypes
            .first { it.classifier == Converter::class }

        val (fst, snd) = superclass.arguments

        val src = fst.type ?: typeOf<Any?>()
        val dst = snd.type ?: typeOf<Any?>()

        val instance = klass.createInstance() as Converter<Any?, Any?>

        server.provider[getType(src) to getType(dst)] = { instance.invoke(it) }
        return
    }

    for (annotation in klass.annotations) {
        when (annotation) {
            is Context -> {
                server.provider[klass] = klass.createInstance()
                return
            }

            is Controller -> {
                val base = annotation.path
                val instance = klass.createInstance()

                for (function in klass.memberFunctions) {
                    scan(server, base, instance, function)
                }

                return
            }
        }
    }

    for (function in klass.staticFunctions) {
        scan(server, "/", null, function)
    }
}

private fun scan(server: Server, base: String, instance: Any?, function: KFunction<*>) {
    var route: Route? = null
    for (annotation in function.annotations) {
        route = when (annotation) {
            is Route -> annotation

            is Connect -> Route(
                annotation.path,
                Method.CONNECT,
                annotation.accept,
                annotation.result,
            )

            is Delete -> Route(
                annotation.path,
                Method.DELETE,
                annotation.accept,
                annotation.result,
            )

            is Get -> Route(
                annotation.path,
                Method.GET,
                annotation.accept,
                annotation.result,
            )

            is Head -> Route(
                annotation.path,
                Method.HEAD,
                annotation.accept,
                annotation.result,
            )

            is Options -> Route(
                annotation.path,
                Method.OPTIONS,
                annotation.accept,
                annotation.result,
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

            is Trace -> Route(
                annotation.path,
                Method.TRACE,
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
