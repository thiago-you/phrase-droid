package you.thiago.phrasedroid.util

import you.thiago.phrasedroid.data.Locale
import you.thiago.phrasedroid.data.ResourceFile
import you.thiago.phrasedroid.data.Translation

object FileMapper {

    fun getResourceFilesList(translations: List<Translation>): List<ResourceFile> {
        val sortedList = translations
            .map { mapToResourceFile(it) }
            .distinctBy { it.filename }
            .sortedBy { it.locale }
            .toMutableList()

        val firstItem  = sortedList.find { it.locale == "pt-BR" }

        firstItem?.also {
            val firstItemIndex = sortedList.indexOf(firstItem)

            if (firstItemIndex != -1) {
                sortedList.removeAt(firstItemIndex)
                sortedList.add(0, firstItem)
            }
        }

        return sortedList
    }

    private fun mapToResourceFile(translation: Translation): ResourceFile {
        val suffix = getPathSuffix(translation.locale)

        val filename = "values$suffix/strings.xml"
        val filePath = "/app/src/main/res/$filename"

        val translationName = translation.key.name

        val content = TranslationUtil.escapeSingleQuote(translation.content)

        val translationContent = getTranslationContent(translationName, content)

        return ResourceFile(
            filename = filename,
            filePath = filePath,
            name = translationName,
            locale = translation.locale.code,
            translation = content,
            sourceTranslation = content,
            content = translationContent
        )
    }

    private fun getPathSuffix(locale: Locale): String {
        return when (locale.code) {
            "de-DE" -> "-de-rDE"
            "en-GB" -> "-en-rGB"
            "en-CA" -> "-en-rCA"
            "es-AR" -> "-es-rAR"
            "es-CL" -> "-es-rCL"
            "es-CO" -> "-es-rCO"
            "es-MX" -> "-es-rMX"
            "es-ES" -> "-es-rES"
            "fr-FR" -> "-fr-rFR"
            "fr-CA" -> "-fr-rCA"
            "it-IT" -> "-it-rIT"
            "nl-NL" -> "-nl"
            "pt-PT" -> "-pt-rPT"
            "pt-BR" -> "-pt"
            "nl" -> "-nl"
            "es" -> "-es"
            "pt" -> "-pt"
            "fr" -> "-fr-rFR"
            else -> ""
        }
    }

    private fun getTranslationContent(name: String, content: String): String {
        return "<string name=\"%s\">%s</string>".format(name, content)
    }
}
