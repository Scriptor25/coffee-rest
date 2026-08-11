plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    linuxX64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":std"))
                implementation(project(":reflect"))
            }
        }
    }
}
