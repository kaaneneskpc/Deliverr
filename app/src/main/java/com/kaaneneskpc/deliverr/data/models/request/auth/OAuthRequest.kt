package com.kaaneneskpc.deliverr.data.models.request.auth

data class OAuthRequest(
    val token: String,
    val provider: String
)