package org.wycliffeassociates.install4j.ui.view

import com.jfoenix.controls.JFXButton
import org.kordamp.ikonli.javafx.FontIcon
import org.wycliffeassociates.install4j.ui.viewmodel.AppUpdaterViewModel
import tornadofx.*

class UpdateCompleteFragment : Fragment() {

    val restartNowIcon = FontIcon("mdi-power")
    val restartLaterIcon = FontIcon("mdi-calendar-clock")

    val vm: AppUpdaterViewModel by inject()

    override val root = vbox {
        fitToParentWidth()

        styleClass.add("updater__container")

        visibleProperty().bind(vm.showUpdateCompleted)
        managedProperty().bind(visibleProperty())

        label("Download Complete!") {
            styleClass.add("updater__subtitle")
        }

        label("To apply this update, Orature must restart") {
            styleClass.add("updater__text")
        }

        hbox {
            styleClass.add("updater__container")
            add(
                JFXButton("Restart Later").apply {
                    graphic = restartLaterIcon
                    styleClass.addAll("btn", "btn--secondary")
                    setOnAction {
                        vm.updateLater()
                    }
                }
            )
            add(
                JFXButton("Restart Now").apply {
                    graphic = restartNowIcon
                    styleClass.addAll("btn", "btn--secondary")
                    setOnAction {
                        vm.updateAndRestart()
                    }
                }
            )
        }
    }
}
