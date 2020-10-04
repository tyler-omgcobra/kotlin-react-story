plugins {
  kotlin("js") version "1.4.10"
}
group = "org.omgcobra"
version = "1.0-SNAPSHOT"

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
  }
}
dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")
  implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.2")
  implementation("org.jetbrains:kotlin-react:16.13.1-pre.123-kotlin-1.4.10")
  implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.123-kotlin-1.4.10")
  implementation("org.jetbrains:kotlin-react-router-dom:5.1.2-pre.123-kotlin-1.4.10")
  implementation("org.jetbrains:kotlin-styled:5.2.0-pre.123-kotlin-1.4.10")
}
