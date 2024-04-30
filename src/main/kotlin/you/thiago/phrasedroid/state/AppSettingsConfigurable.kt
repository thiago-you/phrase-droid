package you.thiago.phrasedroid.state

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.Nullable
import javax.swing.JComponent

/**
 * Provides controller functionality for application settings.
 */
internal class AppSettingsConfigurable : Configurable {

    private var appSettings: AppSettingsComponent? = null

    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return "PhraseDroid"
    }

    @Nullable
    override fun createComponent(): JComponent {
        appSettings = AppSettingsComponent()
        return appSettings!!.panel
    }

    override fun isModified(): Boolean {
        val settings: AppState = AppState().getInstance().state
        val modified = appSettings?.settingsFilePath != settings.settingsFilePath

        return modified
    }

    override fun apply() {
        val settings: AppState = AppState().getInstance().state
        settings.settingsFilePath = appSettings?.settingsFilePath ?: String()
    }

    override fun reset() {
        val settings: AppState = AppState().getInstance().state
        appSettings?.settingsFilePath = settings.settingsFilePath
    }

    override fun disposeUIResources() {
        appSettings = null
    }
}