package newera.fyp.chatera_client.view.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import newera.fyp.chatera_client.R
import newera.fyp.chatera_client.adapter.ChatsAdapter
import newera.fyp.chatera_client.handler.KeysHandle
import newera.fyp.chatera_client.helper.KtorHelper
import newera.fyp.chatera_client.helper.recycleview.ItemClickAction
import newera.fyp.chatera_client.helper.recycleview.ItemSwipeAction
import newera.fyp.chatera_client.helper.recycleview.SwipeHelper
import newera.fyp.chatera_client.interfaces.IDefaultListFragment
import newera.fyp.chatera_client.model.chats.ChatModel
import newera.fyp.chatera_client.model.chats.ChatViewModel
import newera.fyp.chatera_client.services.ChatServices


class ChatsFragment : Fragment(), IDefaultListFragment {

    private var chatModels = ArrayList<ChatModel>()
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var adapter: ChatsAdapter
    private lateinit var iChatService: ChatServices
    private var publicKey: String? = null
    private val chatsViewModel: ChatViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        publicKey = KeysHandle.getKey(requireContext(), KeysHandle.USER_PUBLIC_KEY)
        iChatService = ChatServices(KtorHelper(requireContext()).client())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getList()
        setList(view)
        floatingBtnClick(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearList()
    }

    override fun setList(view: View) {
        var onSwiping = false
        val linearLayoutManager = LinearLayoutManager(context)

        chatRecyclerView = view.findViewById(R.id.chats_rv)
        chatRecyclerView.addItemDecoration(DividerItemDecoration(
            requireContext(),
            linearLayoutManager.orientation
            ))
        adapter = ChatsAdapter(chatModels, object : ItemClickAction{

            override fun onItemClick(position: Int) {
                chatsViewModel.setId(chatModels[position].id)
            }

            override fun onTouchUp() {
                if(!onSwiping){
                    (activity as ChatActivity).messageFragment()
                }
                onSwiping = false
            }
        })
        if (chatRecyclerView.layoutManager == null) {
            chatRecyclerView.layoutManager = linearLayoutManager
        }
        if (chatRecyclerView.adapter == null) {
            chatRecyclerView.adapter = adapter
        }
        val swipeHelper = SwipeHelper(requireContext(), object : ItemSwipeAction {
            override fun onItemSwipe(position: Int) {
                onSwiping = true
                delItem(position)
            }
        })
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(chatRecyclerView)
    }

    override fun getList() {
        lifecycleScope.launch {
            try {
                val response = iChatService.getAll(publicKey!!)

                when(response.statusCode){
                    200->{
                        if(response.result != null){
                            if(response.result.size != chatModels.size) {
                                response.result.forEach {
                                    chatModels.add(it)
                                }
                                onNewItem()
                            }
                        }
                    }
                }
            } catch (error: Exception) {
                Log.e("getList", error.message.toString())
            }
        }
    }

    override fun delItem(position: Int) {
        lifecycleScope.launch{
            iChatService.deleteOne(chatModels[position].id)
        }
        chatModels.removeAt(position)
        onDelItem(position)
    }

    override fun onNewItem() {
        activity?.runOnUiThread {
            adapter.notifyItemInserted(chatModels.size-1)
        }
    }

    override fun onDelItem(position: Int) {
        activity?.runOnUiThread {
            adapter.notifyItemRemoved(position)
        }
    }

    override fun clearList() {
        val count = chatModels.size
        chatModels.clear()
        adapter.notifyItemRangeRemoved(0, count)
    }

    private fun floatingBtnClick(view: View) {
        val floatingActionButton = view.findViewById<FloatingActionButton>(R.id.chat_add_btn)
        floatingActionButton?.setOnClickListener {
            clearList()

            val fragmentManager : FragmentManager = (context as AppCompatActivity).supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.setReorderingAllowed(true)
            transaction.replace(R.id.activity_chat_fcv, FindUserFragment::class.java, null)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}