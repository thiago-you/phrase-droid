package you.thiago.phrasedroid.network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import you.thiago.phrasedroid.data.ApiSettings

class Api {

    private val phraseApiUrl = "https://api.phrase.com"

    private val projectKeyEndpoint = "/v2/projects/%s/keys?q=name:%s"

    suspend fun fetchApiData(apiSettings: ApiSettings): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = HttpClientFactory.client.request(buildEndpoint(apiSettings)) {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer ${apiSettings.key}")
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
    }

    private fun buildEndpoint(apiSettings: ApiSettings): String {
        return phraseApiUrl + projectKeyEndpoint.format(apiSettings.id, "ON_REQUEST_WAIT_CONFIRMATION")
    }
}