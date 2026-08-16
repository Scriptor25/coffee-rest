# Coffee Rest - Kotlin (JVM) HTTP Rest Server

```kotlin
@Controller("/")
class HelloWorldRest {

    @Get("/")
    fun getHelloWorld(): String {
        return "Hello World!"
    }

    @Post("/something")
    context(body: JSONObject)
    fun postSomething(): HTTPResultUnit {
        // do something with `body` ...
        return HTTPResultUnit(201, "Created")
    }
}
```
