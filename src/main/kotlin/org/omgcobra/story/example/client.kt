package org.omgcobra.story.example

import kotlinext.js.jsObject
import kotlinx.css.*
import kotlinx.datetime.LocalDate
import kotlinx.html.js.onClickFunction
import org.omgcobra.story.*
import org.omgcobra.story.components.*
import org.omgcobra.story.styles.themes.*
import react.*
import react.dom.*
import styled.css
import styled.styledDiv

interface ExampleState : StoryState {
  var test: String
}

val LeftContent: RClass<RProps> = rFunction(::LeftContent.name) {
  val (state) = useStoryState<ExampleState>()

  p {
    +state.test
  }
}

val LeftButtons: RClass<RProps> = rFunction(::LeftButtons.name) {
  val uiHolder = useUI()
  val theme = uiHolder.uiState.theme
  val setTheme: (Theme) -> Unit = { uiHolder.updateUIState { this.theme = it } }

  li {
    themedButton(variants = setOf(ButtonVariant.Tertiary)) {
      +"Toggle Theme"
      attrs {
        onClickFunction = {
          setTheme(if (theme == MaterialDark) Material else MaterialDark)
        }
      }
    }
  }
}

val leftBar = SideBarConfig(
    content = { LeftContent {} },
    buttons = { LeftButtons {} },
    showFullscreen = true,
    showRestart = true,
    showSaves = true,
    showHistory = true
)

val RightContent: RClass<RProps> = rFunction(::RightContent.name) {
  val (state) = useStoryState<ExampleState>()
  p {
    +"Right bar"
  }
  p {
    +state.test.repeat(3)
  }
}

val rightBar = SideBarConfig(right = true, content = { RightContent {} })

val Ex: RClass<RProps> = rFunction(::Ex.name) {
  val updateState = useStoryState<ExampleState>().setState
  val uiHolder = useUI()
  val setTheme: (Theme) -> Unit = { uiHolder.updateUIState { theme = it } }

  themedButton(variants = setOf(ButtonVariant.Success)) {
    +"button"
    attrs {
      onClickFunction = {
        updateState {
          test += "a"
        }
      }
    }
  }
  themedButton(variants = setOf(ButtonVariant.Error)) {
    +"Go to second"
    attrs {
      onClickFunction = {
        updateState {
          passage = ExTwo
        }
      }
    }
  }
  themedButton(variants = setOf(ButtonVariant.Box)) {
    +"Calendar"
    updateClick(updateState) {
      passage = ExCal
    }
  }
  hr {}
  themedButton {
    +"Light"
    attrs {
      onClickFunction = {
        setTheme(Material)
      }
    }
  }
  div {
    themedButton {
      +"Dark"
      attrs {
        onClickFunction = {
          setTheme(MaterialDark)
        }
      }
    }
    themedA {
      css { marginLeft = 1.em }
      attrs {
        href = "https://google.com"
      }
      +"Google"
    }
    themedA(href = "https://yahoo.com") {
      css { marginLeft = 1.em }
      +"Yahoo"
    }
  }
  hr {}
  select {
    option { +"one" }
    option { +"two" }
    option { +"three" }
  }
  themedButton {
    +"Disabled button"
    attrs {
      disabled = true
    }
  }
}

val ExTwo: RClass<RProps> = rFunction(::ExTwo.name) {
  val (state, updateState) = useStoryState<ExampleState>()
  p {
    +"You got to the second page!"
    br {}
    +"Now let's see if you can get back"
  }
  themedButton {
    +"Bye, As"
    attrs {
      onClickFunction = {
        updateState {
          test = state.test.replace("a+$".toRegex(), "")
        }
      }
    }
  }
  themedButton(variants = setOf(ButtonVariant.Box, ButtonVariant.Tertiary)) {
    +state.test
    attrs {
      onClickFunction = {
        updateState {
          passage = Ex
        }
      }
    }
  }
  BackButton {}
}

val ExCal: RClass<RProps> = rFunction(::ExCal.name) {
  val (chosen, setChosen) = useState(LocalDate(2020, 10, 10))

  val calConfig = CalendarConfig(
      start = LocalDate(2020, 10, 1),
      day = LocalDate(2020, 10, 2),
      chosen = chosen,
      select = { setChosen(it) }
  )

  styledDiv {
    css { width = 100.pct }
    Calendar {
      attrs {
        config = calConfig
      }
    }
  }
  BackButton {}
}

val initialState: ExampleState = jsObject {
  test = "Starting"
  passage = Ex
}

fun wouldBeMain() {
  renderToRoot {
    title = "Example Story"
    leftBarConfig = leftBar
    rightBarConfig = rightBar
    initialStoryState = initialState
    components = setOf(Ex, ExTwo, ExCal)
  }
}
