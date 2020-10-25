import java.util.Properties

plugins {
  id("maven-publish")
  kotlin("js") version "1.4.10"
}
group = "org.omgcobra"
version = "0.1.2"

kotlin {
  js {
    browser {
      webpackTask {
        cssSupport.enabled = true
      }
      runTask {
        cssSupport.enabled = true
      }
    }
    useCommonJs()
  }
}
repositories {
  mavenLocal()
  mavenCentral()
  jcenter()
  maven("https://kotlin.bintray.com/kotlinx")
  maven("https://dl.bintray.com/kotlin/kotlin-js-wrappers")
}

val kotlinVersion = "1.4.10"
val reactVersion = "17.0.0"
val releaseSuffix = "pre.126"
val styledVersion = "5.2.0"

fun release(version: String) = "$version-$releaseSuffix-kotlin-$kotlinVersion"

dependencies {
  implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-datetime", version = "0.1.0")
  implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-html-js", version = "0.7.2")
  implementation(group = "org.jetbrains", name = "kotlin-react", version = release(reactVersion))
  implementation(group = "org.jetbrains", name = "kotlin-react-dom", version = release(reactVersion))
  implementation(group = "org.jetbrains", name = "kotlin-styled", version = release(styledVersion))
}

val localProperties = Properties().apply {
  load(project.file("local.properties").inputStream())
}

publishing {
  publications {
    create<MavenPublication>("default") {
      from(components["kotlin"])
      artifact(tasks["kotlinSourcesJar"])
      groupId = project.group as String
      artifactId = project.name
      version = project.version as String
      pom.withXml {
        val root = asNode()
        root.appendNode("name", "Kotlin React Story")
        root.appendNode("url", "https://github.com/tyler-omgcobra/kotlin-react-story")
      }
    }
  }
  repositories {
    maven {
      val user = "tyler-omgcobra"
      val repo = "kotlin-react-story"
      val name = "kotlin-react-story"
      url = uri("https://api.bintray.com/maven/$user/$repo/$name/;publish=0")
      credentials {
        username = localProperties["bintrayUser"] as? String ?: System.getenv("BINTRAY_USER")
        password = localProperties["bintrayApiKey"] as? String ?: System.getenv("BINTRAY_API_KEY")
      }
    }
  }
}