package org.omgcobra.story.components

import kotlinx.html.js.onClickFunction
import org.omgcobra.story.styles.themes.ButtonVariant
import org.omgcobra.story.styles.themes.themedButton
import org.omgcobra.story.useUI
import react.*
import react.dom.attrs

interface BackProps : RProps {
  var title: String?
  var variants: Collection<ButtonVariant>?
}

val BackButton: FunctionComponent<BackProps> = functionComponent(::BackButton.name) {
  val uiHolder = useUI()
  themedButton(variants = it.variants) {
    +(it.title ?: "Back")
    attrs {
      onClickFunction = {
        uiHolder.back()
      }
    }
  }
}