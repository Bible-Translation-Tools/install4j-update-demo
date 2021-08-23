import javafx.geometry.Pos
import tornadofx.*

const val version = 1

fun main() {
    launch<DemoApp>()
}

class DemoApp : App(MainView::class)

class MainView : View() {
    override val root = vbox {
        alignment = Pos.CENTER
        prefWidth = 100.0
        prefHeight = 100.0
        label("Version $version")
        button("Update")
    }
}
