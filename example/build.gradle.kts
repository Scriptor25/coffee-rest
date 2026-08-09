plugins {
    kotlin("jvm")
    application
}

application {
    mainClass = "dev.scriptor.example.MainKt"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation(project(":server"))

    implementation(libs.json)
}

kotlin {
    jvmToolchain(25)
}
