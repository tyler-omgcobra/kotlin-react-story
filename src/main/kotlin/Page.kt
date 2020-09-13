import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import styled.css
import styled.styledDiv
import styles.ComponentStyles
import styles.UIBarSide
import kotlin.js.Date

val Page = functionalComponent<RProps> {
    val (n, setN) = useState<String?>(null)

    div { attrs { id = "ui-overlay" }
        attrs {
            classes += "ui-close"
        }
    }
    div { attrs { id = "ui-dialog" }
        attrs {
            tabIndex = "0"
            role = "dialog"
            setProp("aria-labelledby", "ui-dialog-title")
        }
        div { attrs { id = "ui-dialog-titlebar "}
            h1 { attrs { id = "ui-dialog-title" } }
            button { attrs { id = "ui-dialog-close" }
                attrs {
                    classes += "ui-close"
                    tabIndex = "0"
                    setProp("aria-label", "Close")
                }
            }
        }
        div { attrs { id = "ui-dialog-body" } }
    }
    styledDiv { attrs { id = "leftBar" }
        css {
            +ComponentStyles.uiBar(UIBarSide.Left)
        }
        div { attrs { id = "ui-bar-tray" }
            button { attrs { id = "ui-bar-toggle" }
                attrs {
                    tabIndex = "0"
                    val toggleText = "Toggle the UI bar"
                    title = toggleText
                    setProp("aria-label", toggleText)
                    type = ButtonType.button
                    onClickFunction = {
                        document.querySelector("#leftBar")?.classList?.toggle("stowed")
                    }
                }
            }
        }
        div { attrs { id = "ui-bar-body" }
            header { attrs { id = "title" }
                attrs {
                    role = "banner"
                }
                div { attrs { id = "story-banner" } }
                h1 { attrs { id = "story-title" } }
                div { attrs { id = "story-subtitle" } }
                div { attrs { id = "story-title-separator" } }
                p { attrs { id = "story-author" } }
            }
            div { attrs { id = "story-caption" }
                span { attrs { id = "captionContents" } }
            }
            nav { attrs { id = "menu" }
                attrs {
                    role = "navigation"
                }
                ul { attrs { id = "menu-story" } }
                ul { attrs { id = "menu-core" }
                    fun menuItem(id: String, text: String) =
                        li { attrs { this.id = id }
                            a {
                                attrs {
                                    tabIndex = "0"
                                }
                                +text
                            }
                        }
                    menuItem("menu-item-saves", "Saves")
                    menuItem("menu-item-restart", "Restart")
                }
            }
        }
    }
    styledDiv { attrs { id = "rightBar" }
        css {
            +ComponentStyles.uiBar(UIBarSide.Right)
        }
        div { attrs { id = "ui-bar-tray" }
            button { attrs { id = "ui-bar-toggle" }
                attrs {
                    tabIndex = "0"
                    val toggleText = "Toggle the Right UI bar"
                    title = toggleText
                    setProp("aria-label", toggleText)
                    type = ButtonType.button
                    onClickFunction = {
                        document.querySelector("#rightBar")?.classList?.toggle("stowed")
                    }
                }
            }
        }
        div { attrs { id = "ui-bar-body" } }
    }
    div { attrs { id = "story" }
        attrs {
            role = "main"
        }
        div { attrs { id = "passages" }
            if (n != null) {
                child(Welcome::class) {
                    attrs {
                        name = n
                        nameChange = setN
                    }
                }
            } else {
                +Date(Date.now()).toISOString()
                br {}
                button {
                    attrs {
                        onClickFunction = {
                            setN("User")
                        }
                    }
                    +"Welcome"
                }
            }
        }
    }
}