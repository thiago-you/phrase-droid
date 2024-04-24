package you.thiago.phrasedroid

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import you.thiago.phrasedroid.data.ApiSettings
import you.thiago.phrasedroid.network.Api
import you.thiago.phrasedroid.state.MyState
import you.thiago.phrasedroid.util.FileLoader
import you.thiago.phrasedroid.util.JsonUtil
import java.nio.file.Paths


class MyDemoAction: AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val apiSettings = getJsonData(e)

        if (apiSettings != null) {
            fetchApiData(e, apiSettings)

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

    private fun getJsonData(e: AnActionEvent): ApiSettings? {
        val filePath = MyState().getInstance().state.settingsFilePath

        if (filePath.isBlank()) {
            return getVirtualFileByProjectRelativePath(e.project!!)
        }

        val file = FileLoader.getVirtualFileByPath(filePath)

        if (file != null) {
            return JsonUtil.readConfig(file)
        }

        return null
    }

    private fun getVirtualFileByProjectRelativePath(project: Project): ApiSettings? {
        // Get the base directory of the project
        val basePath = project.basePath ?: return null

        // Construct the absolute path
        val absolutePath = Paths.get(basePath, "phrase-droid-settings.json").toString()

        val file = FileLoader.getVirtualFileByPath(absolutePath)

        if (file != null) {
            return JsonUtil.readConfig(file)
        }

        return null
    }

    private fun fetchApiData(e: AnActionEvent, apiSettings: ApiSettings) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val response = Api().getTranslationKeyId(apiSettings)

            if (!response.isNullOrBlank()) {
                val translationKey = JsonUtil.readTranslationKeyInfo(response)
                val data = Api().getTranslations(apiSettings, translationKey?.id ?: String())

                if (!data.isNullOrBlank()) {
                    val list = JsonUtil.readTranslations(data)

                    if (list?.isNotEmpty() == true) {
                        ApplicationManager.getApplication().invokeLater {
                            Messages.showMessageDialog(
                                e.project,
                                "API response: ${list.size}",
                                "API Settings",
                                Messages.getInformationIcon()
                            )
                        }
                    }
                }
            }

            ApplicationManager.getApplication().invokeLater {
                Messages.showMessageDialog(
                    e.project,
                    "API response: $response",
                    "API Settings",
                    Messages.getInformationIcon()
                )
            }
        }
    }
}