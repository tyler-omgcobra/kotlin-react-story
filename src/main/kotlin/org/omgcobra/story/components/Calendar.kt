package org.omgcobra.story.components

import kotlinx.css.*
import kotlinx.css.properties.border
import kotlinx.css.properties.borderLeft
import kotlinx.datetime.*
import org.omgcobra.story.*
import org.omgcobra.story.styles.ComponentStyles
import org.omgcobra.story.styles.FontAwesome
import org.w3c.dom.DOMRectReadOnly
import org.w3c.dom.Element
import react.*
import react.dom.*
import styled.*

data class CalendarConfig (
    var start: LocalDate,
    var day: LocalDate? = null,
    var startDay: DayOfWeek = DayOfWeek.SUNDAY,
    var days: Int = start.daysUntil(start + DatePeriod(months = 1)),
    var chosen: LocalDate? = null,
    var select: (LocalDate) -> Unit
) {
  val calStart = start.last(startDay)
  private val lastDate = start + DatePeriod(days = days)
  val calMax = calStart.daysUntil(lastDate)
  val weeks = (calMax + 6) / 7
  val dateRange = start..lastDate + DatePeriod(days = -1)
}

interface CalendarProps : RProps {
  var config: CalendarConfig
}

private val CalendarSkinny: RClass<RProps> = rFunction(::CalendarSkinny.name) { props ->
  val config = useContext(CalendarConfigContext)
  val theme = useUI().uiState.theme
  VerticalLayout {
    attrs {
      align = Align.stretch
      css = {
        border(1.px, BorderStyle.solid, theme.border.withAlpha(0.5))
        minWidth = LinearDimension.maxContent
        maxWidth = (widthStop / 2).px
      }
    }
    (0 until config.days).forEach { day ->
      CalendarDateContext.Provider(config.start + DatePeriod(days = day)) {
        CalendarDay {
          attrs {
            css = {
              !firstChild {
                borderTopWidth = 1.px
                marginTop = 0.px
              }
            }
          }
          props.children()
        }
      }
    }
  }
}

private const val widthStop = 600

val Calendar: RClass<CalendarProps> = rFunction(::Calendar.name) { props ->
  val calRef: RMutableRef<Element?> = useRef(null)
  val width = useSize(calRef, DOMRectReadOnly(0.0, 0.0, widthStop.toDouble(), 0.0)).width
  val calendar = if (width > widthStop) CalendarTable else CalendarSkinny

  div {
    attrs { ref = calRef }
    CalendarConfigContext.Provider(props.config) {
      calendar {
        props.children()
      }
    }
  }
}

private val CalendarConfigContext = createContext<CalendarConfig>()
val CalendarDateContext = createContext<LocalDate>()

private val CalendarTable: RClass<RProps> = rFunction(::CalendarTable.name) { props ->
  val config = useContext(CalendarConfigContext)
  val theme = useUI().uiState.theme
  val weeks = 0 until config.weeks
  val calWeek = week(startingAt = config.startDay, limit = config.calMax)

  HorizontalLayout {
    attrs {
      align = Align.stretch
      css = {
        border(1.px, BorderStyle.solid, theme.border)
        minWidth = widthStop.px
      }
    }
    week(startingAt = config.startDay, limit = config.calMax).forEach {
      styledDiv {
        css {
          textAlign = TextAlign.center
          fontWeight = FontWeight.w700
          flex(flexGrow = 1.0, flexShrink = 1.0, flexBasis = 0.px)
          !firstChild {
            borderLeft(1.px, BorderStyle.solid, theme.border)
          }
        }
        +it.name.toLowerCase().capitalize().substring(0..2)
      }
    }
  }
  weeks.forEach { week ->
    HorizontalLayout {
      attrs {
        align = Align.stretch
        css = {
          border(1.px, BorderStyle.solid, theme.border.withAlpha(0.5))
          borderTopWidth = 0.px
        }
      }
      calWeek.forEachIndexed { day, _ ->
        CalendarDateContext.Provider(config.calStart + DatePeriod(days = day + 7 * week)) {
          CalendarDay {
            attrs {
              css = {
                flex(flexGrow = 1.0, flexShrink = 1.0, flexBasis = 0.px)
                !firstChild {
                  borderLeftWidth = 1.px
                  marginLeft = 0.px
                }
              }
            }
            props.children()
          }
        }
      }
    }
  }
}

private interface TableCellProps : RProps {
  var date: LocalDate
  var css: RuleSet?
}

private val CalendarDay: RClass<TableCellProps> = forwardRef(::CalendarDay.name) { props, rRef ->
  val config = useContext(CalendarConfigContext)
  val date = useContext(CalendarDateContext)
  val theme = useUI().uiState.theme
  val inRange = date in config.dateRange
  val chosen = config.chosen == date
  val today = config.day == date
  val prefix = if (date.dayOfMonth == 1 || date == config.calStart) "${date.monthNumber}/" else ""
  val dateDisplay = "$prefix${date.dayOfMonth}"

  styledDiv {
    attrs {
      ref = rRef
    }
    css {
      minHeight = 6.em
      minWidth = 6.em
      padding(0.5.em)
      border(0.px, BorderStyle.solid, theme.border.withAlpha(0.5))
      if (today) {
        border(1.px, BorderStyle.solid, theme.highlight)
        margin((-1).px)
        zIndex = 1
      }
      if (chosen) {
        border(1.px, BorderStyle.solid, Color.cornflowerBlue)
        margin((-1).px)
        zIndex = 1
      }
      verticalAlign = VerticalAlign.textTop
      if (inRange) {
        +ComponentStyles.highlight
        if (chosen) {
          specific(2) { before { opacity = 0.05 } }
        }
      } else {
        opacity = 0.6
        userSelect = UserSelect.none
      }
      props.css?.let { +it }
    }
    if (inRange) {
      click(Key.Enter, Key.Space) { config.select(date) }
    }
    div {
      inlineStyles {
        position = Position.relative
        marginBottom = 0.5.em
        if (today) color = theme.highlight
      }
      +dateDisplay

      if (today) {
        span {
          inlineStyles {
            position = Position.absolute
            border(0.px, BorderStyle.solid, Color.currentColor)
            borderLeftWidth = 1.px
            borderBottomWidth = 1.px
            right = ((-.5).em)
            top = ((-.5).em)
          }
          +" ${FontAwesome.star}"
        }
      }
    }
    props.children()
  }
}
