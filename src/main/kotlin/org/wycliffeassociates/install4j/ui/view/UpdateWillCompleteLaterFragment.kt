package org.wycliffeassociates.install4j.ui.view

import org.wycliffeassociates.install4j.ui.viewmodel.AppUpdaterViewModel
import tornadofx.*

class UpdateWillCompleteLaterFragment : Fragment() {

    val vm: AppUpdaterViewModel by inject()

    override val root = vbox {

        fitToParentWidth()

        visibleProperty().bind(vm.showUpdateScheduled)
        managedProperty().bind(visibleProperty())

        label("Update Scheduled!") {
            styleClass.add("updater__subtitle")
        }

        label("Orature will apply the update on application restart.") {
            styleClass.add("updater__text")
        }
    }
}
