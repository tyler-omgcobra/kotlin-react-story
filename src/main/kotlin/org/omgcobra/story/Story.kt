package org.omgcobra.story

import kotlinext.js.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.css.*
import kotlinx.css.properties.*
import org.omgcobra.story.components.*
import org.omgcobra.story.styles.*
import org.omgcobra.story.styles.themes.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import styled.*

interface UIState {
  var goingBack: Boolean
  var hasLeftBar: Boolean
  var leftBarOpen: Boolean
  var leftBarPinned: Boolean
  var hasRightBar: Boolean
  var rightBarOpen: Boolean
  var rightBarPinned: Boolean
  var theme: Theme
  var windowSize: WindowSize
}

val UIState.wide: Boolean
  get() = window.innerWidth > 800
val UIState.leftMargin: LinearDimension
  get() = when {
    !hasLeftBar         -> 0.em
    wide && leftBarOpen -> barWidth
    else                -> collapsedWidth
  }
val UIState.rightMargin: LinearDimension
  get() = when {
    !hasRightBar         -> 0.em
    wide && rightBarOpen -> barWidth
    else                 -> collapsedWidth
  }

interface StoryState {
  var passage: RClass<*>
  var last: StoryState?
}

fun <S : StoryState> S.toJSON(): String {
  val clone = clone(this).asDynamic()
  clone.passage = passage.displayName
  clone.last = last?.toJSON()
  return JSON.stringify(clone)
}
fun String.hydrate(uiHolder: UIHolder): StoryState {
  val obj = JSON.parse<dynamic>(this)
  obj.passage = uiHolder.componentMap[obj.passage]
  obj.last = obj.last?.unsafeCast<String>()?.hydrate(uiHolder)
  return obj.unsafeCast<StoryState>()
}

interface StoryProps : RProps {
  var leftBarConfig: SideBarConfig?
  var rightBarConfig: SideBarConfig?
  var initialStoryState: StoryState
  var excludeFromHistory: Collection<RClass<*>>?
  var title: String?
  var onRestart: (StoryState.() -> Unit)?
  var components: Collection<RClass<*>>?
}

data class WindowSize(
    val width: Int,
    val height: Int
)

fun debounce(ms: Int = 100, fn: (dynamic) -> Unit): (dynamic) -> Unit {
  var timer: Int? = null
  return { args ->
    timer?.let { window.clearTimeout(it) }
    timer = window.setTimeout(handler = {
      timer = null
      fn(args)
    }, timeout = ms)
  }
}

val Story = rFunction<StoryProps>("Story") { props ->
  val initialState = props.initialStoryState.apply {
    last = null
  }
  val (pair, setState) = initialState.usePrevReducer()
  val (state, prevState) = pair
  val hasLeft = props.leftBarConfig != null
  val hasRight = props.rightBarConfig != null
  val excluded = props.excludeFromHistory ?: setOf()
  val (components) = (props.components ?: setOf()).useReducer()
  val componentMap = components.map { it.displayName to it }.toMap()
  val initialUIState: UIState = jsObject {
    goingBack = false
    hasLeftBar = hasLeft
    leftBarOpen = hasLeft
    leftBarPinned = hasLeft
    hasRightBar = hasRight
    rightBarOpen = hasRight
    rightBarPinned = hasRight
    theme = MaterialDark
    windowSize = WindowSize(width = window.innerWidth, height = window.innerHeight)
  }
  val (uiStatePair, setUIState) = initialUIState.usePrevReducer()
  val uiState = uiStatePair.first
  val storyRef = useRef<HTMLDivElement?>(null)

  useEffect(listOf()) {
    document.title = props.title ?: "Story"
  }

  useEffectWithCleanup(listOf()) {
    val resize: (Event?) -> Unit = debounce { setUIState { windowSize = WindowSize(width = window.innerWidth, height = window.innerHeight) } }.unsafeCast<(Event?) -> Unit>()
    window.addEventListener("resize", resize)
    return@useEffectWithCleanup { window.removeEventListener("resize", resize) }
  }

  useUpdate(listOf(state.passage)) {
    storyRef.current?.scrollTo(0.0, 0.0)

    if (!uiState.wide) {
      setUIState {
        leftBarOpen = leftBarPinned
        rightBarOpen = rightBarPinned
      }
    }

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
      props.onRestart?.let { it() } ?: Object.assign(this, initialState)
    }
  }

  val uiHolder = UIHolder(
      uiState = uiState,
      updateUIState = setUIState,
      back = back,
      forward = forward,
      restart = restart,
      componentMap = componentMap
  )

  UIContext.Provider(uiHolder) {
    StoryContext.Provider(Modifier(state, setState)) {
      val theme = uiState.theme
      child(ErrorBoundary::class) {
        VerticalLayout {
          attrs {
            ref = storyRef
            spacing = 1.em
            css = {
              transition(::opacity, .4.s, Timing.easeIn)
              put("--scroll-bar-color", theme.surface3dp.toString())
              backgroundColor = theme.background
              color = theme.onBackground
              borderColor = theme.border
              transition(::marginLeft, .2.s, Timing.easeIn)
              transition(::marginRight, .2.s, Timing.easeIn)
              padding(all = 2.em)
              zIndex = 10
              marginLeft = uiState.leftMargin
              marginRight = uiState.rightMargin
              minHeight = 100.vh
              maxHeight = 100.vh
              overflow = Overflow.auto
            }
          }
          state.passage {}
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
              config = it.copy(right = true)
            }
          }
        }
      }
    }
  }
}

val StoryContext = createContext<Modifier<StoryState>>()

fun <S : Any> S.useReducer(): Modifier<S> {
  val reducerFn: (S, S.() -> Unit) -> S = { s, a -> clone(s).apply(a) }
  val (current, modify) = useReducer(reducerFn, initState = this)
  return Modifier(current, modify)
}

fun <S : Any> S.usePrevReducer(): Pair<Pair<S, S?>, (S.() -> Unit) -> Unit> {
  val reducerFn: (Pair<S, S?>, S.() -> Unit) -> Pair<S, S?> = { pair, a ->
    val first = pair.first
    clone(first).apply(a) to first
  }
  return useReducer(reducerFn, initState = this to null)
}

fun <S : StoryState> useStoryState(): Modifier<S> = useContext(StoryContext).unsafeCast<Modifier<S>>()
fun useUI() = useContext(UIContext)

val UIContext: RContext<UIHolder> = createContext()

data class Modifier<T>(var state: T, var setState: Dispatcher<T>)

data class UIHolder(
    var uiState: UIState,
    var updateUIState: Dispatcher<UIState>,
    var back: () -> Unit,
    var forward: () -> Unit,
    var restart: () -> Unit,
    var componentMap: Map<String?, RClass<*>>
)

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