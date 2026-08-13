package dev.scriptor.reflect

@JvmInline
value class ClassId(val value: String) {
    companion object {
        val Any = ClassId("kotlin.Any")
        val Nothing = ClassId("kotlin.Nothing")
        val Unit = ClassId("kotlin.Unit")
        val Boolean = ClassId("kotlin.Boolean")
        val Number = ClassId("kotlin.Number")
        val Byte = ClassId("kotlin.Byte")
        val Short = ClassId("kotlin.Short")
        val Int = ClassId("kotlin.Int")
        val Long = ClassId("kotlin.Long")
        val Float = ClassId("kotlin.Float")
        val Double = ClassId("kotlin.Double")
        val Char = ClassId("kotlin.Char")
        val CharSequence = ClassId("kotlin.CharSequence")
        val String = ClassId("kotlin.String")
        val Enum = ClassId("kotlin.Enum")
        val Array = ClassId("kotlin.Array")
        val BooleanArray = ClassId("kotlin.BooleanArray")
        val ByteArray = ClassId("kotlin.ByteArray")
        val ShortArray = ClassId("kotlin.ShortArray")
        val IntArray = ClassId("kotlin.IntArray")
        val LongArray = ClassId("kotlin.LongArray")
        val FloatArray = ClassId("kotlin.FloatArray")
        val DoubleArray = ClassId("kotlin.DoubleArray")
        val CharArray = ClassId("kotlin.CharArray")
        val Iterator = ClassId("kotlin.Iterator")
        val Throwable = ClassId("kotlin.Throwable")
        val Comparable = ClassId("kotlin.Comparable")
        val Function = ClassId("kotlin.Function")

        val Iterable = ClassId("kotlin.collections.Iterable")
        val Collection = ClassId("kotlin.collections.Collection")
        val List = ClassId("kotlin.collections.List")
    }

    override fun toString(): String = value

    operator fun invoke(): Class = getClass(this)
}
