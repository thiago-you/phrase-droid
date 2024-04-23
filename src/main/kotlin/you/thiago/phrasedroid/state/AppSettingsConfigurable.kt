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

    // A default constructor with no arguments is required because this implementation
    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return "SDK: Application Settings Example"
    }

    fun preferredFocusedComponent(): JComponent {
        return mySettingsComponent!!.preferredFocusedComponent
    }

    @Nullable
    override fun createComponent(): JComponent {
        mySettingsComponent = AppSettingsComponent()
        return mySettingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val settings: MyState = MyState().getInstance().state
        var modified = mySettingsComponent!!.userNameText != settings.userId
        modified = modified or (mySettingsComponent!!.ideaUserStatus != settings.ideaStatus)
        return modified
    }

    override fun apply() {
        val settings: MyState = MyState().getInstance().state
        settings.userId = mySettingsComponent!!.userNameText!!
        settings.ideaStatus = mySettingsComponent!!.ideaUserStatus
    }

    override fun reset() {
        val settings: MyState = MyState().getInstance().state
        mySettingsComponent!!.userNameText = settings.userId
        mySettingsComponent!!.ideaUserStatus = settings.ideaStatus
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}