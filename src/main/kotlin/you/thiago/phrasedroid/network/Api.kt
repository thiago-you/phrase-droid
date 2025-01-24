package you.thiago.phrasedroid.network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import you.thiago.phrasedroid.data.ApiSettings
import you.thiago.phrasedroid.data.Translation
import you.thiago.phrasedroid.state.FlashState
import you.thiago.phrasedroid.util.JsonUtil

object Api {

    private const val API_HOST = "https://api.phrase.com"
    private const val ENDPOINT_KEY_INFO = "/v2/projects/%s/keys?q=name:%s"
    private const val ENDPOINT_TRANSLATIONS = "/v2/projects/%s/keys/%s/translations"

    suspend fun fetchTranslations(apiSettings: ApiSettings): List<Translation> {
        val response = getTranslationKeyId(apiSettings)

        if (!response.isNullOrBlank()) {
            val translationKey = JsonUtil.readTranslationKeyInfo(response)
            val data = getTranslations(apiSettings, translationKey?.id ?: String())

            if (!data.isNullOrBlank()) {
                val list = JsonUtil.readTranslations(data)

                if (list?.isNotEmpty() == true) {
                    return list
                }
            }
        }

        return emptyList()
    }

    private suspend fun getTranslationKeyId(apiSettings: ApiSettings): String? {
        val endpoint = buildEndpoint(apiSettings, ENDPOINT_KEY_INFO, FlashState.translationKey)
        return executeRequest(apiSettings, endpoint)
    }

    private suspend fun getTranslations(apiSettings: ApiSettings, translationId: String): String? {
        val endpoint = buildEndpoint(apiSettings, ENDPOINT_TRANSLATIONS, translationId)
        return executeRequest(apiSettings, endpoint)
    }

    private suspend fun executeRequest(apiSettings: ApiSettings, endpoint: String): String? {
        return try {
            val response: HttpResponse = HttpClientFactory.client.request(endpoint) {
                headers {
                    append(HttpHeaders.Authorization, "Basic ${apiSettings.key}")

                    if (!apiSettings.contactEmail.isNullOrBlank()) {
                        append(HttpHeaders.UserAgent, "Android App (${apiSettings.contactEmail})")
                    }
                    if (!apiSettings.contactUrl.isNullOrBlank()) {
                        append(HttpHeaders.UserAgent, "Android App (${apiSettings.contactUrl})")
                    }
                }
            }

            response.body() as String
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun buildEndpoint(apiSettings: ApiSettings, endpoint: String, extra: String): String {
        return API_HOST + endpoint.format(apiSettings.id, extra)
    }
}