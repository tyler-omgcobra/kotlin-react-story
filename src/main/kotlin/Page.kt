import kotlinext.js.getOwnPropertyNames
import kotlinx.browser.document
import kotlinx.css.RuleSet
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.Attr
import react.RProps
import react.RState
import react.dom.*
import react.functionalComponent
import styled.css
import styled.injectGlobal
import styled.styledDiv
import styled.styledInput
import styles.ComponentStyles
import styles.GlobalStyles
import styles.UIBarSide

external interface PageProps : RProps, RState {
    var title: String
    var showRightBar: Boolean
    var showFullscreen: Boolean
    var showSaves: Boolean
    var showHistory: Boolean
    var saveSlots: Int
    var onRestartClick: () -> Unit
}

val Page = functionalComponent<PageProps> { props ->
    injectGlobal {
        with(GlobalStyles) {
            getOwnPropertyNames().forEach {
                +(asDynamic()[it] as RuleSet)
            }
        }
    }
    document.title = props.title
    div {
        attrs { id = "ui-overlay" }
        attrs {
            classes += "ui-close"
        }
    }
    div {
        attrs { id = "ui-dialog" }
        attrs {
            tabIndex = "0"
            role = "dialog"
            setProp("aria-labelledby", "ui-dialog-title")
        }
        div {
            attrs { id = "ui-dialog-titlebar " }
            h1 { attrs { id = "ui-dialog-title" } }
            button {
                attrs { id = "ui-dialog-close" }
                attrs {
                    classes += "ui-close"
                    tabIndex = "0"
                    setProp("aria-label", "Close")
                }
            }
        }
        div { attrs { id = "ui-dialog-body" } }
    }
    styledDiv {
        attrs { id = "leftBar" }
        css {
            +ComponentStyles.uiBar(UIBarSide.Left)
        }
        div {
            attrs { id = "ui-bar-tray" }
            button {
                attrs { id = "ui-bar-toggle" }
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
        div {
            attrs { id = "ui-bar-body" }
            header {
                attrs { id = "title" }
                attrs {
                    role = "banner"
                }
                div { attrs { id = "story-banner" } }
                h1 { attrs { id = "story-title" } }
                div { attrs { id = "story-subtitle" } }
                div { attrs { id = "story-title-separator" } }
                p { attrs { id = "story-author" } }
            }
            div {
                attrs { id = "story-caption" }
                if (props.showFullscreen) {
                    styledInput {
                        attrs { id = "fullscreen" }
                        attrs {
                            type = InputType.checkBox
                        }
                        css {
                            +ComponentStyles.fullscreen
                        }
                    }
                    label("goFullscreen") {
                        attrs {
                            htmlFor = "fullscreen"
                            onClickFunction = {
                                document.documentElement?.requestFullscreen()
                            }
                        }
                    }
                    label("exitFullscreen") {
                        attrs {
                            htmlFor = "fullscreen"
                            onClickFunction = {
                                document.exitFullscreen()
                            }
                        }
                    }
                }
                span { attrs { id = "captionContents" } }
            }
            nav {
                attrs { id = "menu" }
                attrs {
                    role = "navigation"
                }
                ul { attrs { id = "menu-story" } }
                ul {
                    attrs { id = "menu-core" }
                    if (props.showSaves) {
                        li {
                            attrs { id = "menu-item-saves" }
                            a {
                                attrs {
                                    tabIndex = "0"
                                }
                                +"Saves"
                            }
                        }
                    }
                    li {
                        attrs { id = "menu-item-restart" }
                        a {
                            attrs {
                                tabIndex = "0"
                                onClickFunction = {
                                    document.querySelector("html")?.setAttribute("data-init", "loading")
                                    props.onRestartClick()
                                }
                            }
                            +"Restart"
                        }
                    }
                }
            }
        }
    }
    if (props.showRightBar) {
        styledDiv {
            attrs { id = "rightBar" }
            css {
                +ComponentStyles.uiBar(UIBarSide.Right)
            }
            div {
                attrs { id = "ui-bar-tray" }
                button {
                    attrs { id = "ui-bar-toggle" }
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
    }
    div {
        attrs { id = "story" }
        attrs {
            role = "main"
        }
        div {
            attrs { id = "passages" }
            props.children()
        }
    }
    document.querySelector("html")?.attributes?.removeNamedItem("data-init")
}
