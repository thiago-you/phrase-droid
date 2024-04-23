package you.thiago.phrasedroid.state

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import org.jetbrains.annotations.NotNull
import javax.swing.JComponent
import javax.swing.JPanel


/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class AppSettingsComponent {
    val panel: JPanel

    private val settingsFilePathField = TextFieldWithBrowseButton()

    init {
        settingsFilePathField.toolTipText = "/root/phrase-droid-settings.json"
        settingsFilePathField.textField.text = "/root/phrase-droid-settings.json"

        settingsFilePathField.addBrowseFolderListener(
            "Select File", // Dialog Title
            "Select the JSON settings file path", // Description
            null, // Project (can be null if you don't need project context)
            FileChooserDescriptor(true, false, false, false, false, false)
        )

        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Set JSON settings file path: "), settingsFilePathField, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    val preferredFocusedComponent: JComponent
        get() = settingsFilePathField

    @get:NotNull
    var settingsFilePath: String?
        get() = settingsFilePathField.text
        set(newText) {
            settingsFilePathField.text = newText ?: String()
        }
}