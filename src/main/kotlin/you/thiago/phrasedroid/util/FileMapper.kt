package you.thiago.phrasedroid.util

import you.thiago.phrasedroid.data.Locale
import you.thiago.phrasedroid.data.ResourceFile
import you.thiago.phrasedroid.data.Translation

object FileMapper {

    fun getResourceFilesList(translations: List<Translation>): List<ResourceFile> {
        return translations
            .map { mapToResourceFile(it) }
            .distinctBy { it.filename }
    }

    private fun mapToResourceFile(translation: Translation): ResourceFile {
        val suffix = getPathSuffix(translation.locale)

        val filename = "values$suffix/strings.xml"
        val filePath = "/app/src/main/res/$filename"

        val stringResName = getTranslationName(translation.key.name)
        val translationContent = getTranslationContent(stringResName, translation.content)

        return ResourceFile(
            filename = filename,
            filePath = filePath,
            name = stringResName,
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

    private fun getTranslationName(name: String): String {
        return "string name=\"%s\"".format(name)
    }

    private fun getTranslationContent(stringResName: String, content: String): String {
        return "\t<%s>%s</string>\n".format(stringResName, content)
    }
}
