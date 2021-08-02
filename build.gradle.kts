plugins {
  kotlin("js")
}
group = "org.omgcobra"
version = "0.1.4"

dependencies {
  implementation(enforcedPlatform(Versions.platform))

  implementation(Versions.css)
  implementation(Versions.datetime)
  implementation(Versions.htmlJs)
  implementation(Versions.react)
  implementation(Versions.reactDom)
  implementation(Versions.styled)
}

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
