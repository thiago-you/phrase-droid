package you.thiago.phrasedroid

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import you.thiago.phrasedroid.data.ApiSettings
import you.thiago.phrasedroid.state.MyState
import you.thiago.phrasedroid.util.FileLoader
import you.thiago.phrasedroid.util.JsonUtil


class MyDemoAction: AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val apiSettings = getJsonData()

        if (apiSettings != null) {
            Messages.showMessageDialog(
                e.project,
                "API Settings: $apiSettings",
                "API Settings",
                Messages.getInformationIcon()
            )
        } else {
            Messages.showMessageDialog(
                e.project,
                "Failed to read API settings from file",
                "Error",
                Messages.getErrorIcon()
            )
        }
    }

    private fun getJsonData(): ApiSettings? {
        val filePath = MyState().getInstance().state.settingsFilePath

        val file = FileLoader.getVirtualFileByPath(filePath)

        if (file != null) {
            return JsonUtil.readConfig(file)
        }

        return null
    }
}