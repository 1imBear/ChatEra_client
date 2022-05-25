package newera.fyp.chatera_client.services

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import newera.fyp.chatera_client.data.APIList
import newera.fyp.chatera_client.interfaces.IUserServices
import newera.fyp.chatera_client.model.HttpResponseModel
import newera.fyp.chatera_client.model.users.UserModel
import java.lang.Exception

class UserServices (
    private val client: HttpClient
): IUserServices {

    override suspend fun authentication(req: UserModel): HttpResponseModel<UserModel> {
        return try {
            val response = client.post {
                url(APIList.User.SIGN_IN)
                contentType(ContentType.Application.Json)
                setBody(req)
            }
            response.body()
        } catch (error : Exception){
            Log.e("authentication", error.message.toString())
            HttpResponseModel("error", error.hashCode(), null)
        }
    }

    override suspend fun getAll(req: UserModel): HttpResponseModel<ArrayList<UserModel>> {
        return try {
            val response = client.get {
                url("${APIList.User.GetAllByName}/${req.publicKey}/${req.name}")
                contentType(ContentType.Application.Json)
            }
            response.body()
        } catch (error : Exception){
            Log.e("getAllByName", error.message.toString())
            HttpResponseModel("error", error.hashCode(), null)
        }
    }

    override suspend fun createOne(req: UserModel): HttpResponseModel<String?> {
        return try {
            val response = client.post {
                url(APIList.User.SIGN_UP)
                contentType(ContentType.Application.Json)
                setBody(req)
            }
            response.body()
        } catch (error : Exception){
            Log.e("createOne", error.message.toString())
            HttpResponseModel("error", error.hashCode(), null)
        }
    }
}