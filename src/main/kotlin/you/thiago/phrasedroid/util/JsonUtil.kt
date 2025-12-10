package you.thiago.phrasedroid.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.vfs.VirtualFile
import you.thiago.phrasedroid.data.ApiSettings
import you.thiago.phrasedroid.data.Translation
import you.thiago.phrasedroid.data.TranslationKey
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

object JsonUtil {
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
            val typeOfT = object : TypeToken<List<TranslationKey>>() {}.type
            val list = fromJson<List<TranslationKey>?>(json, typeOfT)

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
            val typeOfT = object : TypeToken<List<Translation>>() {}.type
            return fromJson(json, typeOfT)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun <T> fromJson(json: String?, valueType: Class<T>?): T? {
        try {
            return Gson().fromJson(json, valueType)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun <T> fromJson(json: String?, typeOfT: Type?): T? {
        try {
            return Gson().fromJson(json, typeOfT)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}