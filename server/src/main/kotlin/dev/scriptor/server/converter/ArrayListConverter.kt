package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class ArrayListConverter : Converter<Array<*>, List<*>> {

    context(provider: Provider)
    override fun convert(value: Array<*>): List<*> = value.asList()
}
