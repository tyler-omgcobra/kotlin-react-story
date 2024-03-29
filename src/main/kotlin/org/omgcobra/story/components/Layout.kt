package org.omgcobra.story.components

import kotlinx.css.*
import kotlinx.html.DIV
import org.omgcobra.story.forwardRef
import react.*
import react.dom.attrs
import styled.css
import styled.styledDiv

val LayoutContext = createContext(Layout())

data class Layout(
    val spacing: LinearDimension? = null,
    val alignItems: Align? = null,
    val justifyContent: JustifyContent? = null
)

interface LayoutProps : RProps {
  var spacing: LinearDimension?
  var align: Align?
  var justify: JustifyContent?
  var css: RuleSet?
  var divAttrs: (DIV.() -> Unit)?
}

val VerticalLayout = forwardRef<LayoutProps>("VerticalLayout") { props, rRef ->
  layoutDiv(
      direction = FlexDirection.column,
      props = props,
      rRef = rRef,
      sizingFn = { height = 100.pct },
      spacingFn = { marginTop = it }
  )
}

fun RBuilder.layoutDiv(
    direction: FlexDirection,
    props: LayoutProps,
    rRef: Ref<*>,
    sizingFn: CssBuilder.() -> Unit,
    spacingFn: CssBuilder.(LinearDimension) -> Unit
) {
  LayoutContext.Consumer { layout ->
    styledDiv {
      css {
        display = Display.flex
        val spacing = props.spacing ?: layout.spacing
        spacing?.let { children { !firstChild { spacingFn(it) } } }
        flexDirection = direction
        alignItems = props.align ?: layout.alignItems ?: Align.flexStart
        val justify = props.justify ?: layout.justifyContent
        justify?.let { justifyContent = it }
        sizingFn()
        props.css?.let { apply(it) }
      }
      attrs {
        ref = rRef
        props.divAttrs?.let { apply(it) }
      }
      props.children()
    }
  }
}

val HorizontalLayout = forwardRef<LayoutProps>("HorizontalLayout") { props, rRef ->
  layoutDiv(
      direction = FlexDirection.row,
      props = props,
      rRef = rRef,
      sizingFn = { width = 100.pct },
      spacingFn = { marginLeft = it }
  )
}