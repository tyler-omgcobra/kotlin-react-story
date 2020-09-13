import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.br
import react.dom.button
import react.dom.div
import react.dom.img
import styled.css
import styled.styledDiv
import styled.styledInput

external interface WelcomeProps : RProps {
    var name: String
    var nameChange: (String?) -> Unit
}

data class WelcomeState(var name: String?, var nameChange: (String?) -> Unit) : RState

class Welcome(props: WelcomeProps) : RComponent<WelcomeProps, WelcomeState>(props) {
    init {
        state = WelcomeState(props.name, props.nameChange)
    }
    override fun RBuilder.render() {
        styledDiv {
            css {
                +WelcomeStyles.textContainer
            }
            +"Hello, ${state.name}! Your name backwards is ${state.name?.reversed()}"
        }
        styledInput {
            css {
                +WelcomeStyles.textInput
            }
            attrs {
                type = InputType.text
                value = state.name ?: ""
                onChangeFunction = { event ->
                    setState(WelcomeState(name = (event.target as HTMLInputElement).value, nameChange =  state.nameChange))
                }
            }
        }
        div {
            img(src = "https://placekitten.com/408/287") {}
        }
        button {
            attrs {
                onClickFunction = {
                    setState(WelcomeState(name = "Steve", state.nameChange))
                }
            }
            +"buttonText"
        }
        br {}
        button {
            attrs {
                onClickFunction = {
                    state.nameChange(null)
                }
            }
            +"Go Away"
        }
    }
}
