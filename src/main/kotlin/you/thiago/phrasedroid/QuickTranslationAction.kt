package you.thiago.phrasedroid

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.util.ProgressIndicatorBase
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vcs.changes.RunnableBackgroundableWrapper
import com.intellij.openapi.vfs.VirtualFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import you.thiago.phrasedroid.data.ApiSettings
import you.thiago.phrasedroid.data.Translation
import you.thiago.phrasedroid.network.Api
import you.thiago.phrasedroid.state.AppState
import you.thiago.phrasedroid.state.FlashState
import you.thiago.phrasedroid.util.*

class QuickTranslationAction: AnAction() {

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
            ProgressManager.getInstance().runProcessWithProgressAsynchronously(
                RunnableBackgroundableWrapper(project, "Loading...") {
                    fetchApiData(e, apiSettings)
                },
                getProgressIndicator()
            )
        }
    }

    private fun getProgressIndicator(): ProgressIndicator {
        val indicator = ProgressManager.getInstance().progressIndicator ?: ProgressIndicatorBase()

        indicator.isIndeterminate = true
        indicator.text = "Fetching translations..."

        return indicator
    }

    private fun requireTranslationKey(project: Project, invalidKey: Boolean = false): String? {
        val icon = Messages.getInformationIcon().takeIf { !invalidKey } ?: Messages.getWarningIcon()
        val message = "[Resumed] Translation KEY:".takeIf { !invalidKey } ?: "[Resumed] Translation Key (invalid):"

        val input = Messages.showInputDialog(project, message, "PhraseDroid [R]", icon)

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
            writeTranslations(e, list)
        }
    }

    private fun writeTranslations(e: AnActionEvent, translations: List<Translation>) {
        val project = e.project ?: return

        ApplicationManager.getApplication().invokeLater {
            if (translations.isNotEmpty()) {
                executeWriteTranslationActions(e, translations)
            } else {
                NotificationUtil.warning(project, "Translations not found for this KEY.", "PhraseDroid: Not Found")
            }
        }
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

    private fun executeWriteTranslationActions(e: AnActionEvent, translations: List<Translation>) {
        ApplicationManager.getApplication().invokeLater {
            FlashState.translations = ResFileMapper.getResourceFilesList(translations)
            FlashState.isAllowUpdateSelected = true

            ActionUtil.invokeAction(WriteTranslationsAction(), e.dataContext, e.place, null, null)
        }
    }
}