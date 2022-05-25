package newera.fyp.chatera_client.view.verify

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import newera.fyp.chatera_client.databinding.FragmentRegisterBinding
import newera.fyp.chatera_client.handler.PasswordHandle
import newera.fyp.chatera_client.helper.KtorHelper
import newera.fyp.chatera_client.model.users.UserModel
import newera.fyp.chatera_client.services.UserServices
import java.lang.Exception

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var iUserService: UserServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iUserService = UserServices(KtorHelper(requireContext()).client())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passwordHandle()

        binding.submitBtn.setOnClickListener {
            requestRegister()
        }
        binding.cancelBtn.setOnClickListener {
            (activity as VerifyActivity).signIn()
        }
    }

    private fun requestRegister(){
        try {
            var userModel = UserModel(
                binding.userNameTxt.text.toString(),
                binding.userPasswdTxt.text.toString())

            userModel.name = binding.userNameTxt.text.toString()

            lifecycleScope.launch {
                var response = iUserService.createOne(userModel)
                when (response.statusCode){
                    200 -> (activity as VerifyActivity).signIn()
                    else -> {
                        binding.alertTxt.visibility = View.VISIBLE
                        binding.alertTxt.text = response.message?: "Please field up as require"
                    }
                }
            }
        }catch (error : Exception){
            Log.e("requestRegister", error.message.toString())
        }
    }

    private fun passwordHandle(){
        try {
            setSubmitBtn(false)
            var passwordHandle = PasswordHandle
            var config = passwordHandle.config
            var userPasswd = binding.userPasswdTxt
            var userPasswd2 = binding.userPasswd2Txt

            userPasswd.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, count: Int, p2: Int, p3: Int) {
                    config = passwordHandle.range(count+1)
                    if(!config.enable){
                        binding.userPasswdTxt.error = config.error
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    setSubmitBtn(config.enable)
                }
            })
            userPasswd2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, count: Int, p2: Int, p3: Int) {
                    config = passwordHandle.compare(binding.userPasswdTxt.text.toString(), binding.userPasswd2Txt.text.toString())
                    if(!config.enable){
                        binding.userPasswd2Txt.error = config.error
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    setSubmitBtn(config.enable)
                }
            })
        }
        catch (error : Exception){
            Log.e("confirmPassword", error.message.toString())
        }
    }

    private  fun setSubmitBtn(clickable: Boolean){
        binding.submitBtn.isEnabled = clickable
        binding.submitBtn.isClickable = clickable
    }
}