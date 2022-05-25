package newera.fyp.chatera_client.helper.recycleview

import newera.fyp.chatera_client.model.chats.ChatModel
import newera.fyp.chatera_client.model.users.UserModel

interface ItemClickAction {
    fun onItemClick(position: Int)
    fun onTouchUp()
}

interface ItemSwipeAction {
    fun onItemSwipe(position: Int)
}