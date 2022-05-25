package newera.fyp.chatera_client.model.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName("UserName")
    var userName: String? = null,
    @SerialName("Password")
    var userPasswd: String? = null,
) {
    val id: String? = null
    @SerialName("Name")
    var name: String? = null
    @SerialName("PublicKey")
    var publicKey: String? = null
}