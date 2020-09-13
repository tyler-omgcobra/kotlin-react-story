import kotlinext.js.getOwnPropertyNames
import kotlinx.browser.document
import kotlinx.css.RuleSet
import react.child
import react.dom.render
import styled.injectGlobal
import styles.GlobalStyles

fun main() {
    render(document.head?.querySelector("title")) {
        +"TF Card Battle"
    }
    injectGlobal {
        with(GlobalStyles) {
            getOwnPropertyNames().forEach {
                +(asDynamic()[it] as RuleSet)
            }
        }
    }

    render(document.getElementById("root")) {
        child(Page)
    }
    document.querySelector("html")?.attributes?.removeNamedItem("data-init")
}
