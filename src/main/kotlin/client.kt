import kotlinx.browser.document
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.button
import react.dom.div
import react.dom.p
import react.dom.render
import kotlin.reflect.KClass

external interface TestState : RProps, RState {
    var test: String
    var passage: KClass<out Component<TestState, *>>
    var setStoryState: (TestState.() -> Unit) -> Unit
    var onTestChange: (String) -> Unit
    var onPassageChange: (KClass<out Component<TestState, *>>) -> Unit
}

typealias Passage = RComponent<TestState, RState>
typealias Story = RComponent<RProps, TestState>

class SecondPage(props: TestState) : Passage(props) {
    override fun RBuilder.render() {
        p {
            +"You got to the second page!"
            +"Now let's see if you can get back"
        }
        button {
            attrs {
                onClickFunction = {
                    props.setStoryState {
                        passage = FirstPage::class
                    }
                }
            }
            +props.test
        }
        button {
            attrs {
                onClickFunction = {
                    props.setStoryState {
                        test = test.replace("a", "")
                    }
                }
            }
            +"Bye, As"
        }
    }
}

class FirstPage(props: TestState) : Passage(props) {
    override fun RBuilder.render() {
        p {
            +props.test
        }
        div {
            button {
                attrs {
                    onClickFunction = {
                        props.setStoryState {
                            test = props.test + "a"
                        }
                    }
                }
                +"button"
            }
        }
        div {
            button {
                attrs {
                    onClickFunction = {
                        props.setStoryState {
                            passage = SecondPage::class
                        }
                    }
                }
                +"Go to second"
            }
        }
    }
}

class TestStory : Story() {
    init {
        state.test = "Starting"
        state.passage = FirstPage::class
    }

    override fun RBuilder.render() {
        child(state.passage) {
            attrs {
                test = state.test
                setStoryState = { setState(it) }
            }
        }
    }
}

fun main() {
    renderRoot()
}

fun renderRoot() {
    render(document.getElementById("root")) {
        child(Page) {
            attrs {
                title = "Test Story"
                showFullscreen = true
                showRightBar = false
                onRestartClick = ::renderRoot
            }
            child(TestStory::class) {}
        }
    }
}
