package newera.fyp.chatera_client.model.users

import kotlinx.serialization.Serializable

@Serializable
class JoinChatModel(
    var PublicKey: String,
    var ChatId: String
) {
}