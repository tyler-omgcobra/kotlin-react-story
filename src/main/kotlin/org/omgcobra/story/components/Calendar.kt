package org.omgcobra.story.components

import kotlinx.css.*
import kotlinx.css.properties.border
import kotlinx.datetime.*
import org.omgcobra.story.*
import org.omgcobra.story.styles.ComponentStyles
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
  private val daysToSubtract = (start.dayOfWeek.ordinal - startDay.ordinal + 7) % 7
  val calStart = start + DatePeriod(days = -daysToSubtract)
  private val lastDate = start + DatePeriod(days = days)
  val calMax = days + daysToSubtract
  val weeks = (calMax + 6) / 7
  val dateRange = start..lastDate + DatePeriod(days = -1)
}

interface CalendarProps : RProps {
  var config: CalendarConfig
}

val Calendar: RClass<CalendarProps> = rFunction(::Calendar.name) { props ->
  val calRef: RMutableRef<Element?> = useRef(null)

  val width = useWidth(calRef)
  val wide = width > 600

  val config = props.config

  div {
    attrs { ref = calRef }
    CalendarConfigContext.Provider(config) {
      if (wide) {
        CalendarTable {
          thead {
            tr {
              (DayOfWeek.values() + DayOfWeek.values())
                .slice(config.startDay.ordinal until config.startDay.ordinal + minOf(7, config.calMax))
                .forEach {
                  th { +it.name.toLowerCase().capitalize().substring(0..2) }
                }
            }
          }
          tbody {
            (0 until config.weeks).forEach { week ->
              tr {
                (1..minOf(7, config.calMax)).forEach { day ->
                  CalendarDateContext.Provider(config.calStart + DatePeriod(days = (day - 1) + 7 * week)) {
                    td {
                      TableCell {
                        props.children()
                      }
                    }
                  }
                }
              }
            }
          }
        }
      } else {
        VerticalLayout {
          attrs {
            align = Align.stretch
          }
          (0 until config.days).forEach { day ->
            CalendarDateContext.Provider(config.start + DatePeriod(days = day)) {
              TableCell {
                props.children()
              }
            }
          }
        }
      }
    }
  }
}

private val CalendarConfigContext = createContext<CalendarConfig>()
val CalendarDateContext = createContext<LocalDate>()

private val CalendarTable: RClass<RProps> = rFunction(::CalendarTable.name) { props ->
  val theme = useUI().uiState.theme
  styledTable {
    css {
      width = 100.pct
      height = 100.pct
      // minHeight = 9.em * config.weeks
      "th" {
        width = (100 / 7).pct
        padding(0.5.em)
        border(1.px, BorderStyle.solid, theme.border)
      }
    }
    props.children()
  }
}

private interface TableCellProps : RProps {
  var date: LocalDate
}

private val TableCell: RClass<TableCellProps> = rFunction(::TableCell.name) { props ->
  val config = useContext(CalendarConfigContext)
  val date = useContext(CalendarDateContext)
  val theme = useUI().uiState.theme
  val inRange = date in config.dateRange
  val chosen = config.chosen == date
  val today = config.day == date
  val prefix = if (date.dayOfMonth == 1 || date == config.calStart) "${date.monthNumber}/" else ""
  val dateDisplay = "$prefix${date.dayOfMonth}"

  styledDiv {
    css {
      minHeight = 6.em
      padding(0.5.em)
      border(1.px, BorderStyle.solid, theme.border.withAlpha(0.5))
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
    }
    if (inRange) {
      click(Key.Enter, Key.Space) { config.select(date) }
    }
    div {
      inlineStyles {
        marginBottom = 0.5.em
        if (today) color = theme.highlight
      }
      +dateDisplay
    }
    props.children()
  }
}
