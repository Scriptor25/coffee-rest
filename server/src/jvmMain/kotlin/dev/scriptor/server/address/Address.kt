package dev.scriptor.server.address

val REGEX_IPV4 =
    """^(?:(?:25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)\.){3}(?:25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)$""".toRegex()
val REGEX_IPV6 =
    """^((?:[0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4}|(?:[0-9A-Fa-f]{1,4}:){1,7}:|(?:[0-9A-Fa-f]{1,4}:){1,6}:[0-9A-Fa-f]{1,4}|(?:[0-9A-Fa-f]{1,4}:){1,5}(?::[0-9A-Fa-f]{1,4}){1,2}|(?:[0-9A-Fa-f]{1,4}:){1,4}(?::[0-9A-Fa-f]{1,4}){1,3}|(?:[0-9A-Fa-f]{1,4}:){1,3}(?::[0-9A-Fa-f]{1,4}){1,4}|(?:[0-9A-Fa-f]{1,4}:){1,2}(?::[0-9A-Fa-f]{1,4}){1,5}|[0-9A-Fa-f]{1,4}:(?:(?::[0-9A-Fa-f]{1,4}){1,6})|:(?:(?::[0-9A-Fa-f]{1,4}){1,7}|:))$""".toRegex()
val REGEX_NAME =
    """^(?=.{1,253}$)(?!-)(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\.)*[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?$""".toRegex()

actual fun parseAddressType(address: String): AddressType = when {
    REGEX_IPV4.matches(address) -> AddressType.IPV4
    REGEX_IPV6.matches(address) -> AddressType.IPV6
    REGEX_NAME.matches(address) -> AddressType.NAME
    else -> AddressType.INVALID
}

actual fun normalizeIpv4(address: String): String =
    address.split('.').joinToString(".") { it.toInt().toString() }

actual fun normalizeIpv6(address: String): String {
    val halves = address.lowercase().split("::", limit = 2)

    val left =
        if (halves[0].isEmpty())
            emptyList()
        else
            halves[0].split(':')
    val right =
        if (halves.size == 1 || halves[1].isEmpty())
            emptyList()
        else
            halves[1].split(':')

    val groups = mutableListOf<String>()
    groups += left

    if (halves.size == 2) {
        repeat(8 - left.size - right.size) {
            groups += "0"
        }
    }

    groups += right

    for (i in groups.indices) {
        groups[i] = groups[i]
            .trimStart('0')
            .ifEmpty { "0" }
    }

    var bestStart = -1
    var bestLength = 0

    var currentStart = -1
    var currentLength = 0

    for (i in 0 until 8) {
        if (groups[i] == "0") {
            if (currentStart == -1) {
                currentStart = i
                currentLength = 1
            } else {
                currentLength++
            }
        } else {
            if (currentLength > bestLength && currentLength >= 2) {
                bestStart = currentStart
                bestLength = currentLength
            }
            currentStart = -1
            currentLength = 0
        }
    }

    if (currentLength > bestLength && currentLength >= 2) {
        bestStart = currentStart
        bestLength = currentLength
    }

    if (bestStart == -1)
        return groups.joinToString(":")

    val sb = StringBuilder()

    var i = 0
    while (i < 8) {
        if (i == bestStart) {
            if (sb.isEmpty())
                sb.append("::")
            else
                sb.append(':')

            i += bestLength

            if (i == 8)
                break
        } else {
            if (sb.isNotEmpty() && sb.last() != ':')
                sb.append(':')

            sb.append(groups[i])
            i++
        }
    }

    return sb.toString()
}

actual fun normalizeName(address: String): String = address.lowercase()
