package newera.fyp.chatera_client.view.chat

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import newera.fyp.chatera_client.R
import newera.fyp.chatera_client.adapter.UsersAdapter
import newera.fyp.chatera_client.handler.KeysHandle
import newera.fyp.chatera_client.helper.KtorHelper
import newera.fyp.chatera_client.helper.recycleview.ItemClickAction
import newera.fyp.chatera_client.interfaces.IDefaultListFragment
import newera.fyp.chatera_client.model.chats.ChatModel
import newera.fyp.chatera_client.model.chats.ChatViewModel
import newera.fyp.chatera_client.model.users.UserModel
import newera.fyp.chatera_client.services.ChatServices
import newera.fyp.chatera_client.services.UserServices
import java.lang.Exception

class FindUserFragment : Fragment(), IDefaultListFragment {
    private lateinit var iUserServices: UserServices
    private var userModels = ArrayList<UserModel>()
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var adapter: UsersAdapter
    private var publicKey: String? = null
    private var name: String? = null
    private lateinit var iChatService: ChatServices
    private val chatsViewModel: ChatViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iUserServices = UserServices(KtorHelper(requireContext()).client())
        iChatService = ChatServices(KtorHelper(requireContext()).client())
        publicKey = KeysHandle.getKey(requireContext(), KeysHandle.USER_PUBLIC_KEY)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setList(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val search = menu.findItem(R.id.user_chat_search)
        search.isVisible = true
        val searchView = search?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(value: String?): Boolean {
                if(!value.isNullOrEmpty())
                {
                    name = value
                    getList()
                }

                return true
            }
            override fun onQueryTextChange(value: String?): Boolean {
                return true
            }
        })
    }

    override fun setList(view: View) {
        userRecyclerView = view.findViewById(R.id.users_rv)
        adapter = UsersAdapter(userModels, object : ItemClickAction{
        val data = ChatModel("", "friend", 0)

            override fun onItemClick(position: Int) {
                val member = UserModel()
                member.publicKey = publicKey
                data.members = ArrayList()
                data.members!!.add(userModels[position])
                data.members!!.add(member)
            }

            override fun onTouchUp() {
                lifecycleScope.launch {
                    val response = iChatService.createOne(data)
                    when(response.statusCode){
                        200 -> {
                            chatsViewModel.setId(response.result!!)
                            (activity as ChatActivity).messageFragment()
                        }
                    }
                }
            }
        })

        if(userRecyclerView.layoutManager == null)
            userRecyclerView.layoutManager = LinearLayoutManager(context)

        if(userRecyclerView.adapter == null)
            userRecyclerView.adapter = adapter

    }

    override fun getList() {
        lifecycleScope.launch {
            try {
                val userModel = UserModel()
                userModel.name = name
                userModel.publicKey = publicKey
                val response = iUserServices.getAll(userModel)

                when(response.statusCode){
                    200->{
                        if(!response.result.isNullOrEmpty()){
                            clearList()
                            response.result.forEach {
                                userModels.add(it)
                            }
                            onNewItem()
                        }
                    }
                }

            } catch (error: Exception) {
                Log.e("requestChat", error.message.toString())
            }
        }
    }

    override fun delItem(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onNewItem() {
        activity?.runOnUiThread {
            adapter.notifyItemInserted(userModels.size-1)
        }
    }

    override fun onDelItem(position: Int) {
        TODO("Not yet implemented")
    }

    override fun clearList() {
        val count = userModels.size
        userModels.clear()
        adapter.notifyItemRangeRemoved(0, count)
    }
}