package org.wycliffeassociates.install4j.ui.view

import com.jfoenix.controls.JFXButton
import org.wycliffeassociates.install4j.ui.viewmodel.AppUpdaterViewModel
import org.wycliffeassociates.install4j.version
import tornadofx.*

class CheckForUpdatesFragment : Fragment() {

    val vm: AppUpdaterViewModel by inject()

    override val root = vbox {
        fitToParentSize()

        styleClass.add("updater__container")

        visibleProperty().bind(vm.showCheckForUpdate)
        managedProperty().bind(visibleProperty())

        vbox {
            styleClass.addAll("updater__version", "updater__container")
            label("Current Version") {
                styleClass.add("updater__subtitle")
            }
            label("$version") {
                styleClass.add("updater__text")
            }
        }

        add(
            JFXButton("Check For Updates").apply {
                styleClass.addAll("btn", "btn--secondary")
                setOnAction {
                    vm.checkForUpdates()
                }
            }
        )
    }
}
