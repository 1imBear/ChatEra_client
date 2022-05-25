package newera.fyp.chatera_client.helper

import android.content.Context
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import newera.fyp.chatera_client.data.APIList
import newera.fyp.chatera_client.model.message.MessageModel
import newera.fyp.chatera_client.model.users.JoinChatModel
import okhttp3.OkHttpClient
import java.net.URI
import java.net.URISyntaxException

class SocketHelper(
    val context: Context,
) {
    private lateinit var socket: Socket
    private val ssl = KtorHelper.SslSettings
    var url = APIList.BASE_URL

    init {
        try {
            val sslContext = ssl.getSslContext(context)
            sslContext!!.init(null, arrayOf(ssl.getTrustManager(context)), null)
            val sslSocketFactory = sslContext.socketFactory

            val okHttpClient = OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, ssl.getTrustManager(context))
                .build()

            val opts = IO.Options()
            opts.callFactory = okHttpClient
            opts.webSocketFactory = okHttpClient


            val uri = URI.create(url)

            socket = IO.socket(uri, opts)
            socket.on(Socket.EVENT_CONNECT) {
                Log.d("socket connection", "============================= Connect")
            }
            socket.on(Socket.EVENT_DISCONNECT) {
                Log.d("socket connection", "============================= OFF")
            }
        }catch (error: URISyntaxException){
            Log.e("socket connection", error.message.toString())
        }
    }

    @Synchronized
    fun connect() {
        socket.connect()
    }

    @Synchronized
    fun disconnect(){
        socket.disconnect()
    }

    @Synchronized
    fun joinRoom(req: JoinChatModel) {
        socket.emit("join", Json.encodeToString(req))
    }

    @Synchronized
    fun sendMessage(req: MessageModel) {
        socket.emit("sendMessage", Json.encodeToString(req))
    }

    @Synchronized
    fun getAll(response: (res: ArrayList<MessageModel>) -> Unit) {
        socket.on("history") {
            response(Json.decodeFromString(it[0].toString()))
        }
    }

    @Synchronized
    fun getNew(response: (res: MessageModel) -> Unit) {
        socket.on("updateChat") {
            response(Json.decodeFromString(it[0].toString()))
        }
    }

    @Synchronized
    fun messageRead(response: (res: String) -> Emitter.Listener) {
        socket.on("messageRead") {
            response(Json.decodeFromString(it[0].toString()))
        }
    }

    @Synchronized
    fun readMessage(req: String) {
        socket.emit("readMessage", Json.encodeToString(req))
    }
}