package com.example.cointracker.di

import com.example.cointracker.common.Constants
import com.example.cointracker.data.remote.CoinLayerApi
import com.example.cointracker.data.repository.CoinRepositoryImpl
import com.example.cointracker.domain.repository.CoinRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCoinLayerApi() : CoinLayerApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinLayerApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCoinRepository(api: CoinLayerApi): CoinRepository {
        return CoinRepositoryImpl(api)
    }
}