package you.thiago.phrasedroid.data

import com.google.gson.annotations.SerializedName

data class Key(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("plural")
    val plural: Boolean,
    @SerializedName("data_type")
    val dataType: String,
    @SerializedName("tags")
    val tags: List<String>
)