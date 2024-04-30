package you.thiago.phrasedroid.state

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import org.jetbrains.annotations.NotNull
import you.thiago.phrasedroid.enums.SettingsEnum
import javax.swing.BoxLayout
import javax.swing.JPanel

/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class AppSettingsComponent {

    val panel: JPanel

    private val settingsFilePathField = TextFieldWithBrowseButton()

    private val defaultFileRelativePath by lazy { "/%s".format(SettingsEnum.RELATIVE_PATH.value) }

    init {
        settingsFilePathField.toolTipText = defaultFileRelativePath
        settingsFilePathField.textField.text = defaultFileRelativePath

        settingsFilePathField.addBrowseFolderListener(
            "Select File",
            "Select the JSON settings file",
            null,
            getFileChooseDescriptor()
        )

        val inputLabel = JBLabel("Set JSON settings file path: ")

        val form = FormBuilder.createFormBuilder()
            .addLabeledComponent(inputLabel, settingsFilePathField, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel

        panel= JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        panel.add(form, 0)
        panel.add(JBLabel("Note: Default relative path $defaultFileRelativePath"), 1)
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