package you.thiago.phrasedroid.ui

import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.UIUtil
import you.thiago.phrasedroid.data.ResourceFile
import java.awt.BorderLayout
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.border.Border

class TranslationsContent(translations: List<ResourceFile>) {

    val contentPanel: JPanel = JPanel()

    init {
        contentPanel.border = BorderFactory.createEmptyBorder(40, 0, 0, 0)
        contentPanel.add(buildTranslationsContent(translations), BorderLayout.CENTER)
    }

    private fun buildTranslationsContent(translations: List<ResourceFile>): JPanel {
        val contentPanel = JPanel()
        contentPanel.layout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)

        translations.forEach { resource ->
            contentPanel.add(buildStringContent(resource), BorderLayout.PAGE_START)
        }

        return contentPanel
    }

    private fun buildStringContent(resource: ResourceFile): JPanel {
        val fileNameLabel = JBLabel("Language File: %s".format(resource.filename))
        fileNameLabel.font = fileNameLabel.font.deriveFont(Font.BOLD)

        val translationLabel = JBLabel(resource.translation)
        translationLabel.fontColor = UIUtil.FontColor.NORMAL
        translationLabel.background = JBColor.WHITE

        val translationPanel = JPanel()
        translationPanel.border = BorderFactory.createEmptyBorder(10, 0, 0, 0)
        translationPanel.background = JBColor.WHITE
        translationPanel.add(translationLabel, BorderLayout.PAGE_START)

        val contentPanel = JPanel()
        contentPanel.layout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)
        contentPanel.border = BorderFactory.createEmptyBorder(20, 0, 0, 0)

        contentPanel.add(fileNameLabel, BorderLayout.NORTH)
        contentPanel.add(translationPanel, BorderLayout.SOUTH)

        return contentPanel
    }
}