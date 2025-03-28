package com.kaaneneskpc.deliverr.data.models.request.payment

data class ConfirmPaymentRequest(
    val paymentIntentId: String,
    val addressId: String
)