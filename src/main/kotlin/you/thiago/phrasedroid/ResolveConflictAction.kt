package you.thiago.phrasedroid

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.PerformInBackgroundOption
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageDialogBuilder
import com.intellij.openapi.ui.Messages
import kotlinx.coroutines.runBlocking
import you.thiago.phrasedroid.data.ApiSettings
import you.thiago.phrasedroid.state.AppState
import you.thiago.phrasedroid.util.FileUtil
import you.thiago.phrasedroid.util.JsonUtil
import you.thiago.phrasedroid.util.NotificationUtil

class ResolveConflictAction: AnAction() {

    private val settingsFilePath by lazy { AppState().getInstance().state.settingsFilePath }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val confirmation = confirmAction(project)

        if (!confirmation) {
            return
        }

        val task = object : Task.Backgroundable(project, "PhraseDroid", true, PerformInBackgroundOption.ALWAYS_BACKGROUND) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                indicator.text = "Resolving conflicts..."

                resolveConflicts(e, ::actionResponse)
            }
        }

        ProgressManager.getInstance().run(task)
    }

    private fun confirmAction(project: Project): Boolean {
        val icon = Messages.getQuestionIcon()
        val title = "Resolve Conflicts"
        val message = "Are you sure you want to auto resolve GIT conflicts? Exceptions may occur."

        val result = MessageDialogBuilder.yesNo(title, message, icon).ask(project)

        return result
    }

    private fun loadSettings(project: Project): ApiSettings? {
        val file = if (settingsFilePath.isNotEmpty()) {
            FileUtil.getVirtualFileByPath(settingsFilePath)
        } else {
            FileUtil.getVirtualFileByProjectRelativePath(project)
        }

        return file?.let { JsonUtil.readConfig(it) }
    }

    private fun resolveConflicts(e: AnActionEvent, onComplete: (AnActionEvent, Boolean) -> Unit) {
        val project = e.project ?: return

        var success = false

        try {
            ApplicationManager.getApplication().invokeLater {
                success = removeConflicts(e)
            }
        } catch(e: Exception) {
            NotificationUtil.warning(project, "There was an unexpected error.", "PhraseDroid: Exception")
        }

        runBlocking {
            onComplete(e, success)
        }
    }

    private fun removeConflicts(e: AnActionEvent): Boolean {
        val project = e.project ?: return false

        return true
    }

    private fun actionResponse(e: AnActionEvent, success: Boolean) {
        val project = e.project ?: return

        if (success) {
            NotificationUtil.success(project, "Conflict resolve successfully!")
        } else {
            NotificationUtil.error(project, "There was an unexpected error.")
        }
    }
}