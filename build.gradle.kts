plugins {
  checkstyle
  id("com.github.node-gradle.node") version "3.4.0"
}

allprojects {
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
  download.set(true)
  version.set(node_version)
  yarnVersion.set(yarn_version)
}

tasks.named("yarn_install") {
  inputs.files(project.file("./package.json"), project.file("./yarn.lock"))
  outputs.dirs(project.file("./node_modules/"))
  outputs.files(project.file("./.git/config"))
}
