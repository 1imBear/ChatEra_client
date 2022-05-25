package newera.fyp.chatera_client.interfaces

import newera.fyp.chatera_client.model.HttpResponseModel
import newera.fyp.chatera_client.model.message.MessageModel

interface IMessageServices {
    suspend fun getAll (req: String) : HttpResponseModel<ArrayList<MessageModel>>

}