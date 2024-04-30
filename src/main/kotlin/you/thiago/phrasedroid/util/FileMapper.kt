package you.thiago.phrasedroid.util

import you.thiago.phrasedroid.data.Locale
import you.thiago.phrasedroid.data.ResourceFile
import you.thiago.phrasedroid.data.Translation

object FileMapper {

    fun getResourceFilesList(translations: List<Translation>): List<ResourceFile> {
        return translations
            .map { mapToResourceFile(it) }
            .validateDefaultItems()
            .sortList()
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

    private fun List<ResourceFile>.validateDefaultItems(): List<ResourceFile> {
        val list = this.toMutableList()

        if (list.firstOrNull { it.locale == "en" } == null) {
            list.firstOrNull { it.locale in listOf("en-GB", "en-CA") }?.also {
                list.add(it.copyItem(""))
            }
        }

        if (list.firstOrNull { it.locale == "pt" } == null) {
            list.firstOrNull { it.locale in listOf("pt", "pt-BR", "pt-PT") }?.also {
                list.add(it.copyItem("-pt"))
            }
        }

        if (list.firstOrNull { it.locale == "es" } == null) {
            list.firstOrNull { it.locale in listOf("es-AR", "es-CL", "es-CO", "es-MX", "es-ES") }?.also {
                list.add(it.copyItem("-es"))
            }
        }

        if (list.firstOrNull { it.locale == "fr" } == null) {
            list.firstOrNull { it.locale in listOf("fr-FR", "fr-CA") }?.also {
                list.add(it.copyItem("-fr"))
            }
        }

        if (list.firstOrNull { it.locale == "nl" } == null) {
            list.firstOrNull { it.locale in listOf("nl-NL") }?.also {
                list.add(it.copyItem("-nl"))
            }
        }

        return list
    }

    private fun List<ResourceFile>.sortList(): List<ResourceFile> {
        val sortedList = this
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

    private fun ResourceFile.copyItem(suffix: String): ResourceFile {
        val itemFilename = "values$suffix/strings.xml"
        val itemFilePath = "/app/src/main/res/${filename}"

        return ResourceFile(
            filename = itemFilename,
            filePath = itemFilePath,
            name = name,
            locale = locale,
            translation = translation,
            sourceTranslation = sourceTranslation,
            content = content
        )
    }
}
