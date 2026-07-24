package dev.scriptor.server.converter

class ArrayListConverter : Converter<Array<*>, List<*>> {

    override fun convert(value: Array<*>): List<*> = value.asList()
}
