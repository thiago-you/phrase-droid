package you.thiago.phrasedroid

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class MyDemoAction: AnAction() {

    override fun update(e: AnActionEvent) {
        super.update(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        Messages.showMessageDialog(
            e.project,
            "Great! You just created your first action!",
            "My First Action",
            Messages.getInformationIcon());

    }

    // Override getActionUpdateThread() when you target 2022.3 or later!
}