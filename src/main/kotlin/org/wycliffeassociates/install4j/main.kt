package org.wycliffeassociates.install4j

import com.install4j.api.launcher.Variables
import com.install4j.api.update.ApplicationDisplayMode
import com.install4j.api.update.UpdateChecker
import javafx.geometry.Pos
import tornadofx.*

const val version = 2

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
        button("Update") {
            setOnAction {
                val updateUrl: String = Variables.getCompilerVariable("sys.updatesUrl")
                val updateDescriptor = UpdateChecker.getUpdateDescriptor(updateUrl, ApplicationDisplayMode.GUI)
                if (updateDescriptor.possibleUpdateEntry != null) {
                    dialog {
                        text = updateDescriptor.possibleUpdateEntry.newVersion

                        prefWidth = 100.0
                        prefHeight = 100.0

                        button("Update") {
                            setOnAction {
                                val url = updateDescriptor.possibleUpdateEntry.url
                                val entry = updateDescriptor.possibleUpdateEntry
                            }
                        }
                    }
                }
            }
        }
    }


}
