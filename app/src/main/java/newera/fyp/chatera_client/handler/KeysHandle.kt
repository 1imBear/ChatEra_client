package newera.fyp.chatera_client.handler

import android.content.Context
import android.util.Log

object KeysHandle {
    const val USER_PRIVATE_ID = "id"
    const val USER_PUBLIC_KEY = "public_key"

    fun saveKey(context: Context, name: String, key: String) {
        try {
            val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            val edit = sharedPreferences.edit()

            edit.putString(name, key).apply()

        }catch (error: Exception) {
            Log.e("save key", error.message.toString())
        }
    }

    fun getKey(context: Context, name: String): String? {
        return try {
            val key = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString(name, null)
            key?: return null
        }catch (error: Exception) {
            Log.e("get key", error.message.toString())
            null
        }
    }
    fun delAllKey(context: Context) {
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.clear()
        edit.apply()
    }

    fun delOneKey(context: Context, name: String) {
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.remove(name).apply()
    }
}