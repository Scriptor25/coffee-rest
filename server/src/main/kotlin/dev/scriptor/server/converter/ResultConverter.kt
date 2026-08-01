package dev.scriptor.server.converter

import dev.scriptor.server.result.Result

interface ResultConverter<in S : Any, out D : Result> : Converter<S, D>
