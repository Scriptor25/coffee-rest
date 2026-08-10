plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    linuxX64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":type-system"))
                implementation("com.squareup.okio:okio:3.18.1")
            }
        }
    }
}
