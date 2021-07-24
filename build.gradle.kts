plugins {
    checkstyle
    id("com.moowork.node") version "1.3.1"
}

allprojects {
    repositories { mavenCentral() }

    apply(plugin = "checkstyle")

    checkstyle { toolVersion = "8.43" }

    tasks.withType<org.gradle.language.jvm.tasks.ProcessResources> { dependsOn(":yarn_install") }
}

val node_version: String by project
val yarn_version: String by project

node.setProperty("download", true)

node.setProperty("version", node_version)

node.setProperty("yarnVersion", yarn_version)

val yarn_install = tasks.named("yarn_install").get()

yarn_install.inputs.files(project.file("./package.json"), project.file("./yarn.lock"))

yarn_install.outputs.dirs(project.file("./node_modules/"))

yarn_install.outputs.files(project.file("./.git/config"))
