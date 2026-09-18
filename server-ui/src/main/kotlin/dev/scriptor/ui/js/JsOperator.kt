package dev.scriptor.ui.js

enum class JsOperatorKind(
    val value: String,
    val unaryPrefix: Boolean = false,
    val unarySuffix: Boolean = false,
) {
    ASSIGN("="),

    ADD("+"),
    ADD_ASSIGN("+="),
    SUB("-"),
    SUB_ASSIGN("-="),
    MUL("*"),
    MUL_ASSIGN("*="),
    DIV("/"),
    DIV_ASSIGN("/="),
    REM("%"),
    REM_ASSIGN("%="),
    EXP("**"),
    EXP_ASSIGN("**="),

    SHL("<<"),
    SHL_ASSIGN("<<="),
    SHR(">>"),
    SHR_ASSIGN(">>="),
    SHR_ZERO(">>>"),
    SHR_ZERO_ASSIGN(">>>="),

    NULLISH_COALESCING("??"),
    NULLISH_COALESCING_ASSIGN("??="),

    BIT_NOT("~", unaryPrefix = true),

    BIT_AND("&"),
    BIT_AND_ASSIGN("&="),
    BIT_OR("|"),
    BIT_OR_ASSIGN("|="),
    BIT_XOR("^"),
    BIT_XOR_ASSIGN("^="),

    LOGICAL_NOT("!", unaryPrefix = true),

    LOGICAL_AND("&&"),
    LOGICAL_AND_ASSIGN("&&="),
    LOGICAL_OR("||"),
    LOGICAL_OR_ASSIGN("||="),

    COMPARE_EQUAL("=="),
    COMPARE_NOT_EQUAL("!="),
    COMPARE_STRICT_EQUAL("==="),
    COMPARE_STRICT_NOT_EQUAL("!=="),
    COMPARE_LESS_THAN("<"),
    COMPARE_LESS_THAN_OR_EQUAL("<="),
    COMPARE_GREATER_THAN(">"),
    COMPARE_GREATER_THAN_OR_EQUAL(">="),

    INC_THEN_GET("++", unaryPrefix = true),
    DEC_THEN_GET("--", unaryPrefix = true),

    GET_THEN_INC("++", unarySuffix = true),
    GET_THEN_DEC("--", unarySuffix = true),

    NEG("-", unaryPrefix = true),
    POS("+", unaryPrefix = true),
}

data class JsOperator(
    val kind: JsOperatorKind,
    val operands: List<JsExpression>,
) : JsExpression {

    override fun toJsString(): String = when {
        kind.unaryPrefix -> "${kind.value}${operands[0].toJsString()}"
        kind.unarySuffix -> "${operands[0].toJsString()}${kind.value}"
        else -> operands.joinToString(kind.value, transform = JsExpression::toJsString)
    }
}
