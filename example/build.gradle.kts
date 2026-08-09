plugins {
    id("buildlogic.java-conventions")
}

dependencies {
    api(project(":coffee-rest"))
    api(libs.org.json.json)
}

description = "example"
