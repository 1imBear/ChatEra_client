package newera.fyp.chatera_client.helper

import android.content.Context
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import newera.fyp.chatera_client.R
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class KtorHelper(
    private val context: Context
) {
    fun client () : HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation){
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            engine {
                sslManager = { httpsURLConnection ->
                    httpsURLConnection.sslSocketFactory = SslSettings.getSslContext(context)?.socketFactory
                }
            }
        }
    }

    object SslSettings {
        private fun getKeyStore(context: Context): KeyStore {
            val keyStoreFile = context.resources.openRawResource(R.raw.domain)
            val keyStorePassword = "123456".toCharArray()
            val keyStore = KeyStore.getInstance("PKCS12")
            keyStore.load(keyStoreFile, keyStorePassword)
            return keyStore
        }

        private fun getTrustManagerFactory(context: Context): TrustManagerFactory? {
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(getKeyStore(context))
            return trustManagerFactory
        }

        fun getSslContext(context: Context): SSLContext? {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, getTrustManagerFactory(context)?.trustManagers, null)
            return sslContext
        }

        fun getTrustManager(context: Context): X509TrustManager {
            return getTrustManagerFactory(context)?.trustManagers?.first { it is X509TrustManager } as X509TrustManager
        }
    }
}