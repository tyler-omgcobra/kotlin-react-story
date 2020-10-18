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
dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
  implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.2")
  implementation("org.jetbrains:kotlin-react:16.13.1-pre.123-kotlin-1.4.10")
  implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.123-kotlin-1.4.10")
  implementation("org.jetbrains:kotlin-react-router-dom:5.1.2-pre.123-kotlin-1.4.10")
  implementation("org.jetbrains:kotlin-styled:5.2.0-pre.123-kotlin-1.4.10")
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
        username = if (project.hasProperty("bintrayUser")) {
          project.property("bintrayUser") as String?
        } else {
          System.getenv("BINTRAY_USER")
        }
        password = if (project.hasProperty("bintrayApiKey")) {
          project.property("bintrayApiKey") as String?
        } else {
          System.getenv("BINTRAY_API_KEY")
        }
      }
    }
  }
}