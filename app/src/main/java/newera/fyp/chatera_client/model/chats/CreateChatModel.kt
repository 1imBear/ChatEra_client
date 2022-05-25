package newera.fyp.chatera_client.model.chats

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import newera.fyp.chatera_client.model.users.UserModel

@Serializable
data class CreateChatModel(
    @SerialName("Title")
    var title: String,
    @SerialName("ChatType")
    var chatType: Int,
    @SerialName("Members")
    var members: ArrayList<String>
) {
}