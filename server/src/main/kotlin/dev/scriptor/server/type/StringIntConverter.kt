package dev.scriptor.server.type

class StringIntConverter : IConverter<String, Int?> {

    override fun from(source: String): Int? {
        return source.toIntOrNull()
    }
}
