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

  override val primary = rgb(0x80, 0xc8, 0x96)
  override val primaryContrast = Color.black

  override val secondary = rgb(0xc8, 0x96, 0x80)
  override val secondaryContrast = Color.black

  override val tertiary = Color.transparent
  override val tertiaryContrast = Color.black

  override val link = Color.blue
  override val border = grey(6)
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

  override val primary = rgb(0x0, 0x70, 0x20)
  override val primaryContrast = Color.white

  override val secondary = rgb(0x70, 0x20, 0x0)
  override val tertiary = Color.transparent
  override val secondaryContrast = Color.white
  override val tertiaryContrast = Color.white
  override val border: Color = grey(6)

  override val link = rgb(0x66, 0x88, 0xdd)
}