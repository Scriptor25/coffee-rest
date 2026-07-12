package io.scriptor

import io.scriptor.annotation.Endpoint
import io.scriptor.annotation.Resource
import io.scriptor.http.HTTPServer
import io.scriptor.loader.Loader
import io.scriptor.log.info
import io.scriptor.log.severe
import io.scriptor.log.trace
import io.scriptor.type.IConverter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

fun getEnv(key: String): String? {
    return System.getenv(key)
}

fun getEnv(key: String, value: String): String {
    val entry = System.getenv(key)
    return entry ?: value
}

fun Class<*>.normalize(): Class<*> = kotlin.javaPrimitiveType ?: kotlin.javaObjectType

fun Type.normalize(): Type = if (this is Class<*>) normalize() else this

fun main() {
    val enableTLS = getEnv("ENABLE_TLS", "0").toInt() != 0
    val port = getEnv("PORT", if (enableTLS) "8443" else "8080").toInt()
    val keystoreFilename = getEnv("KEYSTORE")
    val keystorePassphrase = getEnv("KEYSTORE_PASSPHRASE")

    HTTPServer(port, enableTLS, keystoreFilename, keystorePassphrase).use { server ->
        val loader = Loader("")
        loader
            .iterator()
            .asSequence()
            .filter { clazz -> clazz.isAnnotationPresent(Endpoint::class.java) }
            .forEach { clazz ->
                if (Arrays
                        .stream(clazz.constructors)
                        .noneMatch { constructor -> constructor.parameterCount == 0 }
                ) {
                    severe("endpoint class '%s' does not have a default constructor", clazz)
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

        loader
            .iterator()
            .asSequence()
            .filter { clazz ->
                listOf(*clazz.interfaces).contains(IConverter::class.java)
            }
            .forEach { clazz ->
                if (Arrays
                        .stream(clazz.constructors)
                        .noneMatch { constructor -> constructor.parameterCount == 0 }
                ) {
                    severe("converter class '%s' does not have a default constructor", clazz)
                    return@forEach
                }
                val instance: IConverter<*, *>
                try {
                    instance = clazz.getConstructor().newInstance() as IConverter<*, *>
                } catch (e: Exception) {
                    trace(e)
                    return@forEach
                }

                val interfaceType = clazz.genericInterfaces[Arrays.stream(clazz.interfaces)
                    .toList()
                    .indexOf(IConverter::class.java)
                ] as ParameterizedType

                val source = interfaceType.actualTypeArguments[0].normalize()
                val destination = interfaceType.actualTypeArguments[1].normalize()

                info("converter [ %s -> %s ]", source, destination)

                server.registerConverter(source, destination, instance)
            }
        server.start()
    }
}
