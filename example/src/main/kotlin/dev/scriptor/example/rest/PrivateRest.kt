package dev.scriptor.example.rest

import dev.scriptor.server.FoundSignal
import dev.scriptor.server.NotFoundSignal
import dev.scriptor.server.ParameterList
import dev.scriptor.server.UnauthorizedSignal
import dev.scriptor.server.jvm.annotation.*
import dev.scriptor.server.request.Request
import dev.scriptor.server.result.Result
import dev.scriptor.server.result.StringResult

@RequireAuth
@Controller("/private")
class PrivateRest {

    @Public
    @Get("/login", "text/plain")
    fun getLogin(): String = "Login"

    @Get("/something")
    fun getSomething(): String = "Something"

    @RequireRole("admin")
    @Post("/something", "text/plain", "text/plain")
    fun createSomething(@Body body: String): String = "Created something: $body"

    @RequireRole("admin")
    @Delete("/something/[id]")
    fun deleteSomething(@PathParameter id: String): String = "Deleted something: $id"

    @Handle(UnauthorizedSignal::class)
    fun handleUnauthorized(request: Request, signal: UnauthorizedSignal): Result {
        return FoundSignal(ParameterList("location" to "/private/login")).generate()
    }

    @Handle(NotFoundSignal::class)
    fun handleNotFound(request: Request, signal: NotFoundSignal): Result {
        return StringResult(
            404,
            "Not Found",
            "text/plain",
            ParameterList(),
            "the requested target '${request.target}' could not be found"
        )
    }
}
