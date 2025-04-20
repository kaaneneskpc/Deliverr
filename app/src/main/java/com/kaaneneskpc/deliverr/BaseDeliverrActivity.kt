package com.kaaneneskpc.deliverr

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.kaaneneskpc.deliverr.notification.DeliverrMessagingService

abstract class BaseDeliverrActivity: ComponentActivity() {
    val viewModel by viewModels<DeliverrViewModel>()
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        processIntent(intent, viewModel)
    }

    private fun processIntent(intent: Intent, viewModel: DeliverrViewModel) {
        if (intent.hasExtra(DeliverrMessagingService.ORDER_ID)) {
            val orderID = intent.getStringExtra(DeliverrMessagingService.ORDER_ID)
            viewModel.navigateToOrderDetail(orderID.orEmpty())
            intent.removeExtra(DeliverrMessagingService.ORDER_ID)
        }
    }
}