import kotlinx.html.js.onClickFunction
import react.*
import react.dom.attrs
import styled.css
import styled.styledA
import styled.styledDiv
import styled.styledImg

data class MinicardData(val name: String, val link: String, val onSelect: ((Minicard) -> Unit)?)

external interface MinicardProps : RProps {
    var list: List<MinicardData>
}

data class MinicardState(val response: String) : RState

@JsExport
class Minicard(props: MinicardProps) : RComponent<MinicardProps, MinicardState>(props) {
    init {
        state = MinicardState("")
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                +Styles.usefulLinks
            }
            props.list.forEach { currentProp ->
                styledDiv {
                    css {
                        +Styles.usefulMinicard
                    }
                    if (currentProp.onSelect == null) {
                        styledA(href = currentProp.link, target = "_blank") {
                            css {
                                +Styles.link
                            }
                            +currentProp.name
                            styledImg(alt = "arrow icon", src = "arrow-icon.png") {
                                css {
                                    +Styles.arrowIcon
                                }
                            }
                        }
                    } else {
                        styledDiv {
                            css {
                                +Styles.link
                            }
                            +currentProp.name
                        }
                        attrs {
                            onClickFunction = {
                                currentProp.onSelect.invoke(this@Minicard)
                            }
                        }
                    }
                    +state.response
                }
            }
        }
    }
}