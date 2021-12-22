import react.dom.render
import kotlinx.browser.document
import kotlinx.browser.window

fun main() {
    GlobalStyles.applyGlobalStyle()
    window.onload = {
        render(document.getElementById("root")) {
            child(MainPage::class) {
            }
        }
    }
}
