package com.kaaneneskpc.deliverr.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kaaneneskpc.deliverr.DeliverrSession
import com.kaaneneskpc.deliverr.data.FoodApi
import com.kaaneneskpc.deliverr.data.location.LocationManager
import com.kaaneneskpc.deliverr.data.socket.SocketService
import com.kaaneneskpc.deliverr.data.socket.repository.SocketServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideClient(session: DeliverrSession, @ApplicationContext context: Context): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${session.getToken()}")
                .addHeader("X-Package-Name", context.packageName)
                .build()
            chain.proceed(request)
        }
        client.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        return client.build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl("http://192.168.120.95:8081")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideFoodApi(retrofit: Retrofit): FoodApi {
        return retrofit.create(FoodApi::class.java)
    }

    @Provides
    fun provideSession(@ApplicationContext context: Context): DeliverrSession {
        return DeliverrSession(context)
    }

    @Provides
    fun provideLocationService(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    fun provideLocationManager(
        fusedLocationProviderClient: FusedLocationProviderClient,
        @ApplicationContext context: Context
    ): LocationManager {
        return LocationManager(fusedLocationProviderClient, context)
    }

    @Provides
    fun provideSocketService(): SocketService {
        return SocketServiceImpl()
    }
}