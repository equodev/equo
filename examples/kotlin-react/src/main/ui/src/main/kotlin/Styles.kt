import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import styled.StyleSheet

object Styles : StyleSheet("Styles", isStatic = true) {
    val container by css {
        width = 100.pct
        display = Display.flex
        flexDirection = FlexDirection.column
        alignItems = Align.center
        textAlign = TextAlign.center

        padding(top = 0.px);
    }

    val logo by css {
        height = 300.px;
        width = 300.px;
    }
    val usefulLinks by css {
        alignItems = Align.center;
        display = Display.flex;
        flexDirection = FlexDirection.row;
        cursor = Cursor.pointer;
    }

    val usefulMinicard by css {
        display = Display.flex;
        alignItems = Align.center;
        justifyContent = JustifyContent.center;

        borderColor = rgba(150, 121, 255, 0.377)
        borderWidth = 1.px
        borderStyle = BorderStyle.solid
        borderRadius = 10.px;
        height = 80.px;
        width = 350.px;

        padding(25.px);
        margin(15.px)

        hover {
            backgroundColor = hex(0x3884ff)
        }
    }

    val link by css {
        alignItems = Align.center;
        color = Color.black;
        display = Display.flex;
        justifyContent = JustifyContent.center;

        textDecoration = TextDecoration.none;
    }

    val arrowIcon by css {
        height = 30.px;
        margin(left = 10.px)
    }
} 
