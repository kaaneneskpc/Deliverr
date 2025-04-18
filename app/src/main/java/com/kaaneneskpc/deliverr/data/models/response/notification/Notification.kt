package com.kaaneneskpc.deliverr.data.models.response.notification

data class Notification(
    val createdAt: String,
    val id: String,
    val isRead: Boolean,
    val message: String,
    val orderId: String,
    val title: String,
    val type: String,
    val userId: String
)