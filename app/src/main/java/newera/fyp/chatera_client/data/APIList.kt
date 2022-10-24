package newera.fyp.chatera_client.data

object APIList {
    private const val PORT = 8080
    const val BASE_URL = "https://chaterarld8480ni2.ddns.net:$PORT"

    object User {
        const val SIGN_IN = "$BASE_URL/auth/user"
        const val SIGN_UP = "$BASE_URL/auth/user/create"
        const val GetAllByName = "$BASE_URL/user/getall"
    }
    object Chat {
        const val GetAllById = "$BASE_URL/chat/getall"
        const val CreateOne = "$BASE_URL/chat/create"
        const val DeleteOne = "$BASE_URL/chat/delete"
    }
    object Message {
        const val CHAT = "$BASE_URL"
        const val GetAllById = "$BASE_URL/message/getall"
    }
}