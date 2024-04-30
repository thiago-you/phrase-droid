package you.thiago.phrasedroid.toolbar

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.JPanel

class SidebarWindow: ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val content = ContentFactory
            .getInstance()
            .createContent(JPanel(), "", false)

        toolWindow.contentManager.addContent(content)
    }
}