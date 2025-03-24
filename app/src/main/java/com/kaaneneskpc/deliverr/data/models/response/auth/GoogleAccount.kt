package com.kaaneneskpc.deliverr.data.models.response.auth

data class GoogleAccount(
    val token: String,
    val displayName: String,
    val profileImageUrl: String?
)