package newera.fyp.chatera_client.services

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import newera.fyp.chatera_client.data.APIList
import newera.fyp.chatera_client.interfaces.IChatServices
import newera.fyp.chatera_client.model.chats.ChatModel
import newera.fyp.chatera_client.model.HttpResponseModel
import newera.fyp.chatera_client.model.chats.CreateChatModel
import newera.fyp.chatera_client.model.users.UserModel
import java.lang.Exception

class ChatServices(
    private val client: HttpClient
): IChatServices {
    override suspend fun getAll(req: String): HttpResponseModel<ArrayList<ChatModel>> {
        return try {
            val response = client.get {
                url("${APIList.Chat.GetAllById}/$req")
                contentType(ContentType.Application.Json)
            }
            val body = response.body<HttpResponseModel<ArrayList<ChatModel>>>()
            body
        } catch (error : Exception){
            Log.e("getAll", error.message!!)
            HttpResponseModel(error.message, 400, null)
        }
    }

    override suspend fun createOne(req: ChatModel): HttpResponseModel<String> {
        return try {
            val request = CreateChatModel(req.title, req.chatType, ArrayList())
            request.members = ArrayList();
            for (key in req.members!!){
                request.members.add(key.publicKey!!)
            }

            val response = client.post {
                url(APIList.Chat.CreateOne)
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            response.body()
        } catch (error : Exception){
            Log.e("createOne", error.message!!)
            HttpResponseModel(error.message, 400, null)
        }
    }

    suspend fun deleteOne(req: String): HttpResponseModel<String> {
        return try {
            val response = client.delete {
                url("${APIList.Chat.DeleteOne}/$req")
                contentType(ContentType.Application.Json)
            }
            response.body()
        }
        catch (error: Exception){
            Log.e("deleteOne", error.message!!)
            HttpResponseModel(error.message, 400, null)
        }
    }
}