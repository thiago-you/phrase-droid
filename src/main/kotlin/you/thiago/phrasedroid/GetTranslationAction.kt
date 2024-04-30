package you.thiago.phrasedroid

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
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
import you.thiago.phrasedroid.state.MyState
import you.thiago.phrasedroid.toolbar.LoadingContent
import you.thiago.phrasedroid.toolbar.ToolwindowContent
import you.thiago.phrasedroid.toolbar.TranslationsContent
import you.thiago.phrasedroid.util.FileLoader
import you.thiago.phrasedroid.util.FileMapper
import you.thiago.phrasedroid.util.JsonUtil
import java.nio.file.Paths

class GetTranslationAction: AnAction() {

    private val settingsFilePath by lazy { MyState().getInstance().state.settingsFilePath }

    companion object {
        private const val SETTINGS_RELATIVE_PATH = "phrase-droid-settings.json"
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val apiSettings = getJsonData(e) ?: return showErrorDialog(project, "Failed to load API settings.")

        val input = requireTranslationKey(project)

        displayLoadingWindow(project)

        MyState().getInstance().state.translationKey = input

        fetchApiData(e, apiSettings)
    }

    private fun requireTranslationKey(project: Project): String {
        val input = Messages.showInputDialog(
            project,
            "Setup translation key to get data on API:",
            "Translation Key",
            Messages.getQuestionIcon()
        )

        if (input.isNullOrBlank()) {
            showErrorDialog(project, "Input cannot be empty")
            return requireTranslationKey(project)
        }

        return input
    }

    private fun getJsonData(e: AnActionEvent): ApiSettings? {
        if (settingsFilePath.isBlank()) {
            return getVirtualFileByProjectRelativePath(e.project!!)
        }

        val file = FileLoader.getVirtualFileByPath(settingsFilePath)

        if (file != null) {
            return JsonUtil.readConfig(file)
        }

        return null
    }

    private fun getVirtualFileByProjectRelativePath(project: Project): ApiSettings? {
        val basePath = project.basePath ?: return null

        val absolutePath = Paths.get(basePath, SETTINGS_RELATIVE_PATH).toString()

        val file = FileLoader.getVirtualFileByPath(absolutePath)

        if (file != null) {
            return JsonUtil.readConfig(file)
        }

        return null
    }

    private fun fetchApiData(e: AnActionEvent, apiSettings: ApiSettings) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val list = Api.fetchTranslations(apiSettings)
            setupTranslationsWindow(e, list)
        }
    }

    private fun setupTranslationsWindow(e: AnActionEvent, translations: List<Translation>) {
        ApplicationManager.getApplication().invokeLater {
            if (translations.isNotEmpty()) {
                displayTranslations(e, FileMapper.getResourceFilesList(translations))
            } else {
                showErrorDialog(e.project, "Translations not found")
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

    private fun showErrorDialog(project: Project?, message: String) {
        Messages.showMessageDialog(
            project,
            message,
            "Error",
            Messages.getErrorIcon()
        )
    }
}