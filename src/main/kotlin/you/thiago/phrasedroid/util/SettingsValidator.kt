package you.thiago.phrasedroid.util

import you.thiago.phrasedroid.data.ApiSettings

object SettingsValidator {
    private const val MSG_ID_NOT_SET = "Phrase API \"ID\" not set on settings."
    private const val MSG_KEY_NOT_SET = "Phrase API \"KEY\" not set on settings."
    private const val MSG_ID_INVALID = "Phrase API \"KEY\" is invalid on settings."
    private const val MSG_KEY_INVALID = "Phrase API \"KEY\" is invalid on settings."

    private const val DEFAULT_ID_VALUE = "required: API project ID"
    private const val DEFAULT_KEY_VALUE = "required: API project auth key"

    fun validate(apiSettings: ApiSettings): String? {
        if (apiSettings.id.trim().isEmpty()) {
            return MSG_ID_NOT_SET
        }
        if (apiSettings.key.trim().isEmpty()) {
            return MSG_KEY_NOT_SET
        }
        if (apiSettings.id.contains(DEFAULT_ID_VALUE, ignoreCase = true)) {
            return MSG_ID_INVALID
        }
        if (apiSettings.key.contains(DEFAULT_KEY_VALUE, ignoreCase = true)) {
            return MSG_KEY_INVALID
        }
        if (apiSettings.id.contains(" ") || apiSettings.id.length < 32) {
            return MSG_ID_INVALID
        }
        if (apiSettings.key.contains(" ") || apiSettings.key.length < 32) {
            return MSG_KEY_INVALID
        }

        return null
    }
}