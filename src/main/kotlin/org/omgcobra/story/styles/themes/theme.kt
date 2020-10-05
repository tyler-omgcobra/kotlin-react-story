package org.omgcobra.story.styles.themes

import kotlinx.css.*
import kotlinx.html.*
import org.omgcobra.story.UIContext
import org.omgcobra.story.mergeStyle
import org.omgcobra.story.styles.FontAwesome
import react.*
import react.dom.*
import kotlin.reflect.KProperty1

interface Theme {
  val background: Color
  val onBackground: Color

  val surface: Color
  val onSurface: Color

  val success: Color
  val successContrast: Color

  val error: Color
  val errorContrast: Color

  val link: Color
  val border: Color

  val surface1dp: Color
  val surface2dp: Color
  val surface3dp: Color

  val highlight: Color
}

inline fun RBuilder.themedA(
    href: String? = null,
    target: String? = null,
    classes: String? = null,
    crossinline block: RDOMBuilder<A>.() -> Unit
): ReactElement =
  UIContext.Consumer { uiHolder ->
    a(href, target, classes) {
      block()

      themed(theme = uiHolder.uiState.theme, colorProp = Theme::link)
      try {
        if (attrs.href.contains("://")) +" ${FontAwesome.shortcut}"
      } catch (e: Throwable) {
      }
    }
  }

fun <T : Tag> RDOMBuilder<T>.themed(
    vararg variants: Variant,
    theme: Theme,
    backgroundColorProp: KProperty1<Theme, Color> = Theme::background,
    colorProp: KProperty1<Theme, Color> = Theme::onBackground
) {
  mergeStyle {
    backgroundColor = backgroundColorProp.get(theme)
    color = colorProp.get(theme)
    variants.forEach { (it.buildStyles)(theme) }
  }
}

inline fun RBuilder.themedButton(
    formEncType: ButtonFormEncType? = null,
    formMethod: ButtonFormMethod? = null,
    type: ButtonType? = null,
    classes: String? = null,
    variants: Collection<ButtonVariant>? = null,
    crossinline block: RDOMBuilder<BUTTON>.() -> Unit
): ReactElement =
  UIContext.Consumer { uiHolder ->
    button(formEncType, formMethod, type, classes) {
      block()

      val variantList: Collection<ButtonVariant> = variants ?: listOf()
      themed(
          *variantList.sorted().toTypedArray(),
          theme = uiHolder.uiState.theme,
          backgroundColorProp = Theme::surface2dp
      )
    }
  }

interface Variant {
  val buildStyles: StyledElement.(Theme) -> Unit
}

enum class ButtonVariant(override val buildStyles: StyledElement.(Theme) -> Unit) : Variant {
  Success({
    backgroundColor = it.success
    color = it.successContrast
  }),
  Error({
    backgroundColor = it.error
    color = it.errorContrast
  }),
  Tertiary({
    backgroundColor = Color.transparent
    color = Color.currentColor
  }),
  Box({
    borderWidth = 1.px
    borderStyle = BorderStyle.solid
    borderColor = it.border
    borderRadius = 0.em
  })
  ;
}
