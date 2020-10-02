package org.omgcobra.story.styles

import kotlinx.css.*
import kotlinx.css.properties.*
import org.omgcobra.story.components.barWidth
import org.omgcobra.story.components.collapsedWidth
import styled.StyleSheet

val globalStyles: RuleSet = {
  fontFace(fontAwesome)
  html {
    fontFamily = "font-awesome, Verdana, Candara, Calibri, sans-serif"
  }
  universal {
    boxSizing = BoxSizing.borderBox
    "&::-webkit-scrollbar" {
      width = 0.4.em
      backgroundColor = Color.transparent
      "&-track" {
        backgroundColor = Color.transparent
      }
      "&-thumb" {
        borderRadius = 10.px
        put("background-color", "var(--scroll-bar-color, grey)")
      }
    }
  }
  body {
    maxHeight = 100.vh
    overflow = Overflow.hidden
  }
  "button, input, optgroup, select, textarea, a" {
    +ComponentStyles.highlight
  }
  "button, input, optgroup, select, textarea" {
    backgroundColor = Color.inherit
    color = Color.inherit
    fontStyle = FontStyle.inherit
    fontWeight = FontWeight.inherit
    fontSize = LinearDimension.inherit
    lineHeight = LineHeight.inherit
    fontFamily = "inherit"
  }
  "button, optgroup, a" {
    !disabled {
      cursor = Cursor.pointer
    }
  }
  button {
    padding(0.5.em)
    border(0.px, BorderStyle.solid, Color.transparent, 5.px)
  }
  "input, select, textarea" {
    borderWidth = 1.px
    borderStyle = BorderStyle.solid
    padding(all = .4.em)
  }
  table {
    borderCollapse = BorderCollapse.collapse
    borderSpacing = 0.em
    "td, th" {
      padding(all = 0.em)
    }
  }
  a {
    textDecoration = TextDecoration.none
    hover {
      textDecoration = TextDecoration(setOf(TextDecorationLine.underline))
    }
  }
  disabled {
    opacity = 0.5
    pointerEvents = PointerEvents.none
  }
  hr {
    display = Display.block
    height = 1.px
    border = "none"
    borderTop(1.px, BorderStyle.solid, Color.currentColor)
    margin(all = 0.em)
    alignSelf = Align.stretch
  }
}

object ComponentStyles : StyleSheet("story") {
  val sideBarBody by css {
    height = 100.pct
    padding(horizontal = collapsedWidth, vertical = 2.5.em)
    lineHeight = LineHeight("1.5")
    overflow = Overflow.auto
    children {
      !firstChild {
        marginTop = 2.em
      }
    }
    nav {
      descendants {
        padding(all = 0.em)
        margin(all = 0.em)
        borderWidth = 0.px
        borderStyle = BorderStyle.solid
        borderColor = Color.inherit
        borderRadius = 0.em
      }
      ul {
        margin(top = 1.em)
        listStyleType = ListStyleType.none
        borderWidth = 1.px
        empty {
          display = Display.none
        }
        "li" {
          margin(all = 0.em)
          display = Display.flex
          flexDirection = FlexDirection.column
          !firstChild {
            borderTopWidth = 1.px
          }
        }
      }
    }
  }

  val sideBarTray by css {
    position = Position.absolute
    left = 0.em
    right = 0.em
    display = Display.inlineFlex
    justifyContent = JustifyContent.spaceBetween

    button {
      fontSize = 1.2.em
      width = collapsedWidth
    }
  }

  val sideBar by css {
    transition(::left, duration = .2.s, timing = Timing.easeIn)
    transition(::right, duration = .2.s, timing = Timing.easeIn)
    borderWidth = 0.em
    position = Position.fixed
    zIndex = 50
    top = 0.em
    minWidth = barWidth
    maxWidth = barWidth
    overflowWrap = OverflowWrap.breakWord
    height = 100.pct
    textAlign = TextAlign.center
    borderStyle = BorderStyle.solid
  }

  val highlight by css {
    position = Position.relative
    userSelect = UserSelect.none
    focus {
      outline = Outline.none
    }
    !disabled {
      val overlay: RuleSet = {
        zIndex = 1
        position = Position.absolute
        top = 0.em
        left = 0.em
        bottom = 0.em
        right = 0.em
        backgroundColor = Color.currentColor
        borderRadius = LinearDimension.inherit
        opacity = 0
        transition(::opacity, 0.2.s)
        pointerEvents = PointerEvents.none
      }
      before(overlay)
      after {
        +overlay
        transition(::opacity, 1.s)
        transition("transform", .2.s)
        filter = "blur(8px)"
      }
      focus {
        before {
          opacity = 0.05
        }
      }
      hover {
        before {
          opacity = 0.1
        }
      }
      active {
        before {
          opacity = 0.05
          transition(duration = 0.s)
        }
        after {
          opacity = 0.15
          transition(duration = 0.s)
          transform {
            scale(0)
          }
        }
      }
    }
  }

  private fun layout(direction: FlexDirection, spacing: LinearDimension, align: Align): RuleSet = {
    display = Display.flex
    flexDirection = direction
    alignItems = align
    children {
      val marginProp = when (direction) {
        FlexDirection.row, FlexDirection.rowReverse -> CSSBuilder::marginLeft
        else                                        -> CSSBuilder::marginTop
      }
      marginProp.set(this, spacing)
      firstChild {
        marginProp.set(this, 0.em)
      }
    }
  }

  fun vertical(spacing: LinearDimension = 0.5.em, align: Align = Align.flexStart): RuleSet =
    layout(FlexDirection.column, spacing, align)

  fun horizontal(spacing: LinearDimension = 0.5.em, align: Align = Align.flexStart): RuleSet =
    layout(FlexDirection.row, spacing, align)
}

operator fun QuotedString.plus(other: QuotedString) = QuotedString(value = value + other.value)
operator fun QuotedString.times(int: Int) = (1..int).map { this }.reduce(QuotedString::plus)
operator fun Int.times(str: QuotedString) = str * this
