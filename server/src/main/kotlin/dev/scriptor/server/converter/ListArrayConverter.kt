package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class ListArrayConverter : Converter<List<*>, Array<*>> {

    context(_: Provider?)
    override fun convert(value: List<*>): Array<*> = value.toTypedArray()
}
