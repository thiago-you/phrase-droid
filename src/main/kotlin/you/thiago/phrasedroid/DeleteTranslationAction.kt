package you.thiago.phrasedroid

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.findDocument
import you.thiago.phrasedroid.util.Constants
import you.thiago.phrasedroid.util.NotificationUtil

class DeleteTranslationAction: AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val input = requireTranslationKey(project)

        if (input.isNullOrBlank()) {
            return
        }

        val task = object : Task.Backgroundable(project, "PhraseDroid", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                indicator.text = "Deleting key..."

                deleteKey(e, input)
            }
        }

        ProgressManager.getInstance().run(task)
    }

    private fun requireTranslationKey(project: Project): String? {
        val icon = Messages.getInformationIcon()
        val message = "Translation KEY to Delete:"

        val input = Messages.showInputDialog(project, message, "PhraseDroid", icon)

        return input
    }

    private fun deleteKey(e: AnActionEvent, translationKey: String) {
        val project = e.project ?: return

        try {
            executeAction(e, translationKey)
        } catch(e: Exception) {
            NotificationUtil.warning(project, "There was an unexpected error.", "PhraseDroid: Exception")
        }
    }

    private fun executeAction(e: AnActionEvent, translationKey: String) {
        val project = e.project ?: return

        WriteCommandAction.runWriteCommandAction(project) {
            runCatching {
                writeResources(project, translationKey)
            }.onSuccess {
                NotificationUtil.success(project, "Key removed!")
            }.onFailure {
                NotificationUtil.error(project, "There was an unexpected error.")
            }
        }
    }

    private fun writeResources(project: Project, translationKey: String) {
        getFilesPath()
            .mapNotNull { loadFile(project, it) }
            .filter { it.isValid }
            .forEach { removeTranslationKeyFromFile(it, translationKey) }
    }

    private fun loadFile(project: Project, filePath: String): VirtualFile? {
        return VfsUtil.findFileByIoFile(java.io.File(project.basePath + filePath), true)
    }

    private fun removeTranslationKeyFromFile(file: VirtualFile, translationKey: String) {
        file.findDocument()?.also { document ->
            val content = document.text.takeIf { it.isNotBlank() } ?: return

            val updatedContent = removeKey(content, translationKey)

            if (content != updatedContent) {
                document.setText(updatedContent)
            }
        }
    }

    private fun removeKey(content: String, translationKey: String): String {
        val regex = Regex(
            """\s*<string\s+name="$translationKey">.*?</string>\s*?""", RegexOption.DOT_MATCHES_ALL
        )

        return content.replace(regex, "")
    }

    private fun getFilesPath(): List<String> = Constants.filesSuffix.map { suffix ->
        "/app/src/main/res/values$suffix/strings.xml"
    }
}