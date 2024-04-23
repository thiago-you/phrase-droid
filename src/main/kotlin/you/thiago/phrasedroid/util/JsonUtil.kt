package you.thiago.phrasedroid.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.vfs.VirtualFile
import you.thiago.phrasedroid.data.ApiSettings
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

    private fun <T> fromJson(json: String?, valueType: Class<T>?): T? {
        try {
            return objectMapper.readValue(json, valueType)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}