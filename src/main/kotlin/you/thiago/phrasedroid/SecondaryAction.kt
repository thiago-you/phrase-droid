package you.thiago.phrasedroid

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.findDocument
import you.thiago.phrasedroid.state.MyState

class SecondaryAction: AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val content = buildStringContent(MyState().getInstance().state.content)

        val filePath = project.basePath + "/strings.xml"

        WriteCommandAction.runWriteCommandAction(project) {
            val file = VfsUtil.findFileByIoFile(java.io.File(filePath), true)

            if (file != null && file.isValid) {
                file.findDocument()?.also { document ->
                    document.insertString(document.textLength, content)
                }
            }
        }
    }

    private fun buildStringContent(content: String): String {
        return "\n<string name=\"name2\">$content</string>".format(content)
    }
}