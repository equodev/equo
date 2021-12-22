import kotlinx.css.*
import styled.StyleSheet

object GlobalStyles : StyleSheet("GlobalStyles", isStatic = true) {

    private val styles = CSSBuilder(allowClasses = false).apply {
        body {
            fontFamily = "Rajdhani, sans-serif"
        }

        h1 {
            fontSize = 3.75.rem
        }
        margin(bottom = 0.px, top = 0.px)

        p {
            color = hex(0x62676c)
            fontSize = 1.2.rem
        }

        a {
            color = hex(0x3884ff)
            fontSize = 1.2.rem
        }
    }


    fun applyGlobalStyle() {
        styled.injectGlobal(styles.toString())
    }
}