package dev.scriptor.ui.js

data class JsSwitch(
    val condition: JsExpression,
    val cases: List<JsCase>,
) : JsNode {

    override fun toJsString(statement: Boolean): String =
        "switch (${condition.toJsString(false)}) {${
            cases.joinToString("", transform = JsCase::toJsString)
        }}"
}
