pluginManagement {
    repositories {
        maven(url = "https://dl.equo.dev/gradle/0.6.3/")
        maven(url = "https://dl.equo.dev/bndtools/mvn/")
        gradlePluginPortal()
    }
}

buildCache {
    local {
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

plugins {
    id("com.equo") version "0.6.3"
}
