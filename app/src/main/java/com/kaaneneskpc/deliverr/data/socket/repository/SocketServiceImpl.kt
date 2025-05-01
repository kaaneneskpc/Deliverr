package com.kaaneneskpc.deliverr.data.socket.repository

import com.kaaneneskpc.deliverr.data.socket.SocketService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class SocketServiceImpl @Inject constructor() : SocketService {
    private var webSocket: WebSocket? = null

    companion object {
        private const val SOCKET_ADDRESS = "ws://10.0.2.2:8080"
    }

    private fun createURl(
        orderID: String,
        riderID: String,
        lat: Double?,
        lng: Double?,
        type: String = "LOCATION_UPDATE"
    ): String {
        if (lat == null || lng == null) {
            return "$SOCKET_ADDRESS/track/$orderID"
        }
        return "$SOCKET_ADDRESS/track/$orderID?riderId=$riderID&latitude=$lat&longitude=$lng&type=$type"
    }

    override fun connect(
        orderID: String,
        riderID: String,
        lat: Double?,
        lng: Double?,
    ) {
        val builder = Request.Builder().url(
            createURl(
                orderID,
                riderID,
                lat,
                lng
            )
        ).build()
        val client = OkHttpClient.Builder().build()
        webSocket = client.newWebSocket(builder, createWebSocketListener())
    }

    private fun createWebSocketListener(): WebSocketListener {
        return object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                CoroutineScope(Dispatchers.IO).launch {
                    _messages.emit(text)
                }
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
            }
        }
    }

    override fun disconnect() {
        webSocket?.close(1000, "Goodbye")
        webSocket = null
    }

    override fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    private val _messages = MutableStateFlow("")
    override val messages: Flow<String> = _messages.asStateFlow()

}