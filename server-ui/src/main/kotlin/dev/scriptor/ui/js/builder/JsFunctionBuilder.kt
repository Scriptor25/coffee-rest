package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.JsFunction
import dev.scriptor.ui.js.JsParameter
import dev.scriptor.ui.js.JsSymbol

class JsFunctionBuilder(
    val async: Boolean,
    val name: String?,
    val parameters: List<JsParameter>,
) : JsBuilder<JsFunction>() {

    override fun build(): JsFunction {
        return JsFunction(
            async,
            name,
            parameters,
            nodes,
        )
    }

    fun apply(block: JsFunctionBuilder.(Array<JsSymbol>) -> Unit): JsFunctionBuilder {
        block(parameters.map { JsSymbol(it.name) }.toTypedArray())
        return this
    }

    operator fun get(index: Int): JsSymbol {
        return JsSymbol(parameters[index].name)
    }
}
