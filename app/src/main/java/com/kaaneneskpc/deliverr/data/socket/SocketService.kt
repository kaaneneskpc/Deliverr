package com.kaaneneskpc.deliverr.data.socket

import kotlinx.coroutines.flow.Flow

interface SocketService {

    fun connect(
        orderID: String,
        riderID: String,
        lat: Double?,
        lng: Double?
    )

    fun disconnect()

    fun sendMessage(message: String)

    val messages: Flow<String>
}