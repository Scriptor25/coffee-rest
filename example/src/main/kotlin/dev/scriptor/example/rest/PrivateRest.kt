package dev.scriptor.example.rest

import dev.scriptor.server.jvm.annotation.*

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
}
