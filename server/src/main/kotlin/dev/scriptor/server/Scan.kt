package dev.scriptor.server

import dev.scriptor.server.annotation.Context
import dev.scriptor.server.annotation.Endpoint
import dev.scriptor.server.annotation.Resource
import dev.scriptor.server.converter.Converter
import dev.scriptor.server.http.HTTPServer
import dev.scriptor.server.scanner.Scanner
import kotlin.reflect.full.*

fun scan(
    server: HTTPServer,
    packageName: String? = null,
) {
    val scanner = Scanner(packageName)

    scanner
        .filter { it != Converter::class && it.isSubclassOf(Converter::class) }
        .forEach { klass ->
            val instance: Converter<*, *>
            try {
                instance = klass.createInstance() as Converter<*, *>
            } catch (e: Exception) {
                server.log.trace(e)
                return@forEach
            }

            val superclass = klass
                .allSupertypes
                .find { it.classifier == Converter::class }!!

            val source = superclass.arguments[0].type!!
            val destination = superclass.arguments[1].type!!

            server.registerConverter(source, destination, instance)

            server.log.config("converter [ $source -> $destination ]")
        }

    scanner
        .filter { it.hasAnnotation<Context>() }
        .forEach { klass ->
            val instance: Any
            try {
                instance = klass.createInstance()
            } catch (e: Exception) {
                server.log.trace(e)
                return@forEach
            }

            val context = klass.findAnnotation<Context>()!!

            server.registerContext(context.value, instance)
        }

    scanner
        .filter { it.hasAnnotation<Endpoint>() }
        .forEach { klass ->
            val instance: Any
            try {
                instance = klass.createInstance()
            } catch (e: Exception) {
                server.log.trace(e)
                return@forEach
            }

            val endpoint = klass.findAnnotation<Endpoint>()!!

            for (member in klass.members) {
                val resource = member.findAnnotation<Resource>()
                if (resource != null) {
                    val bundle = server.registerRoute(instance, member, endpoint, resource)

                    server.log.config("route [ $bundle ]")
                }
            }

            server.registerEndpoint(instance)
        }
}
