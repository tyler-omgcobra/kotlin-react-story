import react.RComponent
import react.RProps
import react.RState

//typealias Story<S> = RComponent<RProps, S>

external interface StoryState<S> : RProps, RState {
    var test: String
    var setStoryState: (S.() -> Unit) -> Unit
}