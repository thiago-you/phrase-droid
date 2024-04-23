package you.thiago.phrasedroid.network

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*

object HttpClientFactory {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT)
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                writerWithDefaultPrettyPrinter()
                setDefaultLeniency(true)
                coercionConfigDefaults()
            }
        }
    }
}