package org.omgcobra.story.styles.themes

import kotlinx.css.*
import kotlinx.html.*
import org.omgcobra.story.UIContext
import org.omgcobra.story.styles.FontAwesome
import org.omgcobra.story.useUI
import react.*
import styled.*
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
    crossinline block: StyledDOMBuilder<A>.() -> Unit
) =
  UIContext.Consumer { uiHolder ->
    styledA(href, target) {
      block()

      themed(theme = uiHolder.uiState.theme, colorProp = Theme::link)
      try {
        if (attrs.href.contains("://")) +" ${FontAwesome.shortcut}"
      } catch (e: Throwable) {
      }
    }
  }

fun <T : Tag> StyledDOMBuilder<T>.themed(
    vararg variants: Variant,
    theme: Theme,
    backgroundColorProp: KProperty1<Theme, Color> = Theme::background,
    colorProp: KProperty1<Theme, Color> = Theme::onBackground
) {
  css {
    backgroundColor = backgroundColorProp.get(theme)
    color = colorProp.get(theme)
    variants.forEach { (it.buildStyles)(theme) }
  }
}

interface ThemedButtonProps : RProps {
  var formEncType: ButtonFormEncType?
  var formMethod: ButtonFormMethod?
  var type: ButtonType?
  var variants: Collection<ButtonVariant>?
  var buttonBuilder: StyledDOMBuilder<BUTTON>.() -> Unit
}

val ThemedButton: FunctionComponent<ThemedButtonProps> = functionComponent(::ThemedButton.name) { props ->
  val uiHolder = useUI()

  styledButton(formEncType = props.formEncType, formMethod = props.formMethod, type = props.type) {
    (props.buttonBuilder)()

    val variantList = props.variants ?: listOf()
    themed(
        *variantList.sorted().toTypedArray(),
        theme = uiHolder.uiState.theme,
        backgroundColorProp = Theme::surface2dp
    )
  }
}

fun RBuilder.themedButton(
    formEncType: ButtonFormEncType? = null,
    formMethod: ButtonFormMethod? = null,
    type: ButtonType? = null,
    variants: Collection<ButtonVariant>? = null,
    block: StyledDOMBuilder<BUTTON>.() -> Unit
) = child(ThemedButton) {
    attrs {
      this.formEncType = formEncType
      this.formMethod = formMethod
      this.type = type
      this.variants = variants
      buttonBuilder = block
    }
  }

interface Variant {
  val buildStyles: CssBuilder.(Theme) -> Unit
}

enum class ButtonVariant(override val buildStyles: CssBuilder.(Theme) -> Unit) : Variant {
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
