package newera.fyp.chatera_client.services

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import newera.fyp.chatera_client.data.APIList
import newera.fyp.chatera_client.interfaces.IMessageServices
import newera.fyp.chatera_client.model.HttpResponseModel
import newera.fyp.chatera_client.model.message.MessageModel
import java.lang.Exception

class MessageServices(
    private val client: HttpClient
) : IMessageServices{
    override suspend fun getAll(req: String): HttpResponseModel<ArrayList<MessageModel>> {
        return try {
            var response = client.get {
                url("${APIList.Message.GetAllById}/${req}")
                contentType(ContentType.Application.Json)
            }
            response.body()
        } catch (error : Exception){
            Log.e("getAllById", error.message!!)
            HttpResponseModel(error.message, 400, null)
        }
    }
}