package dev.scriptor.server.converter

import dev.scriptor.server.result.UnitResult

class UnitResultConverter : ResultConverter<Unit, UnitResult> {

    override fun convert(value: Unit) = UnitResult()
}
