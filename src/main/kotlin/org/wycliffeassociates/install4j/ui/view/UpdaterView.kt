package org.wycliffeassociates.install4j.ui.view

import javafx.scene.paint.Color
import org.wycliffeassociates.install4j.ui.viewmodel.AppUpdaterViewModel
import tornadofx.*

class UpdaterView: View() {

    val vm: AppUpdaterViewModel by inject()

    init {
        vm.applyScheduledUpdate()
        importStylesheet(resources.get("/css/updater.css"))
    }

    override val root = stackpane {

        minHeight = 400.0
        minWidth = 400.0
        padding = insets(20.0)

        style {
            backgroundColor += Color.WHITE
        }

        borderpane {
            fitToParentSize()

            top = label("Update") {
                styleClass.add("updater__title")
                style {
                    padding = box(0.px, 0.px, 20.px, 0.px)
                }
            }

            center = stackpane {
                add<NoUpdatesAvailable>()
                add<UpdateWillCompleteLaterFragment>()
                add<UpdateCompleteFragment>()
                add<UpdateDownloadingFragment>()
                add<UpdateAvailableFragment>()
                add<CheckForUpdatesFragment>()
            }
        }
    }
}
