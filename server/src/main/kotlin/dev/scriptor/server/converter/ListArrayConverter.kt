package dev.scriptor.server.converter

class ListArrayConverter : Converter<List<*>, Array<*>> {

    override fun convert(value: List<*>): Array<*> = value.toTypedArray()
}
