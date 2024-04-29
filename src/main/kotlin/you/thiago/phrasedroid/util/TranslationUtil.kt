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
            }
        }
    }

    fun escapeSingleQuote(content: String): String {
        var escapedContent = content.replace("'", "\\\'")
        escapedContent = escapedContent.replace("\\", "\\\\\\\\")
        return escapedContent
    }

    private fun escapeHtml(content: String): String {
        var escapedContent = content.replace("\n", " ")
        escapedContent = escapedContent.replace("'", "\\\'")
        escapedContent = escapedContent.replace("\\", "\\\\\\\\")
        escapedContent = escapedContent.replace("&", "\\&amp;")
        return "<![CDATA[$escapedContent]]>"
    }

    private fun getTranslationContent(name: String, content: String): String {
        return "\t<string name=\"%s\">%s</string>\n".format(name, content)
    }
}