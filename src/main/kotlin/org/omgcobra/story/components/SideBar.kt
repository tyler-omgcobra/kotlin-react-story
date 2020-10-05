package org.omgcobra.story.components

import kotlinext.js.*
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseOverFunction
import org.omgcobra.story.*
import org.omgcobra.story.styles.*
import org.omgcobra.story.styles.themes.*
import org.w3c.dom.*
import react.*
import react.dom.*
import styled.*

val barWidth = 17.5.em
val collapsedWidth = 2.em

data class SideBarConfig(
    val right: Boolean = false,

    val buttons: RDOMBuilder<UL>.() -> Unit = {},
    val content: RBuilder.() -> Unit = {},

    val showFullscreen: Boolean = false,
    val showSaves: Boolean = false,
    val showRestart: Boolean = false,
    val showHistory: Boolean = false,
    val saveSlots: Int = 10,

    val toggleText: String = "Toggle the UI Bar",

    val restartText: String = "Restart",
    val restartIcon: String = FontAwesome.power,
    val saveIcon: String = FontAwesome.save,

    val author: String = "",
    val title: String = "",
    val subtitle: String = "",
    val banner: String = ""
)

interface SideBarProps : RProps {
  var config: SideBarConfig
}

val SideBar: RClass<SideBarProps> = rFunction(displayName = ::SideBar.name) { props ->
  val (uiState, setUIState) = useUI()
  val theme = uiState.theme
  val config = props.config
  val pinned = if (config.right) uiState.rightBarPinned else uiState.leftBarPinned
  val setPinned: (Boolean) -> Unit = { setUIState { if (config.right) rightBarPinned = it else leftBarPinned = it } }
  val open = if (config.right) uiState.rightBarOpen else uiState.leftBarOpen
  val setOpen: (Boolean) -> Unit = { setUIState { if (config.right) rightBarOpen = it else leftBarOpen = it } }

  useEffect(listOf(pinned)) {
    setOpen(pinned)
  }

  SideBarContext.Provider(SideBarState(config, open, setOpen, pinned, setPinned)) {
    styledDiv {
      css {
        +ComponentStyles.sideBar
      }
      inlineStyles {
        backgroundColor = theme.surface
        color = theme.onSurface
        val sideProperty = if (config.right) ::right else ::left
        val offset = if (open) 0.em else collapsedWidth - barWidth
        sideProperty.set(offset)
        width = barWidth
      }
      attrs {
        onMouseOverFunction = { if (it.target !is HTMLButtonElement) setOpen(true) }
        onMouseLeaveFunction = { setOpen(pinned) }
      }
      SideBarTray {}
      SideBarBody {}
    }
  }
}

private data class SideBarState(
    var config: SideBarConfig = SideBarConfig(),
    var open: Boolean = true,
    var setOpen: (Boolean) -> Unit = {},
    var pinned: Boolean = true,
    var setPinned: (Boolean) -> Unit = {}
)

private val SideBarContext = createContext<SideBarState>()

private val SideBarTray: RClass<RProps> = rFunction(::SideBarTray.name) {
  val (config, open, setOpen, pinned, setPinned) = useContext(SideBarContext)
  val (_, _, back, forward) = useUI()
  val (storyState) = useStoryState<StoryState>()

  styledDiv {
    if (config.right) inlineStyles { flexDirection = FlexDirection.rowReverse }
    css {
      +ComponentStyles.sideBarTray
    }
    div {
      if (config.showFullscreen) {
        themedButton {
          +(if (document.fullscreen) FontAwesome.resize_down else FontAwesome.resize_up)
          attrs {
            onClickFunction = {
              if (!document.fullscreen) {
                document.documentElement?.requestFullscreen()
              } else {
                document.exitFullscreen()
              }?.then {
                setOpen(open)
              }
            }
          }
        }
      }
    }
    HorizontalLayout {
      attrs { spacing = 1.em }
      if (config.showHistory) {
        themedButton {
          +FontAwesome.arrow_left
          attrs {
            title = "Go backward within the game history"
            disabled = storyState.last == null
            onClickFunction = {
              back()
            }
          }
        }
        themedButton {
          +FontAwesome.arrow_right
          attrs {
            title = "Go forward within the game history"
            disabled = true
            onClickFunction = {
              forward()
            }
          }
        }
      }
    }
    div {
      themedButton(variants = setOf(ButtonVariant.Tertiary).filter { !pinned }) {
        +(if (config.right.xor(pinned)) FontAwesome.chevron_left else FontAwesome.chevron_right)
        attrs {
          title = config.toggleText
          onClickFunction = {
            setPinned(!pinned)
          }
        }
      }
    }
  }
}

private val SideBarBody: RClass<RProps> = rFunction(::SideBarBody.name) {
  val (state, updateState) = useContext(StoryContext)
  val (config, open) = useContext(SideBarContext)
  val uiHolder = useUI()
  val theme = uiHolder.uiState.theme
  val restart = uiHolder.restart

  styledDiv {
    css {
      +ComponentStyles.sideBarBody
    }
    inlineStyles {
      put("--scroll-bar-color", theme.surface3dp.toString())
      if (!open) {
        transition(::visibility, 0.2.s, Timing.stepEnd)
        visibility = Visibility.hidden
      }
    }
    header {
      attrs { id = "title" }
      attrs {
        role = "banner"
      }
      div {
        +config.banner
      }
      h1 {
        inlineStyles {
          margin(all = 0.em)
          fontSize = 162.5.pct
        }
        +config.title
      }
      div {
        +config.subtitle
      }
      div {
        attrs { id = "story-title-separator" }
      }
      p {
        inlineStyles {
          marginTop = 2.em
          fontWeight = FontWeight.w700
        }
        +config.author
      }
    }
    div {
      apply(config.content)
    }
    nav {
      attrs {
        role = "navigation"
      }
      inlineStyles {
        borderColor = theme.border
      }
      ul {
        apply(config.buttons)
      }
      ul {
        if (config.showSaves) {
          li {
            themedButton(variants = setOf(ButtonVariant.Tertiary)) {
              +"${config.saveIcon} Saves"
              attrs {
                onClickFunction = {
                  localStorage["save"] = state.toJSON()
                }
              }
            }
          }
          li {
            button {
              +"Load"
              attrs {
                onClickFunction = {
                  localStorage["save"]?.let {
                    val obj = it.hydrate(uiHolder)
                    uiHolder.updateUIState {
                      goingBack = true
                    }
                    updateState {
                      Object.assign(this, obj)
                    }
                  }
                }
              }
            }
          }
        }
        if (config.showRestart) {
          li {
            themedButton(variants = setOf(ButtonVariant.Tertiary)) {
              +"${config.restartIcon} ${config.restartText}"
              attrs {
                onClickFunction = {
                  it.preventDefault()
                  document.querySelector("html")?.setAttribute("data-init", "loading")
                  restart()
                  document.querySelector("html")?.removeAttribute("data-init")
                }
              }
            }
          }
        }
      }
    }
  }
}
