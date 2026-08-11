plugins {
    kotlin("jvm")
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-compiler:2.4.10")

    implementation(project(":reflect"))
}
