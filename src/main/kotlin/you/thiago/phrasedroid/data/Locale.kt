package you.thiago.phrasedroid.data

import com.google.gson.annotations.SerializedName

data class Locale(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("code")
    val code: String
)