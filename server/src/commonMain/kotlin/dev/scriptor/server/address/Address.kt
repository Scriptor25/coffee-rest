package dev.scriptor.server.address

expect fun parseAddressType(address: String): AddressType

expect fun normalizeIpv4(address: String): String

expect fun normalizeIpv6(address: String): String

expect fun normalizeName(address: String): String
