package you.thiago.phrasedroid

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.findDocument
import you.thiago.phrasedroid.data.ResourceFile
import you.thiago.phrasedroid.state.MyState

class WriteTranslationsAction: AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        MyState().getInstance().state.translations.also { list ->
            writeResources(project, list)
        }
    }

    private fun loadFile(project: Project, filePath: String): VirtualFile? {
        return VfsUtil.findFileByIoFile(java.io.File(project.basePath + filePath), true)
    }

    private fun writeResources(project: Project, resourceFiles: List<ResourceFile>) {
        WriteCommandAction.runWriteCommandAction(project) {
            resourceFiles.forEach { resource ->
                val file = loadFile(project, resource.filePath)

                if (file != null && file.isValid) {
                    writeResourceFiles(file, resource)
                }
            }
        }
    }

    private fun writeResourceFiles(file: VirtualFile, resource: ResourceFile) {
        file.findDocument()?.also { document ->
            if (!document.text.contains(resource.name)) {
                val lastLineStartOffset = document.getLineStartOffset(document.lineCount - 1)
                document.insertString(lastLineStartOffset, "\t${resource.content}\n")
            } else {
                if (MyState().getInstance().state.isUpdateSelected) {
                    val content = document.text

                    val regex = "<string name=\"${resource.name}\">[\\s\\S]*?</string>".toRegex()

                    val updatedContent = regex.replace(content) { resource.content }

                    document.setText(updatedContent)
                }
            }
        }
    }
}
