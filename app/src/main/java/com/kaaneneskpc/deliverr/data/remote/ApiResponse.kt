package com.kaaneneskpc.deliverr.data.remote

import retrofit2.Response

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val code: Int, val message: String) : ApiResponse<Nothing>() {
        fun formatMessage(): String {
            return "Error: $code - $message"
        }
    }
    data class Exception(val exception: kotlin.Exception) : ApiResponse<Nothing>()
}

suspend fun <T> safeApiCall(call: suspend () -> Response<T>): ApiResponse<T> {
    return try {
        val res = call.invoke()
        if(res.isSuccessful) {
            ApiResponse.Success(res.body()!!)
        } else {
            ApiResponse.Error(res.code(), res.errorBody()?.string() ?: "Internal Server Error")
        }
    } catch (e: Exception) {
        ApiResponse.Exception(e)
    }
}