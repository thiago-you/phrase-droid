package you.thiago.phrasedroid.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Key(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("plural") val plural: Boolean,
    @JsonProperty("data_type") val dataType: String,
    @JsonProperty("tags") val tags: List<String>
)