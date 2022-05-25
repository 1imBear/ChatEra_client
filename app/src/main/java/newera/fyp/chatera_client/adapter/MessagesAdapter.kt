package newera.fyp.chatera_client.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import newera.fyp.chatera_client.R
import newera.fyp.chatera_client.handler.EncryptionManager
import newera.fyp.chatera_client.handler.KeysHandle
import newera.fyp.chatera_client.model.message.MessageModel

class MessagesAdapter (
    val context: Context,
    private  val data: ArrayList<MessageModel>,
) : RecyclerView.Adapter<MessagesAdapter.MessageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_message_content_item, parent, false)
        return MessageHolder(view, context)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val item = data[position]
        holder.bindMessage(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class MessageHolder(val view: View, val context: Context) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        private val selfContentParam = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.END
        }

        fun bindMessage(message: MessageModel) {
            if(!message.content.isNullOrEmpty()){
                val content = view.findViewById(R.id.message_content_txt) as TextView

                if (message.publicKey == KeysHandle.getKey(context, KeysHandle.USER_PUBLIC_KEY)) {
                    content.layoutParams = selfContentParam
                }
                when (val decrypt = EncryptionManager(context).decrypt(message.content!!, message.chatId!!)) {
                    is ByteArray -> {
                        content.text =  String(decrypt)
                    }
                    is String -> {
                        content.text =  decrypt
                    }
                }
            }
        }


        override fun onClick(p0: View?) {
            TODO("Delete / Edit Message")
        }
    }
}