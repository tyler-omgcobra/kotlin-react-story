package org.omgcobra.story

import kotlinext.js.Object
import kotlinx.css.*
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyDownFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import react.*
import react.dom.*
import styled.toStyle
import kotlin.math.*

fun Double.asMoney(
    decimalPlaces: Int = 2,
    decimalSeparator: String = ".",
    thousandsSeparator: String = ",",
    currencySymbol: String = "$"
): String {
  val sign = if (this < 0) "-" else ""
  val places = 10.0.pow(decimalPlaces).toInt()

  var intVal = round(this.absoluteValue * places).toInt()
  var str = if (decimalPlaces > 0) {
    val s = decimalSeparator + "${intVal % places}".padStart(decimalPlaces, '0')
    intVal /= places
    s
  } else {
    ""
  }
  while (intVal >= 1000) {
    val s = "${intVal % 1000}".padStart(3, '0')
    str = "$thousandsSeparator$s$str"
    intVal /= 1000
  }
  str = "$intVal$str"
  return "$sign$currencySymbol$str"
}

class IdGenerator(private var value: Int = 0) {
  val next: Int
    get() = value++
}

enum class Key(private val stringValue: String? = null) {
  Enter, Space(" "), Backspace;

  override fun toString() = stringValue ?: name
}

fun <T : CommonAttributeGroupFacade> RDOMBuilder<T>.click(vararg keys: Key, block: (Event) -> Unit) {
  attrs {
    tabIndex = "0"
    onClickFunction = block
    onKeyDownFunction = { event ->
      if (event.unsafeCast<KeyboardEvent>().key in keys.map { "$it" }) {
        event.preventDefault()
        (event.target as HTMLElement).click()
      }
    }
  }
}

var CommonAttributeGroupFacade.onMouseLeaveFunction: (Event) -> Unit
  get() = throw UnsupportedOperationException("You can't read variable onMouseLeave")
  set(newValue) {
    consumer.onTagEvent(this, "onmouseleave", newValue)
  }
var CommonAttributeGroupFacade.onMouseEnterFunction: (Event) -> Unit
  get() = throw UnsupportedOperationException("You can't read variable onMouseEnter")
  set(newValue) {
    consumer.onTagEvent(this, "onmouseenter", newValue)
  }

fun <P : RProps> forwardRef(displayName: String? = null, handler: RBuilder.(P, RRef) -> Unit): RClass<P> =
  forwardRef(handler).also { it.displayName = displayName }

interface ErrorState : RState {
  var error: Throwable?
  var info: RErrorInfo?
}

class ErrorBoundary(props: RProps) : RComponent<RProps, ErrorState>(props) {
  override fun componentDidCatch(error: Throwable, info: RErrorInfo) {
    setState {
      this.error = error
      this.info = info
    }
  }

  override fun RBuilder.render() {
    if (state.error != null) {
      div {
        h1 {
          +"An error occurred"
        }
        p {
          +"${state.error}"
        }
      }
    } else {
      props.children()
    }
  }
}

typealias Dispatcher<S> = (S.() -> Unit) -> Unit

fun useUpdate(dependencies: RDependenciesList? = null, effect: () -> Unit) {
  val mounted = useRef(false)

  useEffect(dependencies) {
    if (!mounted.current) {
      mounted.current = true
    } else {
      effect()
    }
  }
}

@Suppress("UnsafeCastFromDynamic")
fun <T : Tag> RDOMBuilder<T>.mergeStyle(block: StyledElement.() -> Unit) {
  attrs {
    setProp("style", Object.assign(StyledElement().apply(block).toStyle(), get("style")))
  }
}

fun <S : StoryState> RBuilder.withState(block: RBuilder.(S, Dispatcher<S>) -> Unit) = StoryContext.Consumer { storyModifier ->
  val (state, updateState) = storyModifier.unsafeCast<Modifier<S>>()
  block(state, updateState)
}

fun <T : CommonAttributeGroupFacade, S : StoryState> RDOMBuilder<T>.updateClick(updateState: Dispatcher<S>, block: S.() -> Unit) {
  attrs {
    onClickFunction = {
      updateState(block)
    }
  }
}