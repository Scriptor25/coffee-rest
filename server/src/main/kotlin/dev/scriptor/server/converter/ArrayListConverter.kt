package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class ArrayListConverter : Converter<Array<*>, List<*>> {

    context(_: Provider?)
    override fun convert(value: Array<*>): List<*> = value.asList()
}
