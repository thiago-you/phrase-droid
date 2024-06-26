package you.thiago.phrasedroid

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import you.thiago.phrasedroid.data.ApiSettings
import you.thiago.phrasedroid.data.ResourceFile
import you.thiago.phrasedroid.data.Translation
import you.thiago.phrasedroid.network.Api
import you.thiago.phrasedroid.state.AppState
import you.thiago.phrasedroid.state.FlashState
import you.thiago.phrasedroid.toolbar.LoadingContent
import you.thiago.phrasedroid.toolbar.ToolwindowContent
import you.thiago.phrasedroid.toolbar.TranslationsContent
import you.thiago.phrasedroid.util.*

class GetTranslationAction: AnAction() {

    private val settingsFilePath by lazy { AppState().getInstance().state.settingsFilePath }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val apiSettings = loadSettings(project) ?: return showCreateSettingsJsonDialog(project)

        val input = requireTranslationKey(project)

        if (input.isNullOrBlank()) {
            return
        }

        if (validateSettings(project, apiSettings)) {
            displayLoadingWindow(project)
            fetchApiData(e, apiSettings)
        }
    }

    private fun requireTranslationKey(project: Project, invalidKey: Boolean = false): String? {
        val icon = Messages.getInformationIcon().takeIf { !invalidKey } ?: Messages.getWarningIcon()
        val message = "Translation KEY:".takeIf { !invalidKey } ?: "Translation Key (invalid):"

        val input = Messages.showInputDialog(project, message, "PhraseDroid", icon)

        FlashState.translationKey = input ?: ""

        if (!input.isNullOrBlank()) {
            return validateInput(project, input).ifBlank {
                requireTranslationKey(project, true)
            }
        }

        return input
    }

    private fun loadSettings(project: Project): ApiSettings? {
        val file = if (settingsFilePath.isNotEmpty()) {
            FileUtil.getVirtualFileByPath(settingsFilePath)
        } else {
            FileUtil.getVirtualFileByProjectRelativePath(project)
        }

        return file?.let { JsonUtil.readConfig(it) }
    }

    private fun validateInput(project: Project, input: String?): String {
        val validInput = InputValidator.validate(input)

        if (validInput.isBlank()) {
            NotificationUtil.warning(project, "Invalid translation key: $input")
        }

        return validInput
    }

    private fun validateSettings(project: Project, apiSettings: ApiSettings): Boolean {
        SettingsValidator.validate(apiSettings)?.also {
            NotificationUtil.error(project, it)
            return false
        }

        return true
    }

    private fun fetchApiData(e: AnActionEvent, apiSettings: ApiSettings) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val list = Api.fetchTranslations(apiSettings)
            setupTranslationsWindow(e, list)
        }
    }

    private fun setupTranslationsWindow(e: AnActionEvent, translations: List<Translation>) {
        val project = e.project ?: return

        ApplicationManager.getApplication().invokeLater {
            if (translations.isNotEmpty()) {
                displayTranslations(e, ResFileMapper.getResourceFilesList(translations))
            } else {
                NotificationUtil.warning(project, "Translations not found for this KEY.", "PhraseDroid: Not Found")
                closeToolwindow(project)
            }
        }
    }

    private fun displayLoadingWindow(project: Project) {
        setupToolbarContent(project, LoadingContent(), "Loading")
    }

    private fun displayTranslations(e: AnActionEvent, resourceFiles: List<ResourceFile>) {
        val project = e.project ?: return
        setupToolbarContent(project, TranslationsContent(e, resourceFiles), "Confirmation")
    }

    private fun getToolWindow(project: Project): ToolWindow? {
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val toolWindow = toolWindowManager.getToolWindow("PhraseDroid")

        return toolWindow
    }

    private fun setupToolbarContent(project: Project, toolWindowContent: ToolwindowContent, name: String) {
        val toolWindow = getToolWindow(project) ?: return

        val content = ContentFactory
            .getInstance()
            .createContent(toolWindowContent.contentPanel, name, false)

        toolWindow.contentManager.removeAllContents(false)
        toolWindow.contentManager.addContent(content)
        toolWindow.show()
    }

    private fun closeToolwindow(project: Project) {
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val toolWindow = toolWindowManager.getToolWindow("PhraseDroid")

        toolWindow?.contentManager?.removeAllContents(false)
        toolWindow?.hide(null)
    }

    private fun showCreateSettingsJsonDialog(project: Project) {
        val confirm = Messages.showOkCancelDialog(
            project,
            "Settings file not found. Want to create the JSON file with the settings template?",
            "Settings File",
            "Create File",
            "Cancel",
            Messages.getWarningIcon()
        )

        if (confirm == Messages.OK) {
            createSettingsFile(project)
        } else {
            NotificationUtil.error(project, "Failed to load API configuration. Check if JSON settings file exists.")
        }
    }

    private fun createSettingsFile(project: Project) {
        val createdFile = createFileAtProjectRoot(project)

        if (createdFile != null) {
            NotificationUtil.success(project, "Settings file created successfully!")
        } else {
            NotificationUtil.error(project, "Failed to create file settings.json.")
        }
    }

    private fun createFileAtProjectRoot(project: Project): VirtualFile? {
        return ApplicationManager.getApplication().runWriteAction<VirtualFile> {
            runCatching {
                return@runWriteAction FileUtil.createSettingsFileFromTemplate(project)
            }

            return@runWriteAction null
        }
    }
}