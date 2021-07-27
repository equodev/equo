plugins {
    checkstyle
    id("com.moowork.node") version "1.3.1"
}

allprojects {
    repositories { mavenCentral() }

    apply(plugin = "checkstyle")

    checkstyle {
      toolVersion = "8.43"
    }

    tasks.withType<org.gradle.language.jvm.tasks.ProcessResources> { 
      dependsOn(":yarn_install")
    }
}

val node_version: String by project
val yarn_version: String by project

node {
  download = true
  version = node_version
  yarnVersion = yarn_version
}

tasks.named("yarn_install") {
  inputs.files(project.file("./package.json"), project.file("./yarn.lock"))
  outputs.dirs(project.file("./node_modules/"))
  outputs.files(project.file("./.git/config"))
}