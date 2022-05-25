package newera.fyp.chatera_client.handler

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class EncryptionManager(
   val context : Context,
) {
    @SuppressLint("GetInstance")
    fun encrypt(value: String, name: String): String {
        return try {
            val data: ByteArray = value.toByteArray(Charsets.UTF_8)
            val cipher = Cipher.getInstance("AES")
            val key = KeysHandle.getKey(context, name)
            val secretKey = convertToSecretKey(key!!)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val cipherText = cipher.doFinal(data)
            //val iv = cipher.iv
            android.util.Base64.encodeToString(cipherText, android.util.Base64.DEFAULT)
        }catch (error: Exception){
            Log.e("encrypt", error.message.toString())
            value
        }
    }

    @SuppressLint("GetInstance")
    fun decrypt(value: String, name: String): Any? {
        return try {
            val cipher = Cipher.getInstance("AES")
            val data = android.util.Base64.decode(value, android.util.Base64.DEFAULT)
            val key = KeysHandle.getKey(context, name)
            //val ivSpec = IvParameterSpec(data)

            cipher.init(Cipher.DECRYPT_MODE, convertToSecretKey(key!!))
            String(cipher.doFinal(data))
        }catch (error: Exception) {
            Log.e("decrypt", error.message.toString())
            null
        }
    }

    fun generateKey(): String {
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)
        val key = keygen.generateKey()
        return convertToString(key)
    }

    private fun convertToSecretKey (key: String) : SecretKey {
        val bytes = android.util.Base64.decode(key, android.util.Base64.DEFAULT)
        val ois = ObjectInputStream(ByteArrayInputStream(bytes))
        return ois.readObject() as SecretKey
    }

    private fun convertToString (key: SecretKey) : String {
        val bas = ByteArrayOutputStream()
        val oos = ObjectOutputStream(bas)
        oos.writeObject(key)
        return String(android.util.Base64.encode(bas.toByteArray(), android.util.Base64.DEFAULT))
    }
}