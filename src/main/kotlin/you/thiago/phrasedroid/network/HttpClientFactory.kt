package you.thiago.phrasedroid.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.gson.gson

object HttpClientFactory {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }
}