package styles

import kotlinx.css.*
import kotlinx.css.properties.*
import styled.StyleSheet
import kotlin.reflect.KMutableProperty1

private val icon: RuleSet = {
    fontFamily = "tme-fa-icons"
    fontStyle = FontStyle.normal
    fontWeight = FontWeight.w400
    put("font-variant", "normal")
    textTransform = TextTransform.none
    lineHeight = LineHeight("1")
    put("speak", "none")
}

object GlobalStyles {
    // normalize.css v3.0.3 | MIT License | github.com/necolas/normalize.css
    val normalize: RuleSet = {
        html {
            fontFamily = "sans-serif"
            put("-ms-text-size-adjust", "100%")
            put("-webkit-text-size-adjust", "100%")
            width = 100.pct
        }

        body {
            margin(all = 0.em)
        }

        "article, aside, details, figcaption, figure, footer, header, hgroup, main, menu, nav, section, summary" {
            display = Display.block
        }
        "audio, canvas, progress, video" {
            display = Display.inlineBlock
            verticalAlign = VerticalAlign.baseline
        }
        "audio:not([controls])" {
            display = Display.none
            height = 0.em
        }
        "[hidden], template" {
            display = Display.none
        }
        a {
            backgroundColor = Color.transparent
            active {
                outline = Outline.none
            }
            hover {
                outline = Outline.none
            }
        }
        "abbr[title]" {
            borderBottomWidth = 1.px
            borderBottomStyle = BorderStyle.dotted
        }
        "b, strong" {
            fontWeight = FontWeight.w700
        }
        dfn {
            fontStyle = FontStyle.italic
        }
        h1 {
            fontSize = 2.em
            margin(vertical = .67.em, horizontal = 0.em)
        }
        mark {
            backgroundColor = Color.yellow
            color = Color.black
        }
        small {
            fontSize = 80.pct
        }
        "sub, sup" {
            fontSize = 75.pct
            lineHeight = LineHeight("0")
            position = Position.relative
            verticalAlign = VerticalAlign.baseline
        }
        sup {
            top = (-.5).em
        }
        sub {
            bottom = (-.25).em
        }
        img {
            borderWidth = 0.em
        }
        "svg:not(:root)" {
            overflow = Overflow.hidden
        }
        figure {
            margin(vertical = 1.em, horizontal = 40.px)
        }
        hr {
            boxSizing = BoxSizing.contentBox
            height = 0.em
        }
        pre {
            overflow = Overflow.auto
        }
        "code, kbd, pre, samp" {
            fontFamily = "monospace, monospace"
            fontSize = 1.em
        }
        "button, input, optgroup, select, textarea" {
            color = Color.inherit
            fontStyle = FontStyle.inherit
            fontWeight = FontWeight.inherit
            fontSize = LinearDimension.inherit
            lineHeight = LineHeight.inherit
            fontFamily = "inherit"
        }
        button {
            overflow = Overflow.visible
        }
        "button, select" {
            textTransform = TextTransform.none
        }
        "button, :root input[type=button], input[type=reset], input[type=submit]" {
            put("-webkit-appearance", "button")
            cursor = Cursor.pointer
        }
        "button[disabled], :root input[disabled]" {
            cursor = Cursor.default
        }
        "button::-moz-focus-inner, input::-moz-focus-inner" {
            borderWidth = 0.em
            padding(all = 0.em)
        }
        input {
            lineHeight = LineHeight.normal
        }
        "input[type=checkbox], input[type=radio]" {
            boxSizing = BoxSizing.borderBox
            padding(all = 0.em)
        }
        "input[type=number]" {
            "&::-webkit-inner-spin-button, &::-webkit-outer-spin-button" {
                height = LinearDimension.auto
            }
        }
        "input[type=search]" {
            put("-webkit-appearance", "textfield")
            boxSizing = BoxSizing.contentBox
        }
        "input[type=search]" {
            "&::-webkit-search-cancel-button, &::-webkit-search-decoration" {
                put("-webkit-appearance", "none")
            }
        }
        fieldset {
            border(width = 1.px, style = BorderStyle.solid, color = Color.silver)
            margin(vertical = 0.em, horizontal = 2.px)
            padding(top = .35.em, horizontal = .625.em, bottom = .75.em)
        }
        "legend" {
            borderWidth = 0.em
            padding(all = 0.em)
        }
        textarea {
            overflow = Overflow.auto
        }
        "optgroup" {
            fontWeight = FontWeight.w700
        }
        table {
            borderCollapse = BorderCollapse.collapse
            borderSpacing = 0.em
        }
        "td, th" {
            padding(all = 0.em)
        }
    }
    val initScreen: RuleSet = {
        "#init-screen" {
            display = Display.none
            zIndex = 100000
            fontSize = 28.px
            lineHeight = LineHeight("1")
            fontFamily = "Helmet, Freesans, sans-serif"
            fontWeight = FontWeight.w700
            color = Color("#eee")
            backgroundColor = Color("#111")
            textAlign = TextAlign.center
            child("div") {
                display = Display.none
                position = Position.relative
                margin(0.em, LinearDimension.auto)
                maxWidth = 1136.px
                top = 25.pct
            }
            prefix("html[data-init]") {
                display = Display.block
            }
        }
        "html[data-init=loading]" {
            ".loading" {
                display = Display.block
            }
            "#passages, #leftBar, #rightBar" {
                display = Display.none
            }
        }
    }
    val font: RuleSet = {
        fontFace {
            fontFamily = "tme-fa-icons"
            put(
                "src",
                "url(data:application/octet-stream;base64,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) format('woff')"
            )
        }
    }
    val core: RuleSet = {
        html {
            fontSize = 16.px
            lineHeight = LineHeight("1")
            fontFamily = "Helmet, Freesans, sans-serif"
        }
        ".no-transition" {
            transition = Transitions.none
        }
        focus {
            put("outline", "thin dotted")
        }
        disabled {
            cursor = Cursor.notAllowed
        }
        body {
            color = Color("#eee")
            backgroundColor = Color("#111")
        }
        a {
            cursor = Cursor.pointer
            color = Color("#68d")
            textDecoration = TextDecoration.none
            put("-o-transition-duration", ".2s")
            put("transition-duration", ".2s")
            hover {
                color = Color("#8af")
                textDecoration = TextDecoration(setOf(TextDecorationLine.underline))
            }
            "&.link-broken" {
                color = Color("#c22")
                hover {
                    color = Color("#e44")
                }
            }
        }
        "a[disabled], span.link-disabled" {
            color = Color("#aaa")
            cursor = Cursor.notAllowed
            textDecoration = TextDecoration.none
        }
        area {
            cursor = Cursor.pointer
        }
        button {
            cursor = Cursor.pointer
            color = Color("#eee")
            backgroundColor = Color("#35a")
            border(1.px, BorderStyle.solid, Color("#57c"))
            lineHeight = LineHeight.normal
            padding(.4.em)
            put("-o-transition-duration", ".2s")
            put("transition-duration", ".2s")
            userSelect = UserSelect.none
            hover {
                backgroundColor = Color("#57c")
                borderColor = Color("#79e")
            }
            disabled {
                backgroundColor = Color("#444")
                border(1.px, BorderStyle.solid, Color("#666"))
            }
        }
        "input, select, textarea" {
            color = Color("#eee")
            backgroundColor = Color.transparent
            border(1.px, BorderStyle.solid, Color("#444"))
            padding(.4.em)
        }
        select {
            padding(.34.em, .4.em)
        }
        textarea {
            minWidth = 30.em
            resize = Resize.vertical
        }
        input {
            "&[type=text]" {
                minWidth = 18.em
            }
            "&[type=range]" {
                put("-webkit-appearance", "none")
                minHeight = 1.2.em
                focus {
                    put("outline", "0")
                    "&::-webkit-slider-runnable-track" {
                        background = "#222"
                    }
                }
                "&::-webkit-slider-runnable-track, &::-moz-range-track" {
                    background = "#222"
                    border(1.px, BorderStyle.solid, Color("#444"), 0.em)
                    cursor = Cursor.pointer
                    height = 10.px
                    width = 100.pct
                }
                "&::-webkit-slider-thumb, &::-moz-range-thumb, &::-ms-thumb" {
                    background = "#35a"
                    border(1.px, BorderStyle.solid, Color("#57c"), 0.em)
                    cursor = Cursor.pointer
                    height = 18.px
                    width = 33.px
                }
                "&::-webkit-slider-thumb" {
                    put("-webkit-appearance", "none")
                    marginTop = (-5).px
                }
                "&::-ms-thumb" {
                    height = 16.px
                }
                "&::-ms-fill-lower, &::-ms-fill-upper" {
                    background = "#222"
                    border(1.px, BorderStyle.solid, Color("#444"), 0.em)
                }
            }
        }
        "input[type=checkbox], input[type=file], input[type=radio], select" {
            cursor = Cursor.pointer
        }
        "input:not(:disabled):focus, input:not(:disabled):hover, select:not(:disabled):focus, select:not(:disabled):hover, textarea:not(:disabled):focus, textarea:not(:disabled):hover" {
            backgroundColor = Color("#333")
            borderColor = Color("#eee")
        }
        hr {
            display = Display.block
            height = 1.px
            border = "none"
            borderTop(1.px, BorderStyle.solid, Color("#eee"))
            margin(1.em, 0.em)
            padding(0.em)
        }
        "audio, canvas, progress, video" {
            maxWidth = 100.pct
            verticalAlign = VerticalAlign.middle
        }
        ".error-view" {
            backgroundColor = Color("#511")
            borderLeft(.5.em, BorderStyle.solid, Color("#c22"))
            display = Display.inlineBlock
            margin(.1.em)
            maxWidth = 100.pct
            padding(0.em, .25.em)
            position = Position.relative
            child(".error-toggle") {
                backgroundColor = Color.transparent
                border = "none"
                lineHeight = LineHeight.inherit
                left = 0.em
                padding(0.em)
                position = Position.absolute
                top = 0.em
                width = 1.75.em
                adjacentSibling(".error") {
                    marginLeft = 1.5.em
                }
                before {
                    content = QuotedString(""""\e81a"""")
                }
                enabled {
                    before {
                        content = QuotedString(""""\e818"""")
                    }
                }
            }
            child(".error") {
                display = Display.inlineBlock
                marginLeft = .25.em
                before {
                    content = QuotedString(""""\e80d\00a0\00a0"""")
                }
            }
            child(".error-source") {
                "&[hidden]" {
                    display = Display.none
                }
                "&:not([hidden])" {
                    backgroundColor = Color("rgba(0, 0, 0, .2)")
                    display = Display.block
                    margin(0.em, 0.em, .25.em)
                    overflowX = Overflow.auto
                    padding(.25.em)
                }
            }
        }
        ".highlight, .marked" {
            color = Color("#ff0")
            fontWeight = FontWeight.w700
            fontStyle = FontStyle.italic
        }
        ".nobr" {
            whiteSpace = WhiteSpace.nowrap
        }
        ".error-view > .error-toggle:before, .error-view > .error:before, [data-icon-after]:after, [data-icon-before]:before, [data-icon]:before, a.link-external:after"(
            icon
        )
        "[data-icon]:before" {
            put("content", "attr(data-icon)")
        }
        "[data-icon-before]:before" {
            put("content", """attr(data-icon-before) "\00a0"""")
        }
        "[data-icon-after]:after" {
            put("content", """"\00a0" attr(data-icon-after)""")
        }
        "a.link-external:after" {
            content = """\00a0\e80e""".quoted
        }
    }
    val coreDisplay: RuleSet = {
        "#story" {
            zIndex = 10
            margin(2.5.em)
            transition(::marginLeft, .2.s, Timing.easeIn)
            transition(::marginRight, .2.s, Timing.easeIn)
        }
        media("screen and (max-width: 1136px)") {
            "#story" {
                marginRight = 1.5.em
            }
        }
        "#passages" {
            maxWidth = 54.em
            margin(0.em, LinearDimension.auto)
        }
    }
    val corePassage: RuleSet = {
        ".passage" {
            lineHeight = LineHeight("1.75")
            textAlign = TextAlign.left
            transition(::opacity, .4.s, Timing.easeIn)
            "ol, ul" {
                marginLeft = .5.em
                paddingLeft = 1.5.em
            }
            table {
                margin(1.em, 0.em)
                borderCollapse = BorderCollapse.collapse
                fontSize = 100.pct
            }
            "caption, td, th, tr" {
                padding(3.px)
            }
        }
    }
    val coreMacro: RuleSet = {
        ".macro-append-insert, .macro-linkappend-insert, .macro-linkprepend-insert, .macro-linkreplace-insert, .macro-prepend-insert, .macro-repeat-insert, .macro-replace-insert, .macro-timed-insert" {
            transition(::opacity, .4.s, Timing.easeIn)
        }
        ".macro-append-in, .macro-linkappend-in, .macro-linkprepend-in, .macro-linkreplace-in, .macro-prepend-in, .macro-repeat-in, .macro-replace-in, .macro-timed-in" {
            opacity = 0
        }
    }
    val uiDialog: RuleSet = {
        "&[data-dialog] body" {
            overflow = Overflow.hidden
        }
        "#ui-overlay" {
            visibility = Visibility.hidden
            opacity = 0
            zIndex = 100000
            position = Position.fixed
            top = (-50).pct
            left = (-50).pct
            height = 200.pct
            width = 200.pct
            backgroundColor = Color.black

            "&.open" {
                visibility = Visibility.visible
                transition(::opacity, .2.s, Timing.easeIn)
                opacity = .8
            }
            "&:not(.open)" {
                transition(::visibility, .2.s, Timing.stepEnd)
                transition(::opacity, .2.s, Timing.easeIn)
            }
        }
        "#ui-dialog" {
            display = Display.none
            opacity = 0
            zIndex = 100100
            position = Position.fixed
            top = 50.px
            margin(0.em)
            padding(0.em)
            maxWidth = 66.em
            "&.open" {
                display = Display.block
                transition(::opacity, .2.s, Timing.easeIn)
                opacity = 1
            }
            child("*") {
                boxSizing = BoxSizing.borderBox
            }
        }
        "#ui-dialog-titlebar" {
            position = Position.relative
            backgroundColor = Color("#444")
            minHeight = 24.px
        }
        "#ui-dialog-close" {
            display = Display.block
            position = Position.absolute
            right = 0.em
            top = 0.em
            whiteSpace = WhiteSpace.nowrap
            cursor = Cursor.pointer
            fontSize = 120.pct
            margin(0.em)
            padding(0.em)
            width = 3.6.em
            height = 92.pct
            backgroundColor = Color.transparent
            border(1.px, BorderStyle.solid, Color.transparent)
            transition(duration = .2.s)
            hover {
                backgroundColor = Color("#b44")
                borderColor = Color("#d66")
            }
            userSelect = UserSelect.none
            +icon
        }
        "#ui-dialog-title" {
            margin(0.em)
            padding(.2.em, 3.5.em, .2.em, .5.em)
            fontSize = 1.5.em
            textAlign = TextAlign.center
            textTransform = TextTransform.uppercase
        }
        "#ui-dialog-body" {
            overflow = Overflow.auto
            minWidth = 280.px
            height = 92.pct
            height = 100.pct - 2.1.em
            backgroundColor = Color("#111")
            border(1.px, BorderStyle.solid, Color("#444"))
            textAlign = TextAlign.left
            lineHeight = LineHeight("1.5")
            padding(1.em)
            child(":first-child") {
                marginTop = 0.em
            }
            hr {
                backgroundColor = Color("#444")
            }
            "ul.buttons" {
                margin(0.em)
                padding(0.em)
                listStyleType = ListStyleType.none
                "li" {
                    display = Display.inlineBlock
                    margin(0.em)
                    padding(.4.em, .4.em, 0.em, 0.em)
                }
                child("li + li > button") {
                    marginLeft = 1.em
                }
            }
        }
    }
    val ui: RuleSet = {
        "#ui-dialog-body" {
            "&.settings" {
                "[id|=setting-body] > div:first-child" {
                    display = Display.table
                    width = 100.pct
                }
                "[id|=setting-label]" {
                    display = Display.tableCell
                    padding(.4.em, 2.em, .4.em, 0.em)
                    adjacentSibling("div") {
                        display = Display.tableCell
                        minWidth = 8.em
                        textAlign = TextAlign.right
                        verticalAlign = VerticalAlign.middle
                        whiteSpace = WhiteSpace.nowrap
                    }
                }
            }
            "&.list" {
                padding(0.em)
                ul {
                    margin(0.em)
                    padding(0.em)
                    listStyleType = ListStyleType.none
                    border(1.px, BorderStyle.solid, Color.transparent)
                }
                "li" {
                    margin(0.em)
                    not(":first-child") {
                        borderTop(1.px, BorderStyle.solid, Color("#444"))
                    }
                    a {
                        display = Display.block
                        padding(.25.em, .75.em)
                        border(1.px, BorderStyle.solid, Color.transparent)
                        color = Color("#eee")
                        textDecoration = TextDecoration.none
                        hover {
                            backgroundColor = Color("#333")
                            borderColor = Color("#eee")
                        }
                    }
                }
            }
            "&.saves" {
                padding(0.em, 0.em, 1.px)
                child(":not(:first-child)") {
                    borderTop(1.px, BorderStyle.solid, Color("#444"))
                }
                table {
                    borderSpacing = 0.em
                    width = 100.pct
                }
                tr {
                    not(":first-child") {
                        borderTop(1.px, BorderStyle.solid, Color("#444"))
                    }
                }
                td {
                    padding(.33.em)
                    firstChild {
                        minWidth = 1.5.em
                        textAlign = TextAlign.center
                    }
                    nthChild("3") {
                        lineHeight = LineHeight("1.2")
                    }
                    lastChild {
                        textAlign = TextAlign.right
                    }
                }
                ".empty" {
                    color = Color("#999")
                    put("speak", "none")
                    textAlign = TextAlign.center
                    userSelect = UserSelect.none
                }
                ".datestamp" {
                    fontSize = 75.pct
                    marginLeft = 1.em
                }
                "ul.buttons" {
                    "li" {
                        padding(.4.em)
                        lastChild {
                            float = Float.right
                        }
                    }
                    child("li + li > button") {
                        marginLeft = .2.em
                    }
                }
            }
        }
    }
    val fullScreen: RuleSet = {
        ":-webkit-full-screen" {
            backgroundColor = Color("#111")
        }
        ".exitfullscreen img" {
            display = Display.none
        }
        "input#fullscreen" {
            display = Display.none
            checked {
                sibling(".exitfullscreen img") {
                    display = Display.inline
                }
                sibling(".gofullscreen img") {
                    display = Display.none
                }
            }
        }
        ".fullscreenImg" {
            cursor = Cursor.pointer
            put("transition", "0.3s")
            borderRadius = 3.px
            position = Position.absolute
            top = 5.px
            left = 5.px
            height = 25.px
            verticalAlign = VerticalAlign.textBottom
            visibility = Visibility.visible
            hover {
                backgroundColor = Color("#444")
            }
        }
    }
}

enum class UIBarSide(
    val outerSide: KMutableProperty1<StyledElement, LinearDimension>,
    val outerMargin: KMutableProperty1<StyledElement, LinearDimension>,
    val borderSide: KMutableProperty1<StyledElement, LinearDimension>,
    val borderWidth: KMutableProperty1<StyledElement, LinearDimension>,
    val openedCharacter: QuotedString,
    val closedCharacter: QuotedString
) {
    Left(
        outerSide = StyledElement::left,
        outerMargin = StyledElement::marginLeft,
        borderSide = StyledElement::right,
        borderWidth = StyledElement::borderRightWidth,
        openedCharacter = """\e81d""".quoted,
        closedCharacter = """\e81e""".quoted
    ), Right(
        outerSide = StyledElement::right,
        outerMargin = StyledElement::marginRight,
        borderSide = StyledElement::left,
        borderWidth = StyledElement::borderLeftWidth,
        openedCharacter = """\e81e""".quoted,
        closedCharacter = """\e81d""".quoted
    )
}

object ComponentStyles : StyleSheet("app") {
    fun uiBar(side: UIBarSide): RuleSet = {
        side.outerSide.set(this, 0.em)
        put("-o-transition", "${side.outerSide.name} .2s ease-in")
        transition(side.outerSide, duration = .2.s, timing = Timing.easeIn)
        borderWidth = 0.em
        side.borderWidth.set(this, 1.px)
        "#ui-bar-toggle" {
            border(width = 1.px, style = BorderStyle.solid, color = Color("#444"))
            side.borderSide.set(this, 0.em)
            side.borderWidth.set(this, 0.em)
            padding(.25.em, .5.em)
            before {
                content = side.openedCharacter
            }
        }
        "#ui-bar-history [id|=history]:not(:first-child)" {
            side.outerMargin.set(this, 1.2.em)
        }
        sibling("#story") {
            side.outerMargin.set(this, 20.em)
        }
        +"stowed" {
            side.outerSide.set(this, (-15.5).em)
            sibling("#story") {
                side.outerMargin.set(this, 4.5.em)
            }
            "#ui-bar-toggle" {
                before {
                    content = side.closedCharacter
                }
            }
        }
        media("screen and (max-width: 1136px)") {
            sibling("#story") {
                side.outerMargin.set(this, 19.em)
            }
            +"stowed" {
                sibling("#story") {
                    side.outerMargin.set(this, 3.5.em)
                }
            }
        }
        media("screen and (max-width: 768px)") {
            sibling("#story") {
                side.outerMargin.set(this, 3.5.em)
            }
        }
        position = Position.fixed
        zIndex = 50
        top = 0.em
        width = 17.5.em
        height = 100.pct
        margin(all = 0.em)
        padding(all = 0.em)
        backgroundColor = Color("#222")
        textAlign = TextAlign.center
        "a" {
            textDecoration = TextDecoration.none
        }
        "hr" {
            borderColor = Color("#444")
        }
        borderStyle = BorderStyle.solid
        borderColor = Color("#444")
        +"stowed" {
            "#ui-bar-body, #ui-bar-history" {
                visibility = Visibility.hidden
                put("-o-transition", "visibility .2s step-end")
                transition(::visibility, duration = .2.s, timing = Timing.stepEnd)
            }
        }
        "#ui-bar-tray" {
            position = Position.absolute
            top = .2.em
            left = 0.em
            right = 0.em
        }
        "#ui-bar-body" {
            height = 90.pct
            height = 100.pct - 2.5.em
            margin(2.5.em, 0.em)
            padding(0.em, 1.5.em)
            lineHeight = LineHeight("1.5")
            overflow = Overflow.auto
            child(":not(:first-child)") {
                marginTop = 2.em
            }
        }
        "#ui-bar-history [id|=history], #ui-bar-toggle" {
            fontSize = 1.2.em
            lineHeight = LineHeight.inherit
            color = Color("#eee")
            backgroundColor = Color.transparent
        }
        "#ui-bar-history [id|=history]" {
            border(width = 1.px, style = BorderStyle.solid, color = Color("#444"))
        }
        "#ui-bar-toggle" {
            display = Display.block
            position = Position.absolute
            top = 0.em
            padding(top = .3.em, horizontal = .45.em, bottom = .25.em)
            hover {
                backgroundColor = Color("#444")
                borderColor = Color("#eee")
            }
        }
        "#ui-bar-history" {
            margin(0.em, LinearDimension.auto)
            "[id|=history]" {
                padding(top = .2.em, horizontal = .45.em, bottom = .35.em)
                ":hover" {
                    backgroundColor = Color("#444")
                    borderColor = Color("#eee")
                }
                ":disabled" {
                    color = Color("#444")
                    backgroundColor = Color.transparent
                    borderColor = Color("#444")
                }
            }
            "#history-jumpto" {
                padding(top = .2.em, horizontal = .665.em, bottom = .35.em)
            }
        }
        "#story-title" {
            margin(all = 0.em)
            fontSize = 162.5.pct
        }
        "#story-author" {
            marginTop = 2.em
            fontWeight = FontWeight.w700
        }
        "#menu ul" {
            margin(top = 1.em, horizontal = 0.em, bottom = 0.em)
            padding(all = 0.em)
            listStyleType = ListStyleType.none
            border(width = 1.px, style = BorderStyle.solid, color = Color("#444"))
            ":empty" {
                display = Display.none
            }
        }
        "#menu li" {
            margin(all = 0.em)
            ":not(:first-child)" {
                borderTop(width = 1.px, style = BorderStyle.solid, color = Color("#444"))
            }
            "a" {
                display = Display.block
                padding(vertical = .25.em, horizontal = .75.em)
                border(width = 1.px, style = BorderStyle.solid, color = Color.transparent)
                color = Color("#eee")
                textTransform = TextTransform.uppercase
                ":hover" {
                    backgroundColor = Color("#444")
                    borderColor = Color("#eee")
                }
            }
        }
        "#menu a, #ui-bar-history [id|=history], #ui-bar-toggle" {
            userSelect = UserSelect.none
            put("-webkit-user-select", "none")
            put("-moz-user-select", "none")
            put("-ms-user-select", "none")
        }
        "#menu-core li[id|=menu-item] a:before, #ui-bar-history [id|=history], #ui-bar-toggle:before"(icon)
        "#menu-item-saves a:before" {
            content = """\e82b\00a0""".quoted
        }
        "#menu-item-settings a:before" {
            content = """\e82d\00a0""".quoted
        }
        "#menu-item-restart a:before" {
            content = """\e82c\00a0""".quoted
        }
        "#menu-item-share a:before" {
            content = """\e82f\00a0""".quoted
        }
    }
}