package dev.scriptor.server

import dev.scriptor.server.annotation.Context
import dev.scriptor.server.annotation.Endpoint
import dev.scriptor.server.annotation.Resource
import dev.scriptor.server.converter.Converter
import dev.scriptor.server.http.Server
import dev.scriptor.server.scanner.Scanner
import kotlin.reflect.full.*

fun scan(server: Server, packageName: String? = null) {
    for (klass in Scanner(packageName)) {
        when {
            klass.isFinal && klass.isSubclassOf(Converter::class) -> {
                val superclass = klass
                    .allSupertypes
                    .find { it.classifier == Converter::class }!!

                val src = superclass.arguments[0].type!!
                val dst = superclass.arguments[1].type!!

                val instance = klass.createInstance()

                server.provider[src to dst] = instance as Converter<Any, Any>
            }

            klass.hasAnnotation<Context>() -> {
                val instance = klass.createInstance()

                server.provider += instance
            }

            klass.hasAnnotation<Endpoint>() -> {
                val endpoint = klass.findAnnotation<Endpoint>()!!.path

                val instance = klass.createInstance()

                for (callee in klass.declaredMembers) {
                    val resource = callee.findAnnotation<Resource>() ?: continue

                    server.register(instance, callee, endpoint, resource)
                }
            }
        }
    }

    server.check()
}
