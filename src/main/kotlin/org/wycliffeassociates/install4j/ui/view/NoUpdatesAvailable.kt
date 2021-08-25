package org.wycliffeassociates.install4j.ui.view

import org.wycliffeassociates.install4j.ui.viewmodel.AppUpdaterViewModel
import tornadofx.Fragment
import tornadofx.fitToParentWidth
import tornadofx.label
import tornadofx.vbox

class NoUpdatesAvailable : Fragment() {

    val vm: AppUpdaterViewModel by inject()

    override val root = vbox {

        fitToParentWidth()

        visibleProperty().bind(vm.showNoUpdatesAvailable)
        managedProperty().bind(visibleProperty())

        label("Up to date!") {
            styleClass.add("updater__subtitle")
        }

        label("Orature is up to date with the latest version.") {
            styleClass.add("updater__text")
        }
    }
}
