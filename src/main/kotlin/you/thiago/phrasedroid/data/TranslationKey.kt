package you.thiago.phrasedroid.data

import com.fasterxml.jackson.annotation.JsonProperty

data class TranslationKey(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("description") val description: String?,
    @JsonProperty("name_hash") val nameHash: String,
    @JsonProperty("plural") val plural: Boolean,
    @JsonProperty("max_characters_allowed") val maxCharactersAllowed: Int,
    @JsonProperty("tags") val tags: List<String>,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("updated_at") val updatedAt: String
)