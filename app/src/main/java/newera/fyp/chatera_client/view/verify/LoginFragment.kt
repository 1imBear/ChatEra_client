package newera.fyp.chatera_client.view.verify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import newera.fyp.chatera_client.databinding.FragmentLoginBinding
import newera.fyp.chatera_client.handler.KeysHandle
import newera.fyp.chatera_client.helper.KtorHelper
import newera.fyp.chatera_client.model.users.UserModel
import newera.fyp.chatera_client.services.UserServices
import newera.fyp.chatera_client.view.chat.ChatActivity
import java.lang.Exception

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var iUserService: UserServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iUserService = UserServices(KtorHelper(requireContext()).client())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitBtn.setOnClickListener {
            setSubmitBtn(false)
            KeysHandle.delOneKey(view.context, KeysHandle.USER_PRIVATE_ID)
            KeysHandle.delOneKey(view.context, KeysHandle.USER_PUBLIC_KEY)
            requestLogin(view.context)
        }
        binding.registerBtn.setOnClickListener{
            (activity as VerifyActivity).signUp()
        }
    }

    private fun requestLogin(context: Context){
        try {
            var userModel = UserModel(
                binding.userNameTxt.text.toString(),
                binding.userPasswdTxt.text.toString())

            viewLifecycleOwner.lifecycleScope.launch {
                val response = iUserService.authentication(userModel)

                when(response.statusCode) {
                    200 -> {
                        if(response.result != null) {
                            KeysHandle.saveKey(context, KeysHandle.USER_PRIVATE_ID, response.result.id!!)
                            KeysHandle.saveKey(context, KeysHandle.USER_PUBLIC_KEY, response.result.publicKey!!)

                            startActivity(Intent(activity, ChatActivity().javaClass))
                            (activity as VerifyActivity).finish()
                        }
                    }
                    else -> {
                        binding.userPasswdTxt.error = "UserName or Password incorrect"
                        setSubmitBtn(true)
                    }
                }
            }

        }catch (error : Exception){
            Log.e("requestLogin", error.message.toString())
        }
    }

    private fun setSubmitBtn(c: Boolean) {
        binding.submitBtn.isEnabled = c
        binding.submitBtn.isClickable = c
    }
}