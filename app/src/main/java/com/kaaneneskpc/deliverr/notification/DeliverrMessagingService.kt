package com.kaaneneskpc.deliverr.notification

import android.app.PendingIntent
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kaaneneskpc.deliverr.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DeliverrMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var deliverrNotificationManager: DeliverrNotificationManager
    
    @Inject
    lateinit var notificationEventBus: NotificationEventBus
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        deliverrNotificationManager.updateToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent = Intent(this, MainActivity::class.java)
        val title = message.notification?.title ?: ""
        val messageText = message.notification?.body ?: ""
        val data = message.data
        val type = data["type"] ?: "general"

        if (type == "order") {
            val orderID = data[ORDER_ID]
            intent.putExtra(ORDER_ID, orderID)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationChannelType = when (type) {
            "order" -> DeliverrNotificationManager.NotificationChannelType.ORDER
            "general" -> DeliverrNotificationManager.NotificationChannelType.PROMOTION
            else -> DeliverrNotificationManager.NotificationChannelType.ACCOUNT
        }
        deliverrNotificationManager.showNotification(
            title, messageText, 13034, pendingIntent,
            notificationChannelType
        )
        
        // Bildirim olayını yayınla - bu, dinleyen ViewModel'lerin güncellemesini sağlayacak
        CoroutineScope(Dispatchers.IO).launch {
            notificationEventBus.publishEvent(NotificationEventBus.NotificationEvent.NewNotificationReceived)
        }
    }

    companion object {
        const val ORDER_ID = "orderId"
    }
}