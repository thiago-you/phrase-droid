package you.thiago.phrasedroid.toolbar

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import you.thiago.phrasedroid.WriteTranslationsAction
import you.thiago.phrasedroid.data.ResourceFile
import you.thiago.phrasedroid.state.FlashState
import you.thiago.phrasedroid.util.TranslationUtil
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.*

class TranslationsContent(
    private val event: AnActionEvent,
    private var translations: List<ResourceFile>
) : ToolwindowContent() {

    private val scrollablePanel: JBScrollPane = buildScrollablePanel(translations)
    private val controlsPanel: JPanel = buildControlsPanel()

    private var hasEscapedData = false
    private var isAllowUpdateSelected = false

    init {
        contentPanel.layout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)
        contentPanel.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        contentPanel.add(buildHeaderPanel())
        contentPanel.add(controlsPanel)
        contentPanel.add(buildCheckboxUpdatePanel())
        contentPanel.add(scrollablePanel)
    }

    private fun buildHeaderPanel(): JPanel {
        val headerPanel = JPanel(BorderLayout())
        headerPanel.border = BorderFactory.createEmptyBorder(10, 0, 20, 5)

        val dropdownMenuButton = JButton()
        dropdownMenuButton.icon = AllIcons.Actions.More
        dropdownMenuButton.toolTipText = "Actions"
        dropdownMenuButton.border = null
        dropdownMenuButton.isContentAreaFilled = false
        dropdownMenuButton.preferredSize = Dimension(32, 32)

        val popupMenu = JPopupMenu()

        val setMissingItemsAction1 = JMenuItem("Set \"EN\" as default translation on missing items")
        setMissingItemsAction1.border = BorderFactory.createEmptyBorder(10, 10, 5, 10)
        setMissingItemsAction1.addActionListener { setDefaultTranslationOnMissingItems("en") }

        val setMissingItemsAction2 = JMenuItem("Set \"PT\" as default translation on missing items")
        setMissingItemsAction2.border = BorderFactory.createEmptyBorder(10, 10, 5, 10)
        setMissingItemsAction2.addActionListener { setDefaultTranslationOnMissingItems("pt") }

        val removeMissingItemsTranslationAction = JMenuItem("Remove default translation on missing items")
        removeMissingItemsTranslationAction.border = BorderFactory.createEmptyBorder(10, 10, 5, 10)
        removeMissingItemsTranslationAction.addActionListener { removeTranslationOnMissingItems() }

        val closeAction = JMenuItem("Finish (F)")
        closeAction.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        closeAction.addActionListener { closeToolwindow() }

        popupMenu.add(setMissingItemsAction1)
        popupMenu.add(setMissingItemsAction2)
        popupMenu.add(removeMissingItemsTranslationAction)
        popupMenu.add(closeAction)

        dropdownMenuButton.addActionListener {
            popupMenu.show(dropdownMenuButton, 0, dropdownMenuButton.height)
        }

        headerPanel.add(dropdownMenuButton, BorderLayout.EAST)

        return headerPanel
    }

    private fun buildControlsPanel(): JPanel {
        val controlsPanel = JPanel()
        controlsPanel.add(buildControlsPanelContent())

        return controlsPanel
    }

    private fun buildScrollablePanel(translations: List<ResourceFile>): JBScrollPane {
        val scrollablePanel = JBScrollPane()
        scrollablePanel.viewport.add(buildTranslationsContent(translations))
        scrollablePanel.border = BorderFactory.createEmptyBorder(15, 20, 40, 20)

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

    private fun closeToolwindow() {
        val project = event.project ?: return

        val toolWindowManager = ToolWindowManager.getInstance(project)
        val toolWindow = toolWindowManager.getToolWindow("PhraseDroid")

        toolWindow?.contentManager?.removeAllContents(false)
        toolWindow?.hide(null)
    }

    private fun buildControlsPanelContent(): JPanel {
        val controlsPanel = JPanel()
        controlsPanel.layout = BorderLayout()
        controlsPanel.border = BorderFactory.createEmptyBorder(0, 20, 0, 20)

        val escapeDataButtonLabel = if (!hasEscapedData) {
            "Add Markup [CDATA]"
        } else {
            "Remove Markup [CDATA]"
        }

        val leftButtonPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val escapeDataButton = JButton(escapeDataButtonLabel)
        escapeDataButton.preferredSize = Dimension(220, 40)

        escapeDataButton.addActionListener { _ ->
            translations = if (hasEscapedData) {
                TranslationUtil.removeEscapeFromData(translations)
            } else {
                TranslationUtil.escapeData(translations)
            }

            hasEscapedData = !hasEscapedData

            updateScrollablePanel(translations)
            updateControlsPanel()
        }

        leftButtonPanel.add(escapeDataButton)

        val rightButtonPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        val confirmTranslationButton = JButton("Apply Translations")
        confirmTranslationButton.preferredSize = Dimension(220, 40)

        confirmTranslationButton.addActionListener { _ ->
            executeWriteTranslationActions()
        }

        rightButtonPanel.add(confirmTranslationButton)

        controlsPanel.add(leftButtonPanel, BorderLayout.WEST)
        controlsPanel.add(rightButtonPanel, BorderLayout.EAST)

        return controlsPanel
    }

    private fun buildCheckboxUpdatePanel(): JPanel {
        val checkBox = JCheckBox("Allow resource string update if already exists")

        checkBox.addActionListener { _ ->
            isAllowUpdateSelected = checkBox.isSelected
        }

        val checkBoxPanel = JPanel()
        checkBoxPanel.layout = BorderLayout()
        checkBoxPanel.border = BorderFactory.createEmptyBorder(10, 20, 0, 20)
        checkBoxPanel.add(checkBox, BorderLayout.WEST)

        return checkBoxPanel
    }

    private fun executeWriteTranslationActions() {
        ApplicationManager.getApplication().invokeLater {
            FlashState.translations = translations
            FlashState.isAllowUpdateSelected = isAllowUpdateSelected

            ActionUtil.invokeAction(WriteTranslationsAction(), event.dataContext, event.place, null, null)
        }
    }

    private fun buildTranslationsContent(translations: List<ResourceFile>): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        translations.forEach { resource ->
            val name = if (resource.translation.isNotEmpty()) {
                "%s:".format(resource.locale)
            } else {
                "%s (missing):".format(resource.locale)
            }

            val fileNameLabel = JBLabel(name)
            fileNameLabel.font = fileNameLabel.font.deriveFont(Font.BOLD)

            if (resource.translation.isEmpty()) {
                fileNameLabel.foreground = Color.RED
            }

            val fileNamePanel = JPanel(BorderLayout())
            fileNamePanel.add(fileNameLabel, BorderLayout.PAGE_START)
            fileNamePanel.border = BorderFactory.createEmptyBorder(0, 0, 5, 0)

            val translationPanel = JPanel(BorderLayout())
            translationPanel.background = JBColor.WHITE
            translationPanel.add(createCodeBlockLabel(resource.translation), BorderLayout.PAGE_START)

            val contentPanel = JPanel()
            contentPanel.layout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)
            contentPanel.border = BorderFactory.createEmptyBorder(20, 0, 0, 0)
            contentPanel.add(fileNamePanel)
            contentPanel.add(translationPanel)

            panel.add(contentPanel)
        }

        return panel
    }

    private fun createCodeBlockLabel(text: String): JBScrollPane {
        val paddedText = getCodeBlockHtmlTemplate(text.trim())
        val editorPane = JEditorPane("text/html", paddedText)

        editorPane.isEditable = false
        editorPane.border = null

        val scrollPane = JBScrollPane(editorPane)

        scrollPane.verticalScrollBarPolicy = JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPane.horizontalScrollBarPolicy = JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED

        return scrollPane
    }

    private fun getCodeBlockHtmlTemplate(text: String): String = text.trim()
        .replace("<![CDATA[", """&lt;![CDATA[""")
        .replace("]]>", """]]&gt;""")
        .let { """<pre style="padding: 3px 4px;">${it}</pre>""" }

    private fun setDefaultTranslationOnMissingItems(language: String) {
        translations = TranslationUtil.setDefaultLanguageToMissingItems(translations, language)

        updateScrollablePanel(translations)
        updateControlsPanel()
    }

    private fun removeTranslationOnMissingItems() {
        translations = TranslationUtil.removeDefaultLanguageOnMissingItems(translations)

        updateScrollablePanel(translations)
        updateControlsPanel()
    }
}