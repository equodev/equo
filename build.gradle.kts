plugins {
    checkstyle
}

allprojects {
    repositories {
        mavenCentral()
    }

    apply(plugin = "checkstyle")
    checkstyle {
        toolVersion = "8.43"
    }
}
