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
import newera.fyp.chatera_client.helper.recycleview.ItemClickAction
import newera.fyp.chatera_client.model.users.UserModel

class UsersAdapter (
    private val data: ArrayList<UserModel>,
    private val itemClickAction: ItemClickAction
) : RecyclerView.Adapter<UsersAdapter.UserHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_content_item, parent, false)
        return UserHolder(view, itemClickAction)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val item = data[position]
        holder.bindUser(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class UserHolder (
        val view: View,
        private val onClick: ItemClickAction
        ) : RecyclerView.ViewHolder(view) {

        @SuppressLint("ClickableViewAccessibility")
        fun bindUser(user: UserModel) {

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

            name.text = user.name

            userAvatar.setBackgroundResource(R.drawable.ic_launcher_background)
            userAvatar.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }
}