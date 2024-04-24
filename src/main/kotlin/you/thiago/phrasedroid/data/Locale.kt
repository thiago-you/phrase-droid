package you.thiago.phrasedroid.data

import com.fasterxml.jackson.annotation.JsonProperty

data class Locale(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("code") val code: String
)