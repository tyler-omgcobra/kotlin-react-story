plugins {
  kotlin("js") version "1.4.10"
  id("maven-publish")
}
group = "org.omgcobra"
version = "1.0-SNAPSHOT"

repositories {
  mavenLocal()
  mavenCentral()
  jcenter()
  maven {
    url = uri("https://kotlin.bintray.com/kotlinx")
  }
  maven {
    url = uri("https://kotlin.bintray.com/kotlin-js-wrappers")
  }
}

kotlin {
  js {
    browser {
      binaries.executable()
      webpackTask {
        cssSupport.enabled = true
      }
      runTask {
        cssSupport.enabled = true
      }
      testTask {
        useKarma {
          useChromeHeadless()
          webpackConfig.cssSupport.enabled = true
        }
      }
    }
    useCommonJs()
    mavenPublication {
      artifactId = project.name
    }
  }
}
dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
  implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.2")
  implementation("org.jetbrains:kotlin-react:16.13.1-pre.122-kotlin-1.4.10")
  implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.122-kotlin-1.4.10")
  implementation("org.jetbrains:kotlin-styled:5.2.0-pre.122-kotlin-1.4.10")
}
publishing {
  publications {
    create<MavenPublication>("kotlin-story") {
      from(components["kotlin"])
    }
  }
  repositories {
    mavenLocal()
  }
}