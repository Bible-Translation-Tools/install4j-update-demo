package org.wycliffeassociates.install4j

import com.install4j.api.launcher.ApplicationLauncher
import com.install4j.api.launcher.Variables
import com.install4j.api.update.ApplicationDisplayMode
import com.install4j.api.update.UpdateChecker
import java.lang.Thread.sleep
import javafx.application.Platform
import javafx.beans.property.BooleanProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.geometry.Pos
import kotlin.concurrent.thread
import tornadofx.*

const val version = 7

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

    val progressListener = UpdateProgressListener(
        percentCompletedProperty = percentCompleteProperty,
        secondaryPercentCompletedProperty = secondaryPercentCompleteProperty,
        statusMessageProperty = statusMessageProperty,
        detailMessageProperty = detailedMessageProperty
    )
    val updater = UpdateLauncher(progressListener)

    override val root = vbox {

        UpdateChecker.executeScheduledUpdate(null, true, null)

        val showProgress = SimpleBooleanProperty(false)
        val hasCheckedUpdate = SimpleBooleanProperty(false)
        val updateAvailable = SimpleBooleanProperty(false)
        val isUpdating = SimpleBooleanProperty(false)
        val noUpdatesAvailable = SimpleBooleanProperty(false)
        val updateFinished = SimpleBooleanProperty(false)

        val updateUrlText = SimpleStringProperty()
        val updateSize = SimpleStringProperty()
        val updateVersion = SimpleStringProperty()
        val showUpdateButton = SimpleBooleanProperty(false)

        alignment = Pos.CENTER
        prefWidth = 800.0
        prefHeight = 600.0

        label("Current Version: $version")

        button("Check For Updates") {
            visibleProperty().bind(hasCheckedUpdate.not())
            setOnAction {
                hasCheckedUpdate.set(true)
                val updateUrl: String = Variables.getCompilerVariable("sys.updatesUrl")
                val updateDescriptor = UpdateChecker.getUpdateDescriptor(updateUrl, ApplicationDisplayMode.GUI)
                if (updateDescriptor.possibleUpdateEntry != null) {
                    updateAvailable.set(true)
                    updateUrlText.set(updateDescriptor.possibleUpdateEntry.url.toString())
                    updateSize.set(updateDescriptor.possibleUpdateEntry.fileSizeVerbose)
                    updateVersion.set(updateDescriptor.possibleUpdateEntry.newVersion)
                    showUpdateButton.set(true)
                } else {
                    noUpdatesAvailable.set(true)
                }
            }
        }

        label("You are on the latest version!") {
            visibleWhen { noUpdatesAvailable }
            managedWhen { visibleProperty() }
        }

        vbox {
            visibleWhen { updateAvailable }
            managedWhen { visibleProperty() }

            hbox {
                label("Update Version: ")
                label { textProperty().bind(updateVersion) }
            }

            hbox {
                label("Update Size: ")
                label { textProperty().bind(updateSize) }
            }

            label("Update URL:")
            hyperlink {
                visibleWhen { textProperty().isNotEmpty }
                textProperty().bind(updateUrlText)
                action {
                    hostServices.showDocument(textProperty().value)
                }
            }
            spacer {}
            button("Update Now") {
                visibleProperty().bind(showUpdateButton)
                setOnAction {
                    showProgress.set(true)
                    isUpdating.set(true)
                    updater.update {
                        Platform.runLater {
                            showProgress.set(false)
                            isUpdating.set(false)
                            updateFinished.set(true)
                        }
                    }
                }
            }
        }

        vbox {
            visibleWhen { isUpdating }
            managedWhen { visibleProperty() }

            label { textProperty().bind(statusMessageProperty) }
            label { textProperty().bind(detailedMessageProperty) }

            progressbar {
                visibleProperty().bind(showProgress)
                progressProperty().bind(percentCompleteProperty.divide(100.0))
            }

            progressbar {
                visibleProperty().bind(showProgress)
                progressProperty().bind(secondaryPercentCompleteProperty.divide(100.0))
            }
        }

        vbox {
            visibleWhen { updateFinished }
            managedWhen { visibleProperty() }

            hbox {
                button("Restart Later") {
                    setOnAction {
                        updateFinished.set(false)
                    }
                }
                spacer {  }
                button("Restart Now") {
                    setOnAction {
                        UpdateChecker.executeScheduledUpdate(null, true, null)
                    }
                }
            }
        }
    }
}

class UpdateLauncher(private val listener: UpdateProgressListener? = null) : ApplicationLauncher.Callback {

    fun update(onComplete: () -> Unit) {
        ApplicationLauncher.launchApplication("140", null, false, this)
        thread {
            while (!UpdateChecker.isUpdateScheduled()) {
                sleep(1000)
            }
            onComplete()
        }
    }

    override fun exited(exitValue: Int) {
    }

    override fun prepareShutdown() {
    }

    override fun createProgressListener(): ApplicationLauncher.ProgressListener? {
        return listener
    }
}

class UpdateProgressListener(
    private val screenActivatedProperty: StringProperty? = null,
    private val secondaryPercentCompletedProperty: IntegerProperty? = null,
    private val percentCompletedProperty: IntegerProperty? = null,
    private val statusMessageProperty: StringProperty? = null,
    private val detailMessageProperty: StringProperty? = null,
    private val actionStartedProperty: StringProperty? = null,
    private val indeterminateProgressProperty: BooleanProperty? = null
) : ApplicationLauncher.ProgressListener {


    override fun screenActivated(id: String?) {
        screenActivatedProperty?.set(id)
    }

    override fun actionStarted(id: String?) {
        actionStartedProperty?.set(id)
    }

    override fun statusMessage(message: String?) {
        statusMessageProperty?.set(message)
    }

    override fun detailMessage(message: String?) {
        detailMessageProperty?.set(message)
    }

    override fun percentCompleted(value: Int) {
        percentCompletedProperty?.set(value)
    }

    override fun secondaryPercentCompleted(value: Int) {
        secondaryPercentCompletedProperty?.set(value)
    }

    override fun indeterminateProgress(indeterminateProgress: Boolean) {
        indeterminateProgressProperty?.set(indeterminateProgress)
    }
}
