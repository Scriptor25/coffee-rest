package dev.scriptor.server.converter

import dev.scriptor.server.Provider
import dev.scriptor.server.result.UnitResult

class UnitResultConverter : Converter<Unit, UnitResult> {

    context(_: Provider?)
    override fun convert(value: Unit) = UnitResult()
}
