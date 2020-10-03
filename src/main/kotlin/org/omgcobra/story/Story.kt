package org.omgcobra.story

import kotlinext.js.*
import kotlinx.browser.document
import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.*
import org.omgcobra.story.components.*
import org.omgcobra.story.styles.*
import org.omgcobra.story.styles.themes.*
import react.*
import react.dom.*
import styled.*

interface UIState {
  var goingBack: Boolean
  var leftBarOpen: Boolean
  var leftBarPinned: Boolean
  var rightBarOpen: Boolean
  var rightBarPinned: Boolean
  var theme: Theme
}

interface StoryState {
  var passage: RClass<*>
  var last: StoryState?
}

interface StoryProps : RProps {
  var leftBarConfig: SideBarConfig?
  var rightBarConfig: SideBarConfig?
  var initialStoryState: StoryState
  var excludeFromHistory: Collection<RClass<*>>?
  var title: String?
  var onRestart: (StoryState.() -> Unit)?
}

val Story = rFunction<StoryProps>("Story") { props ->
  val initialState = props.initialStoryState.apply {
    last = null
  }
  val (pair, setState) = initialState.usePrevReducer()
  val (state, prevState) = pair
  val wide = document.documentElement?.clientWidth?.let { it > 768 } ?: false
  val hasLeftBar = props.leftBarConfig != null
  val hasRightBar = props.rightBarConfig != null
  val excluded = props.excludeFromHistory ?: setOf()
  val initialUIState: UIState = jsObject {
    goingBack = false
    leftBarOpen = hasLeftBar && wide
    leftBarPinned = hasLeftBar && wide
    rightBarOpen = hasRightBar && wide
    rightBarPinned = hasRightBar && wide
    theme = MaterialDark
  }
  val (uiState, setUIState) = initialUIState.useReducer()

  useEffect(listOf()) {
    document.title = props.title ?: "Story"
  }

  useUpdate(listOf(state.passage)) {
    document.scrollingElement?.scrollTo(0.0, 0.0)

    if (!uiState.goingBack && prevState?.passage !in excluded) {
      setState {
        last = prevState
      }
    }
    setUIState { goingBack = false }
  }

  val back: () -> Unit = {
    state.last?.let {
      setUIState { goingBack = true }
      setState { Object.assign(this, it) }
    }
  }
  val forward: () -> Unit = { TODO("Forward isn't working yet") }
  val restart = {
    setUIState { goingBack = true }
    setState {
      Object.assign(this, initialState)
      props.onRestart?.let { it() }
    }
  }

  val uiHolder = UIHolder(
      uiState = uiState,
      updateUIState = setUIState,
      back = back,
      forward = forward,
      restart = restart
  )

  UIContext.Provider(uiHolder) {
    StoryContext.Provider(Modifier(state, setState)) {
      val theme = uiState.theme
      div {
        attrs {
          id = "story"
          role = "main"
        }
        inlineStyles {
          put("--scroll-bar-color", theme.surface3dp.toString())
          backgroundColor = theme.background
          color = theme.onBackground
          borderColor = theme.border
          transition(::marginLeft, .2.s, Timing.easeIn)
          transition(::marginRight, .2.s, Timing.easeIn)
          padding(all = 2.em)
          zIndex = 10
          marginLeft = if (!hasLeftBar) 0.em else if (wide && uiState.leftBarOpen) barWidth else collapsedWidth
          marginRight = if (!hasRightBar) 0.em else if (wide && uiState.rightBarOpen) barWidth else collapsedWidth
          minHeight = 100.vh
          maxHeight = 100.vh
          overflow = Overflow.auto
        }
        child(ErrorBoundary::class) {
          VerticalLayout {
            attrs {
              spacing = 1.em
              styles = {
                textAlign = TextAlign.left
                transition(::opacity, .4.s, Timing.easeIn)
              }
            }
            state.passage {}
          }
        }
      }
      props.leftBarConfig?.let {
        child(ErrorBoundary::class) {
          SideBar {
            attrs {
              config = it
            }
          }
        }
      }
      props.rightBarConfig?.let {
        child(ErrorBoundary::class) {
          SideBar {
            attrs {
              config = it
            }
          }
        }
      }
    }
  }
}

private val StoryContext = createContext<Modifier<StoryState>>()

fun <S : Any> S.useReducer(): Pair<S, (S.() -> Unit) -> Unit> {
  val reducerFn: (S, S.() -> Unit) -> S = { s, a -> clone(s).apply(a) }
  return useReducer(reducerFn, initState = this)
}

fun <S : Any> S.usePrevReducer(): Pair<Pair<S, S?>, (S.() -> Unit) -> Unit> {
  val reducerFn: (Pair<S, S?>, S.() -> Unit) -> Pair<S, S?> = { pair, a ->
    val first = pair.first
    clone(first).apply(a) to first
  }
  return useReducer(reducerFn, initState = this to null)
}

fun <S : StoryState> useStoryState(): Modifier<S> = useContext(StoryContext).unsafeCast<Modifier<S>>()
fun useTheme(): ThemeHolder {
  val (current, modifier) = useContext(UIContext)
  return current.theme to { modifier { theme = it } }
}
fun useUI() = useContext(UIContext)

interface LayoutProps : RProps {
  var spacing: LinearDimension?
  var align: Align?
  var styles: (StyledElement.() -> Unit)?
}

val VerticalLayout = rFunction<LayoutProps>("VerticalLayout") { props ->
  styledDiv {
    css { +ComponentStyles.vertical(spacing = props.spacing ?: 0.em, align = props.align ?: Align.flexStart) }
    inlineStyles { props.styles?.let { apply(it) } }
    attrs { Object.assign(this, props) }
    props.children()
  }
}

val HorizontalLayout = rFunction<LayoutProps>("HorizontalLayout") { props ->
  styledDiv {
    css { +ComponentStyles.horizontal(spacing = props.spacing ?: 0.em, align = props.align ?: Align.flexStart) }
    inlineStyles { props.styles?.let { apply(it) } }
    attrs { Object.assign(this, props) }
    props.children()
  }
}

private val UIContext: RContext<UIHolder> = createContext()

data class Modifier<T>(var current: T, var modifier: Dispatcher<T>)

data class UIHolder(
    var uiState: UIState,
    var updateUIState: Dispatcher<UIState>,
    var back: () -> Unit,
    var forward: () -> Unit,
    var restart: () -> Unit
)
typealias ThemeHolder = Pair<Theme, (Theme) -> Unit>
val ThemeHolder.theme
  get() = first
val ThemeHolder.setTheme
  get() = second

inline fun renderToRoot(crossinline attrBuilder: StoryProps.() -> Unit) {
  injectGlobal(globalStyles)
  render(document.getElementById("root")) {
    Story {
      attrs {
        apply(attrBuilder)
      }
    }
  }
}