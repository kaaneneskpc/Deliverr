package com.kaaneneskpc.deliverr.data.models.request.auth

data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)
