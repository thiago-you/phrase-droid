package you.thiago.phrasedroid.ui

import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.UIUtil
import you.thiago.phrasedroid.data.ResourceFile
import you.thiago.phrasedroid.util.TranslationUtil
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.*

class TranslationsContent(private var translations: List<ResourceFile>) {

    val contentPanel: JPanel = JPanel()

    private val scrollablePanel: JBScrollPane = buildScrollablePanel(translations)
    private val controlsPanel: JPanel = buildControlsPanel()

    private var hasEscapedData = false

    init {
        contentPanel.layout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)
        contentPanel.border = BorderFactory.createEmptyBorder(40, 0, 0, 0)
        contentPanel.add(controlsPanel)
        contentPanel.add(scrollablePanel)
    }

    private fun buildControlsPanel(): JPanel {
        val controlsPanel = JPanel()
        controlsPanel.add(buildControlsPanelContent())

        return controlsPanel
    }

    private fun buildScrollablePanel(translations: List<ResourceFile>): JBScrollPane {
        val scrollablePanel = JBScrollPane()
        scrollablePanel.viewport.add(buildTranslationsContent(translations))
        scrollablePanel.border = BorderFactory.createEmptyBorder(30, 20, 80, 20)

        return scrollablePanel
    }

    private fun updateControlsPanel() {
        val updatePanel = buildControlsPanelContent()

        SwingUtilities.invokeLater {
            controlsPanel.removeAll()
            controlsPanel.add(updatePanel)
            controlsPanel.revalidate()
            controlsPanel.repaint()
        }
    }

    private fun updateScrollablePanel(translations: List<ResourceFile>) {
        val updatedPanel = buildTranslationsContent(translations)

        SwingUtilities.invokeLater {
            scrollablePanel.viewport.remove(0)
            scrollablePanel.viewport.add(updatedPanel)
            scrollablePanel.viewport.revalidate()
            scrollablePanel.viewport.repaint()
        }
    }

    private fun buildControlsPanelContent(): JPanel {
        val controlsPanel = JPanel()
        controlsPanel.layout = BorderLayout()
        controlsPanel.border = BorderFactory.createEmptyBorder(0, 20, 0, 20)

        val escapeDataButtonLabel = if (!hasEscapedData) {
            "Add Escape [CDATA]"
        } else {
            "Remove Escape [CDATA]"
        }

        val leftButtonPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val escapeDataButton = JButton(escapeDataButtonLabel)
        escapeDataButton.preferredSize = Dimension(220, 40)
        escapeDataButton.addActionListener { _ ->
            translations = if (hasEscapedData) {
                TranslationUtil.removeTranslationsEscape(translations)
            } else {
                TranslationUtil.escapeTranslations(translations)
            }

            hasEscapedData = !hasEscapedData

            updateScrollablePanel(translations)
            updateControlsPanel()
        }

        leftButtonPanel.add(escapeDataButton)

        val rightButtonPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        val confirmTranslationButton = JButton("Apply Translations")
        confirmTranslationButton.preferredSize = Dimension(220, 40)
        confirmTranslationButton.addActionListener { _ -> }
        rightButtonPanel.add(confirmTranslationButton)

        controlsPanel.add(leftButtonPanel, BorderLayout.WEST)
        controlsPanel.add(rightButtonPanel, BorderLayout.EAST)

        return controlsPanel
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