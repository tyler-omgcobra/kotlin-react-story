package org.omgcobra.story.styles.themes

import kotlinx.css.*
import kotlinx.html.*
import org.omgcobra.story.*
import org.omgcobra.story.styles.FontAwesome
import react.*
import react.dom.*

interface Theme {
  val background: Color
  val onBackground: Color
  val surface: Color
  val onSurface: Color
  val primary: Color
  val secondary: Color
  val tertiary: Color
  val primaryContrast: Color
  val secondaryContrast: Color
  val tertiaryContrast: Color
  val link: Color
  val border: Color
  val surface1dp: Color
  val surface2dp: Color
  val surface3dp: Color
}

inline fun RBuilder.themedA(
    href: String? = null,
    target: String? = null,
    classes: String? = null,
    crossinline block: RDOMBuilder<A>.() -> Unit
): ReactElement =
  UIContext.Consumer { uiHolder ->
    val theme = uiHolder.uiState.theme
    a(href, target, classes) {
      block()

      mergeStyle {
        color = theme.link
      }
      try {
        if (attrs.href.contains("://")) +" ${FontAwesome.shortcut}"
      } catch (e: Throwable) {

      }
    }
  }

inline fun RBuilder.themedButton(
    formEncType: ButtonFormEncType? = null,
    formMethod: ButtonFormMethod? = null,
    type: ButtonType? = null,
    classes: String? = null,
    variants: Iterable<ButtonVariant>? = null,
    crossinline block: RDOMBuilder<BUTTON>.() -> Unit
): ReactElement =
  UIContext.Consumer { uiHolder ->
    val theme = uiHolder.uiState.theme
    button(formEncType, formMethod, type, classes) {
      block()

      mergeStyle {
        backgroundColor = theme.surface2dp
        variants?.forEach { (it.buildStyles)(theme) }
      }
    }
  }

enum class ButtonVariant(val buildStyles: StyledElement.(Theme) -> Unit) {
  Primary({
    backgroundColor = it.primary
    color = it.primaryContrast
  }),
  Secondary({
    backgroundColor = it.secondary
    color = it.secondaryContrast
  }),
  Tertiary({
    backgroundColor = it.tertiary
    color = it.tertiaryContrast
  }),
  Box({
    borderWidth = 1.px
    borderStyle = BorderStyle.solid
    borderColor = it.border
    borderRadius = 0.em
  })
}

inline fun RBuilder.themedSelect(classes: String? = null, crossinline block: RDOMBuilder<SELECT>.() -> Unit) =
  UIContext.Consumer { uiHolder ->
    val theme = uiHolder.uiState.theme
    select(classes) {
      block()

      mergeStyle {
        backgroundColor = theme.background
        color = theme.onBackground
      }
    }
  }