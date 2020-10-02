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
    theme: Theme? = null,
    block: RDOMBuilder<A>.() -> Unit
): ReactElement = a(href, target, classes) {
  block()

  val myTheme = theme ?: useTheme().theme
  mergeStyle {
    color = myTheme.link
  }
  if (attrs.href.contains("://")) +" ${FontAwesome.shortcut}"
}

inline fun RBuilder.themedButton(
    formEncType: ButtonFormEncType? = null,
    formMethod: ButtonFormMethod? = null,
    type: ButtonType? = null,
    classes: String? = null,
    theme: Theme? = null,
    variants: Iterable<ButtonVariant> = setOf(),
    block: RDOMBuilder<BUTTON>.() -> Unit
): ReactElement = button(formEncType, formMethod, type, classes) {
  block()

  val myTheme = theme ?: useTheme().theme
  mergeStyle {
    backgroundColor = myTheme.surface2dp
    variants.forEach { (it.buildStyles)(myTheme) }
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

inline fun RBuilder.themedSelect(classes: String? = null, theme: Theme? = null, block: RDOMBuilder<SELECT>.() -> Unit) = select(classes) {
  block()

  val myTheme = theme ?: useTheme().theme
  mergeStyle {
    backgroundColor = myTheme.background
    color = myTheme.onBackground
  }
}