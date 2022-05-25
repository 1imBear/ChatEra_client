package newera.fyp.chatera_client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HttpResponseModel<T>(
    @SerialName("message")
    val message : String?,
    @SerialName("statuscode")
    val statusCode : Int,
    @SerialName("result")
    val result : T? = null,
)