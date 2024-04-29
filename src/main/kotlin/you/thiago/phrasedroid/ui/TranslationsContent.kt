package you.thiago.phrasedroid.ui

import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.UIUtil
import you.thiago.phrasedroid.data.ResourceFile
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel

class TranslationsContent(translations: List<ResourceFile>) {

    val contentPanel: JPanel = JPanel()

    init {
        contentPanel.layout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)
        contentPanel.border = BorderFactory.createEmptyBorder(40, 0, 0, 0)
        contentPanel.add(buildControlsPanel())
        contentPanel.add(buildScrollablePanel(translations))
    }

    private fun buildControlsPanel(): JPanel {
        val controlsPanel = JPanel()
        controlsPanel.layout = BorderLayout()
        controlsPanel.border = BorderFactory.createEmptyBorder(0, 20, 0, 20)

        val leftButtonPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val escapeDataButton = JButton("Escape Data [CDATA]")
        escapeDataButton.preferredSize = Dimension(220, 40)
        escapeDataButton.addActionListener { _ -> }
        leftButtonPanel.add(escapeDataButton)

        val rightButtonPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        val confirmTranslationButton = JButton("Apply Translations")
        confirmTranslationButton.preferredSize = Dimension(220, 40)
        confirmTranslationButton.addActionListener { _ ->  }
        rightButtonPanel.add(confirmTranslationButton)

        controlsPanel.add(leftButtonPanel, BorderLayout.WEST)
        controlsPanel.add(rightButtonPanel, BorderLayout.EAST)

        return controlsPanel
    }

    private fun buildScrollablePanel(translations: List<ResourceFile>): JBScrollPane {
        val scrollablePanel = JBScrollPane()
        scrollablePanel.viewport.add(buildTranslationsContent(translations))
        scrollablePanel.border = BorderFactory.createEmptyBorder(50, 20, 80, 20)

        return scrollablePanel
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