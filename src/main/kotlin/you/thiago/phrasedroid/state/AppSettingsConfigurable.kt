package you.thiago.phrasedroid.state

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.Nullable
import javax.swing.JComponent


/**
 * Provides controller functionality for application settings.
 */
internal class AppSettingsConfigurable : Configurable {
    private var mySettingsComponent: AppSettingsComponent? = null

    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return "PhraseDroid Settings"
    }

    fun preferredFocusedComponent(): JComponent {
        return mySettingsComponent?.preferredFocusedComponent!!
    }

    @Nullable
    override fun createComponent(): JComponent {
        mySettingsComponent = AppSettingsComponent()
        return mySettingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val settings: MyState = MyState().getInstance().state
        val modified = mySettingsComponent?.settingsFilePath != settings.settingsFilePath

        return modified
    }

    override fun apply() {
        val settings: MyState = MyState().getInstance().state
        settings.settingsFilePath = mySettingsComponent?.settingsFilePath ?: String()
    }

    override fun reset() {
        val settings: MyState = MyState().getInstance().state
        mySettingsComponent?.settingsFilePath = settings.settingsFilePath
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}