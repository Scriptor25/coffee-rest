rootProject.name = "coffee-rest-parent"

include(
    ":reflect",
    ":reflect-plugin",
    ":reflect-plugin-gradle",
    ":std",
    ":server",
    ":example",
)

project(":server").name = "coffee-rest"
