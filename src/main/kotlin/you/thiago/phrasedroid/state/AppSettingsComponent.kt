package you.thiago.phrasedroid.state

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import org.jetbrains.annotations.NotNull
import you.thiago.phrasedroid.enums.SettingsEnum
import javax.swing.JPanel

/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class AppSettingsComponent {

    val panel: JPanel

    private val settingsFilePathField = TextFieldWithBrowseButton()

    init {
        settingsFilePathField.toolTipText = "/%s".format(SettingsEnum.RELATIVE_PATH.value)
        settingsFilePathField.textField.text = "/%s".format(SettingsEnum.RELATIVE_PATH.value)

        settingsFilePathField.addBrowseFolderListener(
            "Select File",
            "Select the JSON settings file",
            null,
            getFileChooseDescriptor()
        )

        val inputLabel = JBLabel("Set JSON settings file path: ")

        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(inputLabel, settingsFilePathField, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    @get:NotNull
    var settingsFilePath: String?
        get() = settingsFilePathField.text
        set(newText) {
            settingsFilePathField.text = newText ?: String()
        }

    private fun getFileChooseDescriptor(): FileChooserDescriptor {
        return FileChooserDescriptor(
            true,
            false,
            false,
            false,
            false,
            false
        )
    }
}