package you.thiago.phrasedroid.util

import you.thiago.phrasedroid.data.ResourceFile

object TranslationUtil {

    fun escapeTranslations(translations: List<ResourceFile>): List<ResourceFile> {
        return translations.map { item ->
            return@map item.apply {
                translation = escapeHtml(translation)
                content = getTranslationContent(name, translation)
            }
        }
    }

    fun removeTranslationsEscape(translations: List<ResourceFile>): List<ResourceFile> {
        return translations.map { item ->
            return@map item.apply {
                translation = sourceTranslation
                content = getTranslationContent(name, translation)
            }
        }
    }

    fun escapeSingleQuote(content: String): String {
        return content.replace("'", """\'""")
    }

    private fun escapeHtml(content: String): String {
        val escapedContent = content.replace("\n", " ")
            .replace("""\\\\'""", "'")
            .replace("""\\\'""", "'")
            .replace("""\\'""", "'")
            .replace("""\'""", "'")
            .replace("""\""", """\\""")
            .replace("'", """\'""")
            .replace("&", "&amp;")

        return "<![CDATA[$escapedContent]]>"
    }

    private fun getTranslationContent(name: String, content: String): String {
        return "<string name=\"%s\">%s</string>".format(name, content)
    }
}