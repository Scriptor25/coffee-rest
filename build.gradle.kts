plugins {
    kotlin("jvm") version "2.4.0" apply false
}

group = "dev.scriptor.coffee-rest"
version = "1.0.0"

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenLocal()
        mavenCentral()
    }
}
