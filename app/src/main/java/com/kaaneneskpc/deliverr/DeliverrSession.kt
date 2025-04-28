package com.kaaneneskpc.deliverr

import android.content.Context
import android.content.SharedPreferences

class DeliverrSession(val context: Context) {

    val preferences: SharedPreferences = context.getSharedPreferences("deliverr", Context.MODE_PRIVATE)

    fun storeToken(token: String) {
        preferences.edit().putString("token", token).apply()
    }

    fun getToken(): String? {
        preferences.getString("token", null)?.let {
            return it
        }
        return null
    }


    fun storeRestaurantId(restaurantId: String) {
        preferences.edit().putString("restaurantId", restaurantId).apply()
    }

    fun getRestaurantId(): String? {
        preferences.getString("restaurantId", null)?.let {
            return it
        }
        return null
    }

}