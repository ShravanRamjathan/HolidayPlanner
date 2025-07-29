package com.holidayplanner.service

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideCohereApi(retrofit: Retrofit): CohereHoliday {
        return retrofit.create(CohereHoliday::class.java)
    }
}