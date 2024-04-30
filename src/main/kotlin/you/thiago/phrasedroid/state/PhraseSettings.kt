package you.thiago.phrasedroid.state

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*
import you.thiago.phrasedroid.data.ResourceFile

@Service
@State(
    name = "you.thiago.phrasedroid.state.PhraseSettings",
    storages = [Storage("PhrasePluginSettings.xml")]
)
class PhraseSettings : SimplePersistentStateComponent<AppState>(AppState())

class AppState : BaseState() {
    var settingsFilePath: String = ""
    var translationKey: String = ""

    var isUpdateSelected: Boolean = false

    var translations: List<ResourceFile> = emptyList()

    fun getInstance(): PhraseSettings {
        return ApplicationManager.getApplication().getService(PhraseSettings::class.java)
    }
}