import kotlinx.html.DIV
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv
import styled.styledImg

val cards = listOf(
    MinicardData(
        "Development guides",
        "https://docs.equoplatform.com/sdk/1.1.x/developing/getting-started/creating.html",
        null
    ),
    MinicardData(
        "Web UI frameworks Development",
        "https://docs.equoplatform.com/sdk/1.1.x/developing/hot-reload.html",
        null
    ),
    MinicardData(
        "Javascript API documentation",
        "https://docs.equoplatform.com/sdk/1.1.x/index-api-javascript.html",
        null
    ),
    MinicardData("Java API documentation", "https://docs.equoplatform.com/sdk/1.1.x/index-api-java.html", null),
    MinicardData("Click me!", "") { minicard ->
        val comm = EquoCommService.get()
        comm.on("MyKotlinEvent") { response ->
            console.log(response)
            minicard.setState(MinicardState(response = response))
        }
        comm.send("MyEventHandler", "Message from the frontend")
    }
)

@JsExport
class MainPage(props: RProps) : RComponent<RProps, RState>(props) {

    override fun RBuilder.render() {
        styledDiv {
            css {
                +Styles.container
            }
            styledImg(
                alt = "logo equo",
                src = "https://sfo2.digitaloceanspaces.com/equo-cms/2020/08/28/5f495e535cda5Isotipo.png"
            ) {
                css {
                    +Styles.logo
                }
            }
            h1 { +"Welcome to your first Equo App" }
            p {
                +"For a guide and recipes on how to configure / customize an Equo App, check out the "
                a(
                    href = "https://docs.equoplatform.com",
                    target = "_blank"
                ) { +"SDK Documentation" }
            }
            h2 { +"Useful Links" }

            generateCards()

            h2 { +"Ecosystem" }
            p {
                +"Learn Equo "
                a(href = "https://equoplatform.com", target = "_blank") {
                    +"here!"
                }
            }
        }
    }

    private fun StyledDOMBuilder<DIV>.generateCards() {
        var i = 0
        var minicardList: MutableList<MinicardData> = mutableListOf()
        cards.forEach { minicard ->
            minicardList.add(minicard)
            if (i++ % 2 == 1 || cards.size == i) {
                child(Minicard::class) {
                    attrs {
                        list = minicardList
                    }
                }
                minicardList = mutableListOf()
            }
        }
    }
}
