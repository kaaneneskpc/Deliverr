package com.kaaneneskpc.deliverr.di

import com.kaaneneskpc.deliverr.data.location.LocationManager
import com.kaaneneskpc.deliverr.data.socket.SocketService
import com.kaaneneskpc.deliverr.data.socket.repository.LocationUpdateBaseRepository
import com.kaaneneskpc.deliverr.data.socket.repository.LocationUpdateSocketRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FlavorModule{
    @Provides
    fun provideLocationUpdateSocketRepository(
        socketService: SocketService,
        locationManager: LocationManager
    ): LocationUpdateBaseRepository {
        return LocationUpdateSocketRepository(socketService, locationManager)
    }
}