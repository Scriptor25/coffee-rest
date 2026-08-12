plugins {
    kotlin("jvm")
    application

    // id("dev.scriptor.reflect")
}

application {
    mainClass = "dev.scriptor.example.MainKt"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation(project(":std"))
    implementation(project(":reflect"))
    implementation(project(":coffee-rest"))

    implementation(libs.json)
}

kotlin {
    jvmToolchain(25)
}
