package dev.scriptor.server

import dev.scriptor.server.annotation.Endpoint
import dev.scriptor.server.annotation.Resource
import dev.scriptor.server.converter.Converter
import dev.scriptor.server.http.HTTPServer
import dev.scriptor.server.scanner.Scanner
import java.util.logging.Logger
import kotlin.reflect.full.*

fun scan(
    log: Logger,
    hostname: String,
    port: Int,
    packageName: String? = null,
): HTTPServer {
    val server = HTTPServer(log, hostname, port)

    val scanner = Scanner(packageName)

    scanner
        .filter { Converter::class in it.allSuperclasses }
        .forEach { klass ->
            val instance: Converter<*, *>
            try {
                instance = klass.createInstance() as Converter<*, *>
            } catch (e: Exception) {
                log.trace(e)
                return@forEach
            }

            val superclass = klass
                .allSupertypes
                .find { it.classifier == Converter::class }!!

            val source = superclass.arguments[0].type!!
            val destination = superclass.arguments[1].type!!

            server.registerConverter(source, destination, instance)

            log.config("converter [ $source -> $destination ]")
        }

    scanner
        .filter { it.hasAnnotation<Endpoint>() }
        .forEach { klass ->
            val instance: Any
            try {
                instance = klass.createInstance()
            } catch (e: Exception) {
                log.trace(e)
                return@forEach
            }

            val endpoint = klass.findAnnotation<Endpoint>()!!

            for (member in klass.members) {
                val resource = member.findAnnotation<Resource>()
                if (resource != null) {
                    val bundle = server.registerRoute(instance, member, endpoint, resource)

                    log.config("route [ $bundle ]")
                }
            }

            server.registerInstance(instance)
        }

    return server
}
