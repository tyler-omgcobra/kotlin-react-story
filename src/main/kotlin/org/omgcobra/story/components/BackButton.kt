package org.omgcobra.story.components

import kotlinx.html.js.onClickFunction
import org.omgcobra.story.UIContext
import org.omgcobra.story.styles.themes.ButtonVariant
import org.omgcobra.story.styles.themes.themedButton
import react.*

interface BackProps : RProps {
  var title: String?
  var variants: Set<ButtonVariant>?
}

val BackButton: RClass<BackProps> = rFunction(::BackButton.name) {
  UIContext.Consumer { uiHolder ->
    themedButton(variants = it.variants) {
      +(it.title ?: "Back")
      attrs {
        onClickFunction = {
          uiHolder.back()
        }
      }
    }
  }
}