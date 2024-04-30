package you.thiago.phrasedroid.state

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import org.jetbrains.annotations.NotNull
import you.thiago.phrasedroid.enums.SettingsEnum
import you.thiago.phrasedroid.util.JsonTemplate
import java.awt.Component
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JEditorPane
import javax.swing.JPanel

/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class AppSettingsComponent {

    val panel: JPanel = JPanel()

    private val settingsFilePathField = TextFieldWithBrowseButton()

    private val defaultFileRelativePath by lazy { "/%s".format(SettingsEnum.RELATIVE_PATH.value) }

    init {
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(buildSettingsInputComponent(), 0)
    }

    private fun buildSettingsInputComponent(): JPanel {
        settingsFilePathField.toolTipText = defaultFileRelativePath
        settingsFilePathField.textField.text = defaultFileRelativePath

        settingsFilePathField.addBrowseFolderListener(
            "Select File",
            "Select the JSON settings file",
            null,
            getFileChooseDescriptor()
        )

        val inputLabel = JBLabel("Settings file path (JSON): ")
        inputLabel.font = inputLabel.font.deriveFont(Font.BOLD)
        inputLabel.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)

        val noteLabel = JBLabel("Note: Plugin default relative path \"$defaultFileRelativePath\"")
        noteLabel.border = BorderFactory.createEmptyBorder(10, 0, 10, 0)

        val exampleLabel = JBLabel("JSON file template:")
        exampleLabel.font = exampleLabel.font.deriveFont(Font.BOLD)
        exampleLabel.border = BorderFactory.createEmptyBorder(30, 0, 10, 0)

        val examplePanel = buildExamplePanel()
        examplePanel.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)

        noteLabel.alignmentX = Component.LEFT_ALIGNMENT
        inputLabel.alignmentX = Component.LEFT_ALIGNMENT
        exampleLabel.alignmentX = Component.LEFT_ALIGNMENT
        examplePanel.alignmentX = Component.LEFT_ALIGNMENT
        settingsFilePathField.alignmentX = Component.LEFT_ALIGNMENT

        val inputPanel = JPanel()
        inputPanel.layout = BoxLayout(inputPanel, BoxLayout.Y_AXIS)
        inputPanel.add(inputLabel)
        inputPanel.add(settingsFilePathField)
        inputPanel.add(noteLabel)
        inputPanel.add(exampleLabel)
        inputPanel.add(examplePanel)

        val form = FormBuilder.createFormBuilder()
            .addComponent(inputPanel)
            .addComponentFillVertically(JPanel(), 0)
            .panel

        return form
    }

    private fun buildExamplePanel(): JEditorPane {
        val editorPane = JEditorPane("text/html", getJsonTemplate())

        editorPane.isEditable = false
        editorPane.border = null
        editorPane.alignmentX = Component.LEFT_ALIGNMENT

        return editorPane
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

    private fun getJsonTemplate(): String = "<pre style=\"padding: 3px 4px;\">${JsonTemplate.get()}</pre>"
}