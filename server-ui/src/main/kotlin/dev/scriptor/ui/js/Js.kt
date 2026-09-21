package dev.scriptor.ui.js

fun validateKey(key: String): Boolean {
    return """^[A-Za-z_$][A-Za-z0-9_$]*$""".toRegex().matches(key);
}

fun escapeString(str: String, vararg replace: Char): String = buildString {
    for (char in str) {
        if (char !in replace) {
            append(char)
            continue
        }

        append('\\')
        append(char)
    }
}
