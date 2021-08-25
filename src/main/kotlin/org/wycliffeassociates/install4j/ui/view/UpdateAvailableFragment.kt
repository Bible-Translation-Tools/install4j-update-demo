package org.wycliffeassociates.install4j.ui.view

import com.jfoenix.controls.JFXButton
import javafx.geometry.Pos
import org.wycliffeassociates.install4j.ui.viewmodel.AppUpdaterViewModel
import tornadofx.*

class UpdateAvailableFragment : Fragment() {

    val vm: AppUpdaterViewModel by inject()

    override val root = vbox {

        fitToParentWidth()

        visibleProperty().bind(vm.showUpdateAvailable)
        managedProperty().bind(visibleProperty())

        styleClass.add("updater__container")

        label("Update Available!").apply {
            styleClass.add("updater__subtitle")
        }

        hbox {
            style {
                spacing = 10.px
                alignment = Pos.CENTER_LEFT
            }
            label("Update Version:").apply {
                styleClass.add("updater__text")
            }
            label {
                textProperty().bind(vm.updateVersion)
                styleClass.add("updater__text")
            }
        }

        hbox {
            style {
                alignment = Pos.CENTER_LEFT
                spacing = 10.px
            }
            label("Update Size:").apply {
                styleClass.add("updater__text")
            }
            label {
                styleClass.add("updater__text")
                textProperty().bind(vm.updateSize)
            }
        }

        label("Update URL").apply {
            styleClass.add("updater__subtitle")
        }
        hyperlink {
            styleClass.add("updater__text--link")
            textProperty().bind(vm.updateUrlText)
            action {
                hostServices.showDocument(textProperty().value)
            }
        }
        add(
            JFXButton("Update Now").apply {
                styleClass.addAll("btn", "btn--secondary")
                setOnAction {
                    vm.downloadUpdate()
                }
            }
        )
    }
}
