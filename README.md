# Coffee Rest - Kotlin (JVM) HTTP Rest Server

```kotlin
@Endpoint("/")
class HelloWorldRest {

    @Resource("/")
    fun getHelloWorld(): String {
        return "Hello World!"
    }

    @Resource("/something", POST)
    context(body: JSONObject)
    fun postSomething(): HTTPResultUnit {
        // do something with `body` ...
        return HTTPResultUnit(201, "Created")
    }
}
```
