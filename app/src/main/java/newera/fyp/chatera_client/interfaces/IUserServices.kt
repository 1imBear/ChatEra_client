package newera.fyp.chatera_client.interfaces

import newera.fyp.chatera_client.model.HttpResponseModel
import newera.fyp.chatera_client.model.users.UserModel

interface IUserServices {
    suspend fun authentication (req: UserModel): HttpResponseModel<UserModel>
    suspend fun getAll(req: UserModel): HttpResponseModel<ArrayList<UserModel>>
    suspend fun createOne(req: UserModel) : HttpResponseModel<String?>
}