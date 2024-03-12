package me.yeojoy.awswebsocket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.logging.HttpLoggingInterceptor

class MainViewModel : ViewModel() {

    private var okHttpClient: OkHttpClient? = null
    private var webSocket: WebSocket? = null

    private val _socketStatus = MutableLiveData(false)
    val socketStatus: LiveData<Boolean> = _socketStatus

    private val _messages = MutableLiveData<Pair<Boolean, String>>()
    val messages: LiveData<Pair<Boolean, String>> = _messages

    fun connectWebSocket() {
        if (webSocket != null && okHttpClient != null) {
            return
        }

        okHttpClient = getClient()
        webSocket = okHttpClient?.newWebSocket(
            getRequest(),
            WebSocketListener(this)
        )
    }

    fun closeWebSocket() {
        release()
    }

    fun addMessage(message: Pair<Boolean, String>) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {
            _messages.value = message
        }
    }

    fun setStatus(status: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        _socketStatus.value = status
    }

    fun release() {
        webSocket?.close(1000, "Close by release() function.")
        okHttpClient?.dispatcher?.executorService?.shutdown()
        webSocket = null
        okHttpClient = null
    }

    private fun getClient() = OkHttpClient.Builder().apply {
        val bodyInterceptor = HttpLoggingInterceptor()
        bodyInterceptor.level = HttpLoggingInterceptor.Level.BODY
        addInterceptor(bodyInterceptor)
    }.build()

    private fun getRequest() = Request.Builder().apply {
        url("wss://{WEBSOCKET_URL_IN_AWS}")
    }.build()
}