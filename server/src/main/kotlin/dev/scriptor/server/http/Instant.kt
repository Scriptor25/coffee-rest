package dev.scriptor.server.http

import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.time.Instant
import kotlin.time.toJavaInstant

fun Instant.toHTTP(): String =
    DateTimeFormatter.RFC_1123_DATE_TIME
        .withZone(ZoneOffset.UTC)
        .format(toJavaInstant())
