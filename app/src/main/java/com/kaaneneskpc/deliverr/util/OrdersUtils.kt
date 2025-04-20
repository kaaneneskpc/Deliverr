package com.kaaneneskpc.deliverr.util

object OrdersUtils {

    enum class OrderStatus {
        PENDING_ACCEPTANCE, // Initial state when order is placed
        ACCEPTED,          // Restaurant accepted the order
        PREPARING,         // Food is being prepared
        READY,            // Ready for delivery/pickup
        OUT_FOR_DELIVERY, // Rider picked up
        DELIVERED,        // Order completed
        REJECTED,         // Restaurant rejected the order
        CANCELLED         // Customer cancelled
    }
}