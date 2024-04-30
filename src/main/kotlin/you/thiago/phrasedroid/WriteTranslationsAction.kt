package you.thiago.phrasedroid

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.findDocument
import you.thiago.phrasedroid.data.ResourceFile
import you.thiago.phrasedroid.state.FlashState

class WriteTranslationsAction: AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        WriteCommandAction.runWriteCommandAction(project) {
            writeResources(project, FlashState.translations)
        }

        showSuccessMessage(project)
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
                addResourceIntoFile(document, resource)
            } else if (FlashState.isUpdateSelected) {
                updateResourceIntoFile(document, resource, content)
            }
        }
    }

    private fun addResourceIntoFile(document: Document, resource: ResourceFile) {
        val lastLineStartOffset = document.getLineStartOffset(document.lineCount - 1)
        document.insertString(lastLineStartOffset, "\t${resource.content}\n")
    }

    private fun updateResourceIntoFile(document: Document, resource: ResourceFile, content: String) {
        val regex = "<string name=\"${resource.name}\">[\\s\\S]*?</string>".toRegex()
        val updatedContent = regex.replace(content) { resource.content }
        document.setText(updatedContent)
    }

    private fun showSuccessMessage(project: Project) {
        Messages.showMessageDialog(
            project,
            "Translations added successfully!",
            "Done!",
            Messages.getInformationIcon()
        )
    }
}
