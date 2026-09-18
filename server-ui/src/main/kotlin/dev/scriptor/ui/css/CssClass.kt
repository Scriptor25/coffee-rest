package dev.scriptor.ui.css

data class CssClass(
    val selector: String,
    val properties: Map<String, String?>,
    val children: List<CssNode>,
) : CssNode {

    override fun toCssString(): String = "$selector{${
        properties.entries
            .filter { it.value != null }
            .joinToString("") { "${it.key}:${it.value};" }
    }${
        children.joinToString("", transform = CssNode::toCssString)
    }}"
}
