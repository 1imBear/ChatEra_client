package newera.fyp.chatera_client.model.chats

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import newera.fyp.chatera_client.helper.KtorHelper
import newera.fyp.chatera_client.services.ChatServices

class ChatViewModel: ViewModel() {
    private val id: MutableLiveData<String> = MutableLiveData<String>()

    fun getId() : LiveData<String>{
        return id
    }

    fun setId(_id: String){
        id.value = _id
    }

    fun createChat(context: Context, chatModel: ChatModel) {
        val iChatService = ChatServices(KtorHelper(context).client())
        viewModelScope.launch {
            val response = iChatService.createOne(chatModel)
            when(response.statusCode){
                200 -> {
                    id.value = response.result!!
                }
            }
        }
    }
}