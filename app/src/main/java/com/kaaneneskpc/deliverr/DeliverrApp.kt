package com.kaaneneskpc.deliverr

import android.app.Application
import com.kaaneneskpc.deliverr.notification.DeliverrNotificationManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DeliverrApp: Application() {

    @Inject
    lateinit var notificationManager: DeliverrNotificationManager
    override fun onCreate() {
        super.onCreate()
        notificationManager.createChannels()
        notificationManager.getAndStoreToken()
    }

}