plugins {
    kotlin("jvm")

    `java-gradle-plugin`
}

dependencies {
    implementation(project(":reflect"))

    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin-api:2.4.10")
}

gradlePlugin {
    plugins {
        create("reflect-plugin-gradle") {
            id = "dev.scriptor.reflect"
            displayName = "Reflect Plugin Gradle"
            description = "Reflect Plugin Gradle"
            implementationClass = "dev.scriptor.reflect.plugin.gradle.ReflectPluginGradle"
        }
    }
}
