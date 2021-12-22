plugins {
    kotlin("jvm") version "1.5.10"
}

group = "sample-name"
version = property("version").toString()

repositories {
    mavenCentral()
    maven(url = "https://dl.equoplatform.com/framework/mvn/release")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.equo:com.equo.starter.bom:1.1.3")
    implementation("org.jetbrains.kotlin:kotlin-osgi-bundle:1.5.10")
}