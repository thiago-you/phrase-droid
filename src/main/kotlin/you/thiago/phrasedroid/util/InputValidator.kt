package you.thiago.phrasedroid.util

object InputValidator {
    fun validate(input: String?): String {
        if (input.isNullOrBlank()) {
            return ""
        }
        if (input.contains(" ")) {
            return ""
        }
        if (input.length < 3) {
            return ""
        }

        return input.replace("-", "_").uppercase()
    }
}