package newera.fyp.chatera_client.model.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageModel(
    @SerialName("ChatId")
    var chatId: String? = null,
    @SerialName("PublicKey")
    var publicKey: String? = null,
    @SerialName("Content")
    var content: String? = null,
    @SerialName("readed")
    var readed: Boolean = false,
    ) {
    var id: String? = null
    @SerialName("PrivateKey")
    var privateKey: String? = null
    @SerialName("DateUpdate")
    var dateUpdate: String? = null
}