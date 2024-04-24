package you.thiago.phrasedroid.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class SidebarWindow: ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val sidebarWindowContent = SidebarWindowContent(toolWindow)

        val content = ContentFactory
            .getInstance()
            .createContent(sidebarWindowContent.contentPanel, "PhraseDroid", false)

        toolWindow.contentManager.addContent(content)
    }

    fun refreshContent(toolWindow: ToolWindow) {
        val sidebarWindowContent = SidebarWindowContent(toolWindow)

        val content = ContentFactory
            .getInstance()
            .createContent(sidebarWindowContent.contentPanel, "PhraseDroid", false)

        toolWindow.contentManager.addContent(content)
    }
}