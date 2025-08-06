package com.holidayplanner.service

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
    @Provides
    @Singleton
    fun returnRetroFit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://langlens-server.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
