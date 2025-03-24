package com.kaaneneskpc.deliverr.data

import com.kaaneneskpc.deliverr.data.models.response.auth.AuthResponse
import com.kaaneneskpc.deliverr.data.models.request.auth.OAuthRequest
import com.kaaneneskpc.deliverr.data.models.request.auth.SignInRequest
import com.kaaneneskpc.deliverr.data.models.request.auth.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodApi {
    @POST("/auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthResponse>
    @POST("/auth/login")
    suspend fun signIn(@Body request: SignInRequest): Response<AuthResponse>
    @POST("/auth/oauth")
    suspend fun oAuth(@Body request: OAuthRequest): Response<AuthResponse>
}