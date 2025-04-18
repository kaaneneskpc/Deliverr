package com.kaaneneskpc.deliverr.notification

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Uygulama içinde bildirim olaylarını paylaşmak için kullanılan event bus.
 * FCM servisi ve ViewModel arasında iletişim sağlar.
 */
@Singleton
class NotificationEventBus @Inject constructor() {
    
    private val _events = MutableSharedFlow<NotificationEvent>(replay = 0)
    val events = _events.asSharedFlow()
    
    suspend fun publishEvent(event: NotificationEvent) {
        _events.emit(event)
    }
    
    sealed class NotificationEvent {
        object NewNotificationReceived : NotificationEvent()
    }
} 