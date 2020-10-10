package org.omgcobra.story.styles.themes

import kotlinx.css.Color
import kotlinx.css.rgb

object Material : Theme {
  private const val greyBase = 0xee
  private fun grey(darken: Int = 0) = rgb(greyBase - 8 * darken, greyBase - 8 * darken, greyBase - 8 * darken)
  override val background = Color.white
  override val onBackground = Color.black

  override val surface = grey(1)
  override val surface1dp = grey(2)
  override val surface2dp = grey(3)
  override val surface3dp = grey(4)
  override val onSurface = Color.black

  override val success = rgb(0x80, 0xc8, 0x96)
  override val successContrast = Color.black

  override val error = rgb(0xc8, 0x96, 0x80)
  override val errorContrast = Color.black
  override val border = grey(6)

  override val link = Color.blue

  override val highlight = rgb(0x68, 0x75, 0x43)
}

object MaterialDark : Theme {
  private const val greyBase = 0x22
  private fun grey(lighten: Int = 0) = rgb(greyBase + 8 * lighten, greyBase + 8 * lighten, greyBase + 8 * lighten)
  override val background = grey()
  override val onBackground = Color.white

  override val surface = grey(1)
  override val surface1dp = grey(2)
  override val surface2dp = grey(3)
  override val surface3dp = grey(4)
  override val onSurface = Color.white

  override val success = rgb(0x0, 0x70, 0x20)
  override val successContrast = Color.white

  override val error = rgb(0x70, 0x20, 0x0)
  override val errorContrast = Color.white
  override val border: Color = grey(6)

  override val link = rgb(0x66, 0x88, 0xdd)

  override val highlight = Color.limeGreen.lighten(50)
}