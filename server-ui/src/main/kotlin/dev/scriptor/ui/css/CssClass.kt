package dev.scriptor.ui.css

data class CssClass(
    val selector: String,
    val properties: List<CssProperty>,
    val children: List<CssNode>,
) : CssNode {

    override fun toCssString(): String = "$selector{${
        properties.joinToString(";", transform = CssProperty::toCssString)
    }${if (properties.isNotEmpty() && children.isNotEmpty()) ";" else ""}${
        children.joinToString("", transform = CssNode::toCssString)
    }}"
}
