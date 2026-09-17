package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.JsFunction
import dev.scriptor.ui.js.JsNode
import dev.scriptor.ui.js.JsParameter

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
}
