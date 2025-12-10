package you.thiago.phrasedroid.data

import com.google.gson.annotations.SerializedName

data class Translation(
    @SerializedName("id")
    val id: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("unverified")
    val unverified: Boolean,
    @SerializedName("excluded")
    val excluded: Boolean,
    @SerializedName("plural_suffix")
    val pluralSuffix: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("placeholders")
    val placeholders: List<Any>,
    @SerializedName("state")
    val state: String,
    @SerializedName("key")
    val key: Key,
    @SerializedName("locale")
    val locale: Locale
)
