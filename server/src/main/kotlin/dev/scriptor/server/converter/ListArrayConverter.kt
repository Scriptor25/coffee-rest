package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class ListArrayConverter : Converter<List<*>, Array<*>> {

    @Suppress("UNCHECKED_CAST")
    context(provider: Provider)
    override fun convert(value: List<*>): Array<*> = value.toTypedArray()
}
