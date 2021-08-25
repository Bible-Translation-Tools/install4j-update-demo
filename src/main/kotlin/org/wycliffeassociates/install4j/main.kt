package org.wycliffeassociates.install4j

import org.wycliffeassociates.install4j.ui.view.UpdaterView
import tornadofx.*

const val version = 15

fun main() {
    launch<DemoApp>()
}

class DemoApp : App(UpdaterView::class)
