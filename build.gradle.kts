plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.9.20"
  id("org.jetbrains.intellij") version "1.16.0"
}

group = "com.tecknobit"
version = "1.0.2"

repositories {
  mavenCentral()
  maven("https://jitpack.io")
  maven("https://repo.clojars.org")
}

intellij {
  version.set("2023.1.5")
  type.set("IC")

  plugins.set(listOf("com.intellij.java", "org.jetbrains.kotlin"))
}

sourceSets["main"].java.srcDirs("src/main/gen")

dependencies {

  implementation("com.github.N7ghtm4r3:Mantis:1.0.0")
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
