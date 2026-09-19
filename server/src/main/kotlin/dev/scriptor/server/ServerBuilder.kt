package dev.scriptor.server

import dev.scriptor.server.http.Server
import dev.scriptor.server.security.Authenticator
import dev.scriptor.server.security.Authorizer
import dev.scriptor.server.security.DefaultAuthorizer
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.util.logging.Logger

class ServerBuilder(
    val log: Logger,
    val provider: Provider,
) {
    var local: SocketAddress? = null
    var authenticator: Authenticator? = null
    var authorizer: Authorizer = DefaultAuthorizer

    fun bind(port: Int) {
        local = InetSocketAddress(port)
    }

    fun bind(addr: InetAddress?, port: Int) {
        local = InetSocketAddress(addr, port)
    }

    fun bind(hostname: String, port: Int) {
        local = InetSocketAddress(hostname, port)
    }

    fun build(): Server {
        return Server(
            log,
            provider,
            authenticator,
            authorizer,
            local,
        )
    }
}

fun server(
    log: Logger,
    provider: Provider = Provider(),
    block: ServerBuilder.() -> Unit,
): Server {
    return ServerBuilder(
        log,
        provider,
    ).apply(block).build()
}
