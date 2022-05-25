package newera.fyp.chatera_client.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import newera.fyp.chatera_client.R
import newera.fyp.chatera_client.handler.KeysHandle
import newera.fyp.chatera_client.helper.recycleview.ItemClickAction
import newera.fyp.chatera_client.model.chats.ChatModel


class ChatsAdapter (
    private val data: ArrayList<ChatModel>,
    private val itemClickAction: ItemClickAction
) : RecyclerView.Adapter<ChatsAdapter.ChatHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        return ChatHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_content_item, parent, false), itemClickAction)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.bindChat(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ChatHolder (
        val view: View,
        private val onClick: ItemClickAction,
        ) : RecyclerView.ViewHolder(view) {

        @SuppressLint("ClickableViewAccessibility")
        fun bindChat(chat: ChatModel) {

            view.setOnTouchListener { _, motionEvent ->
                when(motionEvent.action){
                    MotionEvent.ACTION_UP -> {
                        onClick.onTouchUp()
                    }
                    MotionEvent.ACTION_DOWN -> {
                        onClick.onItemClick(adapterPosition)
                    }
                }

                true
            }
            val name = view.findViewById(R.id.chat_user_name) as TextView
            val userAvatar = view.findViewById(R.id.chat_thumbnail) as ImageView

            val publicKey = KeysHandle.getKey(view.context, KeysHandle.USER_PUBLIC_KEY)
            name.text = chat.title
            if(chat.chatType == 0) {
                val chats = chat.members?.filter { it.publicKey != publicKey }
                if (chats != null) {
                    name.text = chats[0].name.toString()
                }
            }

            userAvatar.setBackgroundResource(R.drawable.ic_launcher_background)
            userAvatar.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }
}