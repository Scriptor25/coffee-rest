package dev.scriptor.server.converter

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultUnit

class UnitResultConverter : Converter<Unit, HTTPResult<*>> {

    override fun convert(value: Unit): HTTPResult<*> = HTTPResultUnit()
}
