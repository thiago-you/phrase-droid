package you.thiago.phrasedroid

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.findDocument
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import you.thiago.phrasedroid.data.ResourceFile
import you.thiago.phrasedroid.state.MyState
import you.thiago.phrasedroid.ui.TranslationsContent
import you.thiago.phrasedroid.util.FileMapper

class WriteTranslationsAction: AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        getResourceFilesList().also { list ->
            displayTranslations(project, list)
        }
    }

    private fun getResourceFilesList(): List<ResourceFile> {
        return FileMapper.getResourceFilesList(MyState().getInstance().state.translations)
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
                document.insertString(lastLineStartOffset, resource.content)
            }
        }
    }

    private fun displayTranslations(project: Project, resourceFiles: List<ResourceFile>) {
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val toolWindow = toolWindowManager.getToolWindow("PhraseDroid") ?: return

        val loadingContent = TranslationsContent(resourceFiles)

        val content = ContentFactory
            .getInstance()
            .createContent(loadingContent.contentPanel, "PhraseDroid", false)

        toolWindow.contentManager.removeAllContents(false)
        toolWindow.contentManager.addContent(content)
        toolWindow.show()
    }
}
