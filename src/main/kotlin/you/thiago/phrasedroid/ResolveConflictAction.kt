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
import com.intellij.openapi.ui.MessageDialogBuilder
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.findDocument
import you.thiago.phrasedroid.util.Constants
import you.thiago.phrasedroid.util.NotificationUtil

class ResolveConflictAction: AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val confirmation = confirmAction(project)

        if (!confirmation) {
            return
        }

        val task = object : Task.Backgroundable(project, "PhraseDroid", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                indicator.text = "Resolving conflicts..."

                resolveConflicts(e)
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

    private fun resolveConflicts(e: AnActionEvent) {
        val project = e.project ?: return

        try {
            executeAction(e)
        } catch(e: Exception) {
            NotificationUtil.warning(project, "There was an unexpected error.", "PhraseDroid: Exception")
        }
    }

    private fun executeAction(e: AnActionEvent) {
        val project = e.project ?: return

        WriteCommandAction.runWriteCommandAction(project) {
            runCatching {
                writeResources(project)
            }.onSuccess {
                NotificationUtil.success(project, "Conflict resolve successfully!")
            }.onFailure {
                NotificationUtil.error(project, "There was an unexpected error.")
            }
        }
    }

    private fun writeResources(project: Project) {
        getFilesPath()
            .mapNotNull { loadFile(project, it) }
            .filter { it.isValid }
            .forEach { removeConflictsFromFile(it) }
    }

    private fun loadFile(project: Project, filePath: String): VirtualFile? {
        return VfsUtil.findFileByIoFile(java.io.File(project.basePath + filePath), true)
    }

    private fun removeConflictsFromFile(file: VirtualFile) {
        file.findDocument()?.also { document ->
            val content = document.text.takeIf { it.isNotBlank() } ?: return

            if (content.contains(Regex("<<<<<<<|=======|>>>>>>>"))) {
                removeConflictMarks(removeDuplicatedLines(content)).also { updatedContent ->
                    document.setText(updatedContent)
                }
            }
        }
    }

    private fun removeDuplicatedLines(content: String): String {
        val conflictRegex = Regex("(?s)(<<<<<<<.*?=======)(.*?)(>>>>>>>)")

        val updatedContent = conflictRegex.replace(content) { matchResult ->
            val (firstPart, secondPart, endMarker) = matchResult.groupValues.drop(1)

            val firstLines = firstPart.lines().filter { it.isNotBlank() }
            val secondLines = secondPart.lines().filter { it.isNotBlank() }

            (firstLines + secondLines).distinct().joinToString("\n") + "\n" + endMarker
        }

        return updatedContent
    }

    private fun removeConflictMarks(content: String): String {
        return content
            .lineSequence()
            .filterNot { it.contains("<<<<<<<") || it.contains("=======") || it.contains(">>>>>>>") }
            .joinToString("\n")
    }

    private fun getFilesPath(): List<String> = Constants.filesSuffix.map { suffix ->
        "/app/src/main/res/values$suffix/strings.xml"
    }
}