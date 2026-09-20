package dev.scriptor.ui.js

fun validateKey(key: String): Boolean {
    return """^[A-Za-z_$][A-Za-z0-9_$]*$""".toRegex().matches(key);
}

fun escapeString(str: String): String = buildString {
    for (char in str) {
        if (char != '\'') {
            append(char)
            continue
        }

        append("\\'")
    }
}
