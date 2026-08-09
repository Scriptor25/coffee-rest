plugins {
    kotlin("multiplatform")

    id("com.google.devtools.ksp")
}

kotlin {
    applyDefaultHierarchyTemplate()

    linuxX64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":type-system"))
            }
        }
    }
}
