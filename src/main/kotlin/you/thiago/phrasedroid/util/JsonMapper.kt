package you.thiago.phrasedroid.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

object JsonMapper {
    val objectMapper: ObjectMapper = ObjectMapper()

    init {
        objectMapper.registerModule(JavaTimeModule())
    }
}