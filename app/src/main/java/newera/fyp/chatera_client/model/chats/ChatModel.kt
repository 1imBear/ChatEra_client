package newera.fyp.chatera_client.model.chats

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import newera.fyp.chatera_client.model.users.UserModel

@Serializable
@SerialName("Chat")
class ChatModel(
    var id: String,
    @SerialName("Title")
    var title: String,
    @SerialName("ChatType")
    var chatType: Int
) {
    @SerialName("DateUpdate")
    var dateUpdate: String? = null

    @SerialName("Members")
    var members: ArrayList<UserModel>? = null
}