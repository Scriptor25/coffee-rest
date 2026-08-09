plugins {
    kotlin("multiplatform")
}

kotlin {
    applyDefaultHierarchyTemplate()

    linuxX64()

    sourceSets {
        commonMain
    }
}
