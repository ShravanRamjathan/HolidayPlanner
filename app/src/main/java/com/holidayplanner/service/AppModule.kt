package com.holidayplanner.service

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun returnRetroFit(): Retrofit {
        val client: OkHttpClient =
            OkHttpClient.Builder().readTimeout(1000, TimeUnit.MICROSECONDS ).writeTimeout(
                1000,
                TimeUnit.MILLISECONDS
            ).build()
        return Retrofit.Builder()
            .baseUrl("https://langlens-server.onrender.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
