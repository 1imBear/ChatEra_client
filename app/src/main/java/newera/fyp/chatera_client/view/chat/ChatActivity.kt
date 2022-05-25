package newera.fyp.chatera_client.view.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import newera.fyp.chatera_client.R
import newera.fyp.chatera_client.handler.KeysHandle
import newera.fyp.chatera_client.model.chats.ChatViewModel
import newera.fyp.chatera_client.view.verify.VerifyActivity

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.find_user, menu)
        val search = menu.findItem(R.id.user_chat_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Name"

        menuInflater.inflate(R.menu.utility_setting, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout_btn){
            logout()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

        val fragmentManager = supportFragmentManager

        if(fragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        }
        if (fragmentManager.backStackEntryCount > 0) {
            chatFragment()
        }
    }

    fun messageFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.activity_chat_fcv, MessagesFragment::class.java, null)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun chatFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.activity_chat_fcv, ChatsFragment::class.java, null)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun logout(){
        KeysHandle.delOneKey(applicationContext, KeysHandle.USER_PRIVATE_ID)
        startActivity(Intent(this, VerifyActivity::class.java))
        finish()
    }
}