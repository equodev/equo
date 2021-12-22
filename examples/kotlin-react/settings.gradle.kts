rootProject.name = "sample-name"
include("src:main:ui")

buildscript {
    repositories {
        gradlePluginPortal()
        maven(url = "https://dl.equoplatform.com/gradle/0.6.0")
    }

    dependencies {
        classpath("com.equo:com.equo.gradle.plugin:0.6.0")
    }
}

apply(plugin = "com.equo")