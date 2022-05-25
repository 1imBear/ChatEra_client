package newera.fyp.chatera_client.view.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import newera.fyp.chatera_client.adapter.MessagesAdapter
import newera.fyp.chatera_client.databinding.FragmentMessagesBinding
import newera.fyp.chatera_client.handler.EncryptionManager
import newera.fyp.chatera_client.handler.KeysHandle
import newera.fyp.chatera_client.helper.KtorHelper
import newera.fyp.chatera_client.helper.SocketHelper
import newera.fyp.chatera_client.interfaces.IDefaultListFragment
import newera.fyp.chatera_client.model.chats.ChatViewModel
import newera.fyp.chatera_client.model.message.MessageModel
import newera.fyp.chatera_client.model.users.JoinChatModel
import newera.fyp.chatera_client.services.MessageServices

class MessagesFragment : Fragment(), IDefaultListFragment {
    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!
    private lateinit var encryptionManager: EncryptionManager
    private lateinit var iMessageService: MessageServices
    private lateinit var adapter: MessagesAdapter
    private var chatId: String? = null
    private lateinit var publicKey: String
    private var messageModels = ArrayList<MessageModel>()
    private lateinit var socketManager: SocketHelper
    private val chatsViewModel: ChatViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        encryptionManager = EncryptionManager(requireContext())
        iMessageService = MessageServices(KtorHelper(requireContext()).client())
        publicKey = KeysHandle.getKey(requireContext(), KeysHandle.USER_PUBLIC_KEY) as String
        socketManager = SocketHelper(requireContext())

        socketManager.connect()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroy() {
        socketManager.disconnect()
        clearList()
        super.onDestroy()
    }

    override fun onDestroyView() {
        _binding = null
        socketManager.disconnect()
        clearList()
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setList(view)
        sendMessage()
        getNew()
        chatsViewModel.getId().observe(viewLifecycleOwner) {
            chatId = it
            joinChat()
            getList()
        }
    }

    private fun joinChat(){
        socketManager.joinRoom(JoinChatModel(publicKey, chatId!!))
    }

    private fun sendMessage(){
        binding.messageSubmitBtn.setOnClickListener {

            if(!binding.messageTxt.text.isNullOrEmpty()){
                var key : String? = null

                if(messageModels.size == 0) {
                    key = KeysHandle.getKey(requireContext(), chatId!!)
                    if(key == null)
                        key = encryptionManager.generateKey()

                    KeysHandle.saveKey(requireContext(), chatId!!, key)
                }

                val encrypt = encryptionManager.encrypt(binding.messageTxt.text.toString(), chatId!!)
                val messageModel = MessageModel(chatId, publicKey, encrypt)
                messageModel.privateKey = key


                socketManager.sendMessage(messageModel)
                messageModels.add(messageModel)
                binding.messageTxt.text = null
                onNewItem()
            }
        }
    }

    override fun setList(view: View) {
        adapter = MessagesAdapter(view.context, messageModels)

        if(binding.messagesRv.layoutManager == null)
            binding.messagesRv.layoutManager = LinearLayoutManager(context)

        if(binding.messagesRv.adapter == null)
            binding.messagesRv.adapter = adapter
    }

    override fun getList() {
        socketManager.getAll { history ->
            for (obj in history){
                if(!obj.privateKey.isNullOrBlank()){
                    KeysHandle.saveKey(requireContext(), chatId!!, obj.privateKey!!)
                }
                messageModels.add(obj)
            }
            if(history.size > 0){
                onNewItem()
            }
        }

    }

    private fun getNew(){
        socketManager.getNew {
            messageModels.add(it)
            onNewItem()
        }
    }

    override fun delItem(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onNewItem() {
        activity?.runOnUiThread {
            adapter.notifyItemInserted(messageModels.size-1)
            binding.messageTxt.setText("")
            binding.messagesRv.scrollToPosition(messageModels.size - 1) //move focus on last message
        }
    }

    override fun onDelItem(position: Int) {
        TODO("Not yet implemented")
    }

    override fun clearList() {
        val count = messageModels.size
        messageModels.clear()
        adapter.notifyItemRangeRemoved(0, count)
    }
}