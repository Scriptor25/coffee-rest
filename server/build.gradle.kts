plugins {
    kotlin("jvm")
    `java-library`
}

java {
    withSourcesJar()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
}

kotlin {
    jvmToolchain(25)
}
