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
    val scanner = Scanner(packageName)
    val server = HTTPServer(log, hostname, port)

    scanner
        .filter { it.hasAnnotation<Endpoint>() }
        .forEach { clazz ->
            val instance: Any
            try {
                instance = clazz.createInstance()
            } catch (e: Exception) {
                log.trace(e)
                return@forEach
            }

            val endpoint = clazz.findAnnotation<Endpoint>()!!

            for (member in clazz.members) {
                val resource = member.findAnnotation<Resource>()
                if (resource != null) {
                    val bundle = server.registerRoute(instance, member, endpoint, resource)
                    log.config("route [ $bundle ]")
                }
            }

            server.registerInstance(instance)
        }

    scanner
        .filter { Converter::class in it.allSuperclasses }
        .forEach { clazz ->
            val instance: Converter<*, *>
            try {
                instance = clazz.createInstance() as Converter<*, *>
            } catch (e: Exception) {
                log.trace(e)
                return@forEach
            }

            val superclass = clazz
                .allSupertypes
                .find { it.classifier == Converter::class }!!

            val source = superclass.arguments[0].type!!
            val destination = superclass.arguments[1].type!!

            log.config("converter [ $source -> $destination ]")

            server.registerConverter(source, destination, instance)
        }

    return server
}
