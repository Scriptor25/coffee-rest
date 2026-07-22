package dev.scriptor.server

import dev.scriptor.server.annotation.Endpoint
import dev.scriptor.server.annotation.Resource
import dev.scriptor.server.http.HTTPServer
import dev.scriptor.server.log.info
import dev.scriptor.server.log.severe
import dev.scriptor.server.log.trace
import dev.scriptor.server.scanner.Scanner
import dev.scriptor.server.type.IConverter
import dev.scriptor.server.type.normalize
import java.lang.reflect.ParameterizedType
import java.util.*

fun scan(
    hostname: String = "0.0.0.0",
    port: Int = 8080,
    packageName: String? = null,
): HTTPServer {
    val scanner = Scanner(packageName)
    val server = HTTPServer(hostname, port)

    scanner
        .filter { it.isAnnotationPresent(Endpoint::class.java) }
        .forEach { clazz ->
            if (Arrays
                    .stream(clazz.constructors)
                    .noneMatch { constructor -> constructor.parameterCount == 0 }
            ) {
                severe("endpoint class '$clazz' does not have a default constructor")
                return@forEach
            }

            val instance: Any
            try {
                instance = clazz.getConstructor().newInstance()
            } catch (e: Exception) {
                trace(e)
                return@forEach
            }

            val endpoint = clazz.getAnnotation(Endpoint::class.java)
            Arrays
                .stream(clazz.methods)
                .filter { method -> method.isAnnotationPresent(Resource::class.java) }
                .forEach { method ->
                    val resource = method.getAnnotation(Resource::class.java)
                    val bundle = server.registerRoute(instance, method, endpoint, resource)
                    info(bundle.toString())
                }
        }

    scanner
        .filter { IConverter::class.java in listOf(*it.interfaces) }
        .forEach { clazz ->
            if (Arrays
                    .stream(clazz.constructors)
                    .noneMatch { constructor -> constructor.parameterCount == 0 }
            ) {
                severe("converter class '$clazz' does not have a default constructor")
                return@forEach
            }

            val instance: IConverter<*, *>
            try {
                instance = clazz.getConstructor().newInstance() as IConverter<*, *>
            } catch (e: Exception) {
                trace(e)
                return@forEach
            }

            val interfaceType = clazz
                .genericInterfaces[Arrays.stream(clazz.interfaces)
                .toList()
                .indexOf(IConverter::class.java)
            ] as ParameterizedType

            val source = interfaceType.actualTypeArguments[0].normalize()
            val destination = interfaceType.actualTypeArguments[1].normalize()

            info("converter [ $source -> $destination ]")

            server.registerConverter(source, destination, instance)
        }

    return server
}
