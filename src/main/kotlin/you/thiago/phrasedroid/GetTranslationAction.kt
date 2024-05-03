package you.thiago.phrasedroid

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
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
import you.thiago.phrasedroid.enums.SettingsEnum
import you.thiago.phrasedroid.network.Api
import you.thiago.phrasedroid.state.AppState
import you.thiago.phrasedroid.state.FlashState
import you.thiago.phrasedroid.toolbar.LoadingContent
import you.thiago.phrasedroid.toolbar.ToolwindowContent
import you.thiago.phrasedroid.toolbar.TranslationsContent
import you.thiago.phrasedroid.util.FileLoader
import you.thiago.phrasedroid.util.FileMapper
import you.thiago.phrasedroid.util.JsonUtil
import java.nio.file.Paths

class GetTranslationAction: AnAction() {

    private val settingsFilePath by lazy { AppState().getInstance().state.settingsFilePath }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val apiSettings = getJsonData(e) ?: return showErrorDialog(project, "Failed to load API configuration. Check if JSON settings file is available.")

        val input = requireTranslationKey(project)

        if (!input.isNullOrBlank()) {
            displayLoadingWindow(project)
            fetchApiData(e, apiSettings)
        }
    }

    private fun requireTranslationKey(project: Project): String? {
        val input = Messages.showInputDialog(
            project,
            "Translation KEY:",
            "PhraseDroid",
            Messages.getInformationIcon()
        )

        FlashState.translationKey = input ?: ""

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

        val absolutePath = Paths.get(basePath, SettingsEnum.RELATIVE_PATH.value).toString()

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
        val project = e.project ?: return

        ApplicationManager.getApplication().invokeLater {
            if (translations.isNotEmpty()) {
                displayTranslations(e, FileMapper.getResourceFilesList(translations))
            } else {
                showErrorDialog(project, "Translations not found for this KEY.", "Not Found")
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

    private fun showErrorDialog(project: Project, message: String, title: String? = null) {
        Notifications.Bus.notify(
            Notification(
                project.name,
                title ?: "PhraseDroid: error!",
                message,
                NotificationType.ERROR
            )
        )
    }

    private fun closeToolwindow(project: Project) {
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val toolWindow = toolWindowManager.getToolWindow("PhraseDroid")

        toolWindow?.contentManager?.removeAllContents(false)
        toolWindow?.hide(null)
    }
}