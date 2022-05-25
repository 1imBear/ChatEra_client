package newera.fyp.chatera_client.view.verify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import newera.fyp.chatera_client.R
import newera.fyp.chatera_client.handler.KeysHandle
import newera.fyp.chatera_client.view.chat.ChatActivity

class VerifyActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()

        if(KeysHandle.getKey(this.applicationContext, KeysHandle.USER_PRIVATE_ID) != null){
            startActivity(Intent(this, ChatActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)
    }

    fun signIn(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_fragmentContainer, LoginFragment())
            .addToBackStack(null).commit()
    }

    fun signUp(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_fragmentContainer, RegisterFragment())
            .addToBackStack(null).commit()
    }
}