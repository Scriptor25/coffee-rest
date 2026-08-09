plugins {
    kotlin("multiplatform") version "2.4.0" apply false

    id("com.google.devtools.ksp") version "2.3.10" apply false
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
