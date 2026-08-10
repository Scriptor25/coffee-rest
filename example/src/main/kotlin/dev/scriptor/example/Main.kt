package dev.scriptor.example

import dev.scriptor.example.converter.ArrayJsonConverter
import dev.scriptor.example.converter.JsonArrayResultConverter
import dev.scriptor.example.converter.JsonObjectResultConverter
import dev.scriptor.io.Path
import dev.scriptor.server.http.Method
import dev.scriptor.server.http.Server
import dev.scriptor.server.result.StringResult
import dev.scriptor.util.Log
import org.json.JSONArray
import org.json.JSONObject

fun main() {
    val log = Log("example")

    val server = Server(log)

    val arrayJsonConverter = ArrayJsonConverter()
    val jsonArrayResultConverter = JsonArrayResultConverter()
    val jsonObjectResultConverter = JsonObjectResultConverter()

    server.provider { value: Array<*> -> arrayJsonConverter(value) }
    server.provider { value: JSONArray -> jsonArrayResultConverter(value) }
    server.provider { value: JSONObject -> jsonObjectResultConverter(value) }

    server.use { server ->
        server.register(Method.GET, Path("/"), "*/*", "text/plain") { _, _ -> StringResult(value = "Hello World!") }

        server.start()
    }
}
