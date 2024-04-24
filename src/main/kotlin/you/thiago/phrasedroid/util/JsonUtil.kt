package you.thiago.phrasedroid.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.vfs.VirtualFile
import you.thiago.phrasedroid.data.ApiSettings
import you.thiago.phrasedroid.data.Translation
import you.thiago.phrasedroid.data.TranslationKey
import java.nio.charset.StandardCharsets


object JsonUtil {
    private val objectMapper: ObjectMapper = JsonMapper.objectMapper

    fun readConfig(file: VirtualFile): ApiSettings? {
        try {
            val json = String(file.contentsToByteArray(), StandardCharsets.UTF_8)
            return fromJson(json, ApiSettings::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun readTranslationKeyInfo(json: String): TranslationKey? {
        try {
            val typeReference: TypeReference<List<TranslationKey>> = object : TypeReference<List<TranslationKey>>() {}
            val list = fromJson(json, typeReference)

            if (list?.isNotEmpty() == true) {
                return list.first()
            }

            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun readTranslations(json: String): List<Translation>? {
        try {
            val typeReference: TypeReference<List<Translation>> = object : TypeReference<List<Translation>>() {}
            return fromJson(json, typeReference)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun <T> fromJson(json: String?, valueType: Class<T>?): T? {
        try {
            return objectMapper.readValue(json, valueType)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun <T> fromJson(json: String?, typeReference: TypeReference<T>?): T? {
        try {
            return objectMapper.readValue(json, typeReference)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}