plugins {
    kotlin("js")
}

group = "sample-name"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.206-kotlin-1.5.10")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.206-kotlin-1.5.10")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.0-pre.206-kotlin-1.5.10")
    implementation(npm("@equo/comm", "^0.2.0"))
}

kotlin {
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            webpackTask {
                output.libraryTarget = "commonjs2"
            }
        }
    }
}
