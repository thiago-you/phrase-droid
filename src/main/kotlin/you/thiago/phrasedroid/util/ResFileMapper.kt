package you.thiago.phrasedroid.util

import you.thiago.phrasedroid.data.ResourceFile
import you.thiago.phrasedroid.data.Translation

object ResFileMapper {

    fun getResourceFilesList(translations: List<Translation>): List<ResourceFile> {
        return translations
            .map { mapToResourceFile(it) }
            .toMutableList()
            .validateMissingVariations()
            .validateByLanguage()
            .sortList()
    }

    private fun mapToResourceFile(translation: Translation): ResourceFile {
        val locale = translation.locale.code
        val translationName = translation.key.name

        val suffix = getPathSuffix(locale)
        val language = getLanguage(locale)

        val filename = "values$suffix/strings.xml"
        val filePath = "/app/src/main/res/$filename"

        val content = TranslationUtil.escapeSingleQuote(translation.content)
        val translationContent = getTranslationContent(translationName, content)

        return ResourceFile(
            filename = filename,
            filePath = filePath,
            name = translationName,
            locale = locale,
            language = language,
            tag = suffix,
            translation = content,
            sourceTranslation = content,
            content = translationContent
        )
    }

    private fun getPathSuffix(localeCode: String): String {
        return when (localeCode) {
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

    private fun getLanguage(localeCode: String): String {
        return when (localeCode) {
            "de-DE" -> "de"
            "en-GB" -> "en"
            "en-CA" -> "en"
            "es-AR" -> "es"
            "es-CL" -> "es"
            "es-CO" -> "es"
            "es-MX" -> "es"
            "es-ES" -> "es"
            "fr-FR" -> "fr"
            "fr-CA" -> "fr"
            "it-IT" -> "it"
            "nl-NL" -> "nl"
            "pt-PT" -> "pt"
            "pt-BR" -> "pt"
            "nl" -> "nl"
            "es" -> "es"
            "pt" -> "pt"
            "fr" -> "fr"
            else -> ""
        }
    }

    private fun getTranslationContent(name: String, content: String): String {
        return "<string name=\"%s\">%s</string>".format(name, content)
    }

    private fun MutableList<ResourceFile>.validateMissingVariations(): MutableList<ResourceFile> {
        if (this.isEmpty()) {
            return this
        }

        val resourceFile = first().copy().apply {
            translation = ""
            sourceTranslation = ""
            content = ""
        }

        if (firstOrNull { it.tag == "en" || it.tag == "" } == null) {
            add(resourceFile.copyItem(locale = "en", suffix = "", language = "en"))
        }
        if (firstOrNull { it.tag == "-de-rDE" } == null) {
            add(resourceFile.copyItem(locale = "de-DE", suffix = "-de-rDE", language = "de"))
        }
        if (firstOrNull { it.tag == "-en-rGB" } == null) {
            add(resourceFile.copyItem(locale = "en-GB", suffix = "-en-rGB", language = "en"))
        }
        if (firstOrNull { it.tag == "-en-rCA" } == null) {
            add(resourceFile.copyItem(locale = "en-CA", suffix = "-en-rCA", language = "en"))
        }
        if (firstOrNull { it.tag == "-es-rAR" } == null) {
            add(resourceFile.copyItem(locale = "es-AR", suffix = "-es-rAR", language = "es"))
        }
        if (firstOrNull { it.tag == "-es-rCL" } == null) {
            add(resourceFile.copyItem(locale = "es-CL", suffix = "-es-rCL", language = "es"))
        }
        if (firstOrNull { it.tag == "-es-rCO" } == null) {
            add(resourceFile.copyItem(locale = "es-CO", suffix = "-es-rCO", language = "es"))
        }
        if (firstOrNull { it.tag == "-es-rMX" } == null) {
            add(resourceFile.copyItem(locale = "es-MX", suffix = "-es-rMX", language = "es"))
        }
        if (firstOrNull { it.tag == "-es-rES" } == null) {
            add(resourceFile.copyItem(locale = "es-ES", suffix = "-es-rES", language = "es"))
        }
        if (firstOrNull { it.tag == "-fr-rFR" } == null) {
            add(resourceFile.copyItem(locale = "fr-FR", suffix = "-fr-rFR", language = "fr"))
        }
        if (firstOrNull { it.tag == "-fr-rCA" } == null) {
            add(resourceFile.copyItem(locale = "fr-CA", suffix = "-fr-rCA", language = "fr"))
        }
        if (firstOrNull { it.tag == "-it-rIT" } == null) {
            add(resourceFile.copyItem(locale = "it-IT", suffix = "-it-rIT", language = "it"))
        }
        if (firstOrNull { it.tag == "-nl" } == null) {
            add(resourceFile.copyItem(locale = "nl-NL", suffix = "-nl", language = "nl"))
        }
        if (firstOrNull { it.tag == "-pt-rPT" } == null) {
            add(resourceFile.copyItem(locale = "pt-PT", suffix = "-pt-rPT", language = "pt"))
        }
        if (firstOrNull { it.tag == "-pt" } == null) {
            add(resourceFile.copyItem(locale = "pt-BR", suffix = "-pt", language = "pt"))
        }
        if (firstOrNull { it.tag == "-nl" } == null) {
            add(resourceFile.copyItem(locale = "nl", suffix = "-nl", language = "nl"))
        }
        if (firstOrNull { it.tag == "-es" } == null) {
            add(resourceFile.copyItem(locale = "es", suffix = "-es", language = "es"))
        }

        return this
    }

    private fun MutableList<ResourceFile>.validateByLanguage(): MutableList<ResourceFile> {
        val list = map { item ->
            if (item.translation.isEmpty()) {
                this.firstOrNull { it.language == item.language && it.translation.isNotEmpty() }?.also {
                    return@map it.copyItem(suffix = item.tag, locale = item.locale)
                }
            }

            return@map item
        }

        return list.toMutableList()
    }

    private fun List<ResourceFile>.sortList(): List<ResourceFile> {
        val sortedList = this
            .distinctBy { it.filename }
            .sortedWith(compareBy({ it.translation.isNotEmpty() }, { it.language }))
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

    private fun ResourceFile.copyItem(suffix: String, locale: String? = null, language: String? = null): ResourceFile {
        val itemFilename = "values$suffix/strings.xml"
        val itemFilePath = "/app/src/main/res/${itemFilename}"

        return ResourceFile(
            filename = itemFilename,
            filePath = itemFilePath,
            name = name,
            locale = locale ?: this.locale,
            tag = suffix,
            language = language ?: this.language,
            translation = translation,
            sourceTranslation = sourceTranslation,
            content = content
        )
    }
}
