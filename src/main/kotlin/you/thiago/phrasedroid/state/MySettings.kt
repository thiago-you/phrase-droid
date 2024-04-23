package you.thiago.phrasedroid.state

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil
import org.jetbrains.annotations.NotNull


@Service
@State(
    name = "you.thiago.phrasedroid.state.MySettings",
    storages = [Storage("PhrasePluginSettings.xml")]
)
class MySettings : SimplePersistentStateComponent<MyState>(MyState())

class MyState : BaseState() {
    var userId: String = "John Q. Public"
    var ideaStatus: Boolean = false

    fun getInstance(): MySettings {
        return ApplicationManager.getApplication().getService(MySettings::class.java)
    }

    fun getState(): MySettings {
        return getInstance()
    }

    fun loadState(@NotNull state: MySettings) {
        XmlSerializerUtil.copyBean(state, this)
    }
}