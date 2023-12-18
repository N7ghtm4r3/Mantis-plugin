plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.9.20"
  id("org.jetbrains.intellij") version "1.16.0"
}

group = "com.tecknobit"
version = "1.0.0"

repositories {
  mavenCentral()
  maven("https://jitpack.io")
  maven("https://repo.clojars.org")
  // TO-DO: TESTING PURPOSES ONLY REMOVE
  mavenLocal()
}

intellij {
  version.set("2023.1.5")
  type.set("IC")

  plugins.set(listOf("com.intellij.java"))
}

dependencies {

  implementation("com.tecknobit.mantis:Mantis:1.0.0")
  implementation("org.json:json:20230227")
  implementation("net.clojars.suuft:libretranslate-java:1.0.5")

}

tasks {
  withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
  }

  patchPluginXml {
    sinceBuild.set("231")
    untilBuild.set("241.*")
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin {
    token.set(System.getenv("PUBLISH_TOKEN"))
  }

}
