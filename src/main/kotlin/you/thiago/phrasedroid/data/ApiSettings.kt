package you.thiago.phrasedroid.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ApiSettings @JsonCreator constructor(
    @JsonProperty("id") var id: String = "",
    @JsonProperty("key") var key: String = "",
    @JsonProperty("contact_email") var contactEmail: String = "",
    @JsonProperty("contact_url") var contactUrl: String = ""
)