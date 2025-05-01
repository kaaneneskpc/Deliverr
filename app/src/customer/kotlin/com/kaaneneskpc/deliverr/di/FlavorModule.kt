package com.kaaneneskpc.deliverr.di


import com.kaaneneskpc.deliverr.data.socket.SocketService
import com.kaaneneskpc.deliverr.data.socket.repository.CustomerLocationUpdateSocketRepository
import com.kaaneneskpc.deliverr.data.socket.repository.LocationUpdateBaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FlavorModule {
    @Provides
    fun provideLocationUpdateSocketRepository(
        socketService: SocketService,
    ): LocationUpdateBaseRepository {
        return CustomerLocationUpdateSocketRepository(socketService)
    }
}