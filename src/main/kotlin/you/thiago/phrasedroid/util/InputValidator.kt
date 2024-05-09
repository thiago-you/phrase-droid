package you.thiago.phrasedroid.util

object InputValidator {
    fun validate(input: String?): String {
        if (input.isNullOrBlank()) {
            return ""
        }
        if (input.contains(" ")) {
            return ""
        }

        return input.uppercase()
    }
}