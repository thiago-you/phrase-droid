package you.thiago.phrasedroid.data

import com.google.gson.annotations.SerializedName

data class ApiSettings(
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("key")
    var key: String? = "",
    @SerializedName("contact_email")
    var contactEmail: String? = "",
    @SerializedName("contact_url")
    var contactUrl: String? = ""
)