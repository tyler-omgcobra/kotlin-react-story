package org.omgcobra.story.components

import kotlinx.css.*
import kotlinx.css.properties.border
import kotlinx.datetime.*
import kotlinx.html.TD
import org.omgcobra.story.Key
import org.omgcobra.story.click
import org.omgcobra.story.styles.ComponentStyles
import react.*
import react.dom.*
import styled.*

data class CalendarConfig (
    var start: LocalDate,
    var day: LocalDate? = null,
    var startDay: DayOfWeek = DayOfWeek.SUNDAY,
    var days: Int = start.daysUntil(start + DatePeriod(months = 1)),
    var chosen: LocalDate? = null,
    var select: (LocalDate) -> Unit,
    var dayText: RDOMBuilder<TD>.(LocalDate) -> Unit
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
  val config = props.config
  CalendarContext.Provider(config) {
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
              TableCell {
                attrs {
                  date = config.calStart + DatePeriod(days = (day - 1) + 7 * week)
                }
              }
            }
          }
        }
      }
    }
  }
}

private val CalendarContext = createContext<CalendarConfig>()

private val CalendarTable: RClass<RProps> = rFunction(::CalendarTable.name) { props ->
  val config = useContext(CalendarContext)
  styledTable {
    css {
      width = 100.pct
      height = 100.pct
      minHeight = 9.em * config.weeks
      "td, th" {
        padding(0.5.em)
        border(1.px, BorderStyle.solid, rgb(0x33, 0x33, 0x33))
      }
      "th" {
        width = (100 / 7).pct
        borderColor = Color.grey
      }
    }
    props.children()
  }
}

private interface TableCellProps : RProps {
  var date: LocalDate
}

private val TableCell: RClass<TableCellProps> = rFunction(::TableCell.name) { props ->
  val config = useContext(CalendarContext)
  val date = props.date
  val inRange = date in config.dateRange
  val chosen = config.chosen == date
  val today = config.day == date
  val prefix = if (date.dayOfMonth == 1 || date == config.calStart) "${date.monthNumber}/" else ""
  val dateDisplay = "$prefix${date.dayOfMonth}"
  styledTd {
    css {
      verticalAlign = VerticalAlign.textTop
      if (inRange) {
        +ComponentStyles.highlight
        if (chosen) {
          specific(2) {
            before {
              opacity = 0.05
            }
          }
        }
      } else {
        opacity = 0.6
        userSelect = UserSelect.none
      }
    }
    if (inRange) {
      click(Key.Enter, Key.Space) {
        config.select(date)
      }
    }
    span {
      if (today) {
        inlineStyles {
          color = Color.limeGreen.lighten(50)
        }
      }
      +dateDisplay
    }
    br {}
    config.dayText(this, date)
  }
}
