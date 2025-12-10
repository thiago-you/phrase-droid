package you.thiago.phrasedroid.data

import com.google.gson.annotations.SerializedName

data class TranslationKey(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("name_hash")
    val nameHash: String,
    @SerializedName("plural")
    val plural: Boolean,
    @SerializedName("max_characters_allowed")
    val maxCharactersAllowed: Int,
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)