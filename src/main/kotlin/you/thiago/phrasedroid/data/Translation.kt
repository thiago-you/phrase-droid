package you.thiago.phrasedroid.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Translation(
    @JsonProperty("id") val id: String,
    @JsonProperty("content") val content: String,
    @JsonProperty("unverified") val unverified: Boolean,
    @JsonProperty("excluded") val excluded: Boolean,
    @JsonProperty("plural_suffix") val pluralSuffix: String,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("updated_at") val updatedAt: String,
    @JsonProperty("placeholders") val placeholders: List<Any>,
    @JsonProperty("state") val state: String,
    @JsonProperty("key") val key: Key,
    @JsonProperty("locale") val locale: Locale
)
