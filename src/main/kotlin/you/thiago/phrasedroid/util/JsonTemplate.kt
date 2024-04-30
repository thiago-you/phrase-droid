package you.thiago.phrasedroid.util

object JsonTemplate {
    fun get(): String {
        runCatching {
            return javaClass.classLoader.getResource("assets/settings-template.json")
                ?.readText()
                ?.trim()
                ?: String()
        }

        return String()
    }
}