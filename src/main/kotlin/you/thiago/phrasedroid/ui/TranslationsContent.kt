package you.thiago.phrasedroid.ui

import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.UIUtil
import you.thiago.phrasedroid.data.ResourceFile
import java.awt.BorderLayout
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JPanel

class TranslationsContent(translations: List<ResourceFile>) {

    val contentPanel: JBScrollPane = JBScrollPane()

    init {
        contentPanel.viewport.add(buildTranslationsContent(translations))
        contentPanel.border = BorderFactory.createEmptyBorder(40, 20, 80, 20)
    }

    private fun buildTranslationsContent(translations: List<ResourceFile>): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        translations.forEach { resource ->
            val fileNameLabel = JBLabel("%s:".format(resource.locale))
            fileNameLabel.font = fileNameLabel.font.deriveFont(Font.BOLD)

            val fileNamePanel = JPanel(BorderLayout())
            fileNamePanel.add(fileNameLabel, BorderLayout.PAGE_START)
            fileNamePanel.border = BorderFactory.createEmptyBorder(0, 0, 5, 0)

            val translationLabel = JBLabel(resource.translation)
            translationLabel.fontColor = UIUtil.FontColor.NORMAL

            val translationPanel = JPanel(BorderLayout())
            translationPanel.background = JBColor.WHITE
            translationPanel.add(translationLabel, BorderLayout.PAGE_START)

            val contentPanel = JPanel()
            contentPanel.layout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)
            contentPanel.border = BorderFactory.createEmptyBorder(20, 0, 0, 0)
            contentPanel.add(fileNamePanel)
            contentPanel.add(translationPanel)

            panel.add(contentPanel)
        }

        return panel
    }
}