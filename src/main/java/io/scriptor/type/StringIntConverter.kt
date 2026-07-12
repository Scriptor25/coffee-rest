package io.scriptor.type

class StringIntConverter : IConverter<String, Int> {

    override fun from(source: String?): Int {
        if (source == null) {
            return 0
        }
        return source.toInt()
    }
}
