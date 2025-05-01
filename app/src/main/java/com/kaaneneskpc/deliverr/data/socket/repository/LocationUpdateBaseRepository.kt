package com.kaaneneskpc.deliverr.data.socket.repository

import com.kaaneneskpc.deliverr.data.socket.SocketService


abstract class LocationUpdateBaseRepository (val socketService: SocketService)
{
    open val messages = socketService.messages
    abstract fun connect(orderID: String, riderID: String)
    abstract fun disconnect()
}