package you.thiago.phrasedroid.toolbar

import com.intellij.ui.components.JBLabel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JProgressBar

class LoadingContent : ToolwindowContent() {

    init {
        contentPanel.border = BorderFactory.createEmptyBorder(40, 0, 0, 0)

        val loading = JPanel()
        loading.layout = BoxLayout(loading, BoxLayout.Y_AXIS)
        loading.add(buildLoadingContent())
        loading.add(buildProgressContent())

        contentPanel.add(loading, BorderLayout.CENTER)
    }

    private fun buildLoadingContent(): JPanel {
        val loadingLabel = JBLabel("Loading...")
        val loadingPanel = JPanel()
        loadingPanel.add(loadingLabel)

        return loadingPanel
    }

    private fun buildProgressContent(): JPanel {
        val progressBar = JProgressBar(0, 100)
        progressBar.isIndeterminate = true
        progressBar.preferredSize = Dimension(300, 20)

        val progressPanel = JPanel()
        progressPanel.add(progressBar)

        return progressPanel
    }

    @Suppress("unused")
    private fun buildMessageContent(): JPanel {
        val contentPanel = JPanel()
        contentPanel.layout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)

        val messageLabel = JBLabel("\"Do or do not. There is no try.\"")
        val messagePanel = JPanel()
        messagePanel.add(messageLabel)

        @Suppress("DialogTitleCapitalization")
        val authorLabel = JBLabel("- Yoda")
        val authorPanel = JPanel()
        authorPanel.add(authorLabel)

        contentPanel.add(messagePanel, BorderLayout.NORTH)
        contentPanel.add(authorPanel, BorderLayout.SOUTH)

        return contentPanel
    }
}