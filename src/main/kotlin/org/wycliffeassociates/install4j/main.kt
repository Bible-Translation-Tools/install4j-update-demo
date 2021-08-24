package org.wycliffeassociates.install4j

import com.install4j.api.launcher.ApplicationLauncher
import com.install4j.api.launcher.Variables
import com.install4j.api.update.ApplicationDisplayMode
import com.install4j.api.update.UpdateChecker
import java.lang.Thread.sleep
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import kotlin.concurrent.thread
import tornadofx.*

const val version = 6

fun main() {
    launch<DemoApp>()
}

class DemoApp : App(MainView::class)

class MainView : View() {
    private val secondaryPercentCompleteProperty = SimpleIntegerProperty(0)
    private val percentCompleteProperty = SimpleIntegerProperty(0)
    private val statusMessageProperty = SimpleStringProperty()
    private val detailedMessageProperty = SimpleStringProperty()
    private val actionStartedProperty = SimpleStringProperty()

    override val root = vbox {

        val showProgress = SimpleBooleanProperty(false)

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

                        textfield { textProperty().bind(statusMessageProperty) }
                        textfield { textProperty().bind(detailedMessageProperty) }
                        textfield { textProperty().bind(actionStartedProperty) }

                        progressbar {
                            visibleProperty().bind(showProgress)
                            progressProperty().bind(percentCompleteProperty)
                        }

                        progressbar {
                            text = "Secondary"
                            visibleProperty().bind(showProgress)
                            progressProperty().bind(secondaryPercentCompleteProperty)
                        }

                        prefWidth = 100.0
                        prefHeight = 150.0
                        alignment = Pos.CENTER

                        button("Update") {
                            setOnAction {
                                val updater = UpdateLauncher()
                                val progress = updater.createProgressListener()
                                println(progress)

                                ApplicationLauncher.launchApplication("140", null, false, updater)
                                showProgress.set(true)
                                thread {
                                    while (UpdateChecker.isUpdateScheduled() != true) {
                                        sleep(1000)
                                    }
                                    UpdateChecker.executeScheduledUpdate(null, true, null)
                                    showProgress.set(false)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    inner class UpdateLauncher : ApplicationLauncher.Callback {
        override fun exited(exitValue: Int) {

        }

        override fun prepareShutdown() {
        }

        override fun createProgressListener(): ApplicationLauncher.ProgressListener {
            return ProgressListener()
        }
    }

    inner class ProgressListener : ApplicationLauncher.ProgressListener {
        override fun screenActivated(id: String?) {
        }

        override fun actionStarted(id: String?) {
            actionStartedProperty.set(id)
        }

        override fun statusMessage(message: String?) {
            statusMessageProperty.set(message)
        }

        override fun detailMessage(message: String?) {
            detailedMessageProperty.set(message)
        }

        override fun percentCompleted(value: Int) {
            percentCompleteProperty.set(value)
        }

        override fun secondaryPercentCompleted(value: Int) {
            secondaryPercentCompleteProperty.set(value)
        }

        override fun indeterminateProgress(indeterminateProgress: Boolean) {
        }
    }
}
