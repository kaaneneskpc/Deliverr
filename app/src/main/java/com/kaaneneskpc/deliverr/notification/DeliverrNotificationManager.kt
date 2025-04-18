package com.kaaneneskpc.deliverr.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.kaaneneskpc.deliverr.data.FoodApi
import com.kaaneneskpc.deliverr.data.models.request.notification.FCMRequest
import com.kaaneneskpc.deliverr.data.remote.ApiResponse
import com.kaaneneskpc.deliverr.data.remote.safeApiCall
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeliverrNotificationManager @Inject constructor(
    private val foodApi: FoodApi,
    @ApplicationContext val context: Context
) {
    private val notificationManager = NotificationManagerCompat.from(context)
    private val job = CoroutineScope(Dispatchers.IO + SupervisorJob())

    enum class NotificationChannelType(
        val id: String,
        val channelName: String,
        val channelDesc: String,
        val importance: Int,
    ) {
        ORDER(
            "1", "Order", "Order", NotificationManager.IMPORTANCE_HIGH
        ),
        PROMOTION(
            "2", "Promotion", "Promotion", NotificationManager.IMPORTANCE_DEFAULT
        ),
        ACCOUNT(
            "3", "Account", "Account", NotificationManager.IMPORTANCE_LOW
        )
    }

    fun createChannels() {
        NotificationChannelType.entries.forEach {
            val channel = NotificationChannelCompat.Builder(it.id, it.importance)
                .setDescription(it.channelDesc)
                .setName(it.channelName)
                .build()
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun getAndStoreToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                updateToken(it.result)
            }
        }
    }

    fun updateToken(token: String) {
        job.launch {
            val res = safeApiCall { foodApi.updateToken(FCMRequest(token)) }
            if (res is ApiResponse.Success) {
                Log.d("FCM_REQUEST", res.data.message)
            } else {
                Log.d("FCM_REQUEST", "FAILED $res")
            }
        }
    }

    fun showNotification(
        title: String,
        message: String,
        notificationID: Int,
        intent: PendingIntent,
        notificationChannelType: NotificationChannelType
    ) {

        val notification = NotificationCompat.Builder(context, notificationChannelType.id)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationID, notification)
    }

}