package com.kaaneneskpc.deliverr.util

object OrdersUtils {

    enum class OrderStatus {
        PENDING_ACCEPTANCE,
        ACCEPTED,
        PREPARING,
        READY,
        OUT_FOR_DELIVERY,
        DELIVERED,
        REJECTED,
        CANCELLED
    }
}