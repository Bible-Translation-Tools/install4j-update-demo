package org.wycliffeassociates.install4j

import org.wycliffeassociates.install4j.ui.view.UpdaterView
import tornadofx.*

const val version = 12

fun main() {
    launch<DemoApp>()
}

class DemoApp : App(UpdaterView::class)
