package com.kaaneneskpc.deliverr.data.models.response.notification

data class NotificationListResponse(
    val notifications: List<Notification>,
    val unreadCount: Int
)