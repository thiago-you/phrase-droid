package you.thiago.phrasedroid.network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import you.thiago.phrasedroid.data.ApiSettings

class Api {

    private val phraseApiUrl = "https://api.phrase.com"

    private val keyInfoEndpoint = "/v2/projects/%s/keys?q=name:%s"
    private val translationsEndpoint = "/v2/projects/%s/keys/%s/translations"

    suspend fun getTranslationKeyId(apiSettings: ApiSettings): String? {
        val endpoint = buildEndpoint(apiSettings, keyInfoEndpoint, "ON_REQUEST_WAIT_CONFIRMATION")
        return executeRequest(apiSettings, endpoint)
    }

    suspend fun getTranslations(apiSettings: ApiSettings, translationId: String): String? {
        val endpoint = buildEndpoint(apiSettings, translationsEndpoint, translationId)
        return executeRequest(apiSettings, endpoint)
    }

    private suspend fun executeRequest(apiSettings: ApiSettings, endpoint: String): String? {
        return try {
            val response: HttpResponse = HttpClientFactory.client.request(endpoint) {
                headers {
                    append(HttpHeaders.Authorization, "Basic ${apiSettings.key}")
                    append(HttpHeaders.UserAgent, "Android App (${apiSettings.agentEmail})")
                    append(HttpHeaders.UserAgent, "Android App (${apiSettings.agentUrl})")
                }
            }

            response.body() as String
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun buildEndpoint(apiSettings: ApiSettings, endpoint: String, extra: String): String {
        return phraseApiUrl + endpoint.format(apiSettings.id, extra)
    }
}