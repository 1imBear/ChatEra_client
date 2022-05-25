package newera.fyp.chatera_client.interfaces

import newera.fyp.chatera_client.model.chats.ChatModel
import newera.fyp.chatera_client.model.HttpResponseModel

interface IChatServices {
    suspend fun getAll (req : String) : HttpResponseModel<ArrayList<ChatModel>>
    suspend fun createOne (req : ChatModel) : HttpResponseModel<String>
}