package you.thiago.phrasedroid

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.findDocument
import you.thiago.phrasedroid.data.ResourceFile
import you.thiago.phrasedroid.state.FlashState
import you.thiago.phrasedroid.util.NotificationUtil

class WriteTranslationsAction: AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        WriteCommandAction.runWriteCommandAction(project) {
            runCatching {
                writeResources(project, FlashState.translations)
            }.onSuccess {
                NotificationUtil.success(project, "Translations added successfully!")
            }.onFailure {
                NotificationUtil.error(project, "Error adding translations: ${it.message ?: "Unknown error"}")
            }
        }
    }

    private fun writeResources(project: Project, resourceFiles: List<ResourceFile>) {
        resourceFiles.forEach { resource ->
            loadFile(project, resource.filePath)
                ?.takeIf { it.isValid }
                ?.let { writeResourceFiles(it, resource) }
        }
    }

    private fun loadFile(project: Project, filePath: String): VirtualFile? {
        return VfsUtil.findFileByIoFile(java.io.File(project.basePath + filePath), true)
    }

    private fun writeResourceFiles(file: VirtualFile, resource: ResourceFile) {
        file.findDocument()?.also { document ->
            val content = document.text

            if (!content.contains(resource.name)) {
                if (resource.translation.isNotBlank()) {
                    addResourceIntoFile(document, resource)
                }
            } else if (FlashState.isAllowUpdateSelected) {
                updateResourceIntoFile(document, resource, content)
            }
        }
    }

    private fun addResourceIntoFile(document: Document, resource: ResourceFile) {
        val lineCount = document.lineCount
        var lastNonBlankLine = lineCount - 1

        for (i in lineCount - 1 downTo 0) {
            val lineText = TextRange(document.getLineStartOffset(i), document.getLineEndOffset(i))
                .let { document.getText(it) }
                .trim()
                .takeIf { it.isNotEmpty() }

            if (!lineText.isNullOrBlank()) {
                lastNonBlankLine = i
                break
            }
        }

        val lastLineStartOffset = document.getLineStartOffset(lastNonBlankLine)

        document.insertString(lastLineStartOffset, "\t${resource.content}\n")
    }

    private fun updateResourceIntoFile(document: Document, resource: ResourceFile, docContent: String) {
        val regex = "<string name=\"${resource.name}\">[\\s\\S]*?</string>".toRegex()
        val updatedContent = regex.replace(docContent) { resource.content }
        document.setText(updatedContent)
    }
}
