package com.holidayplanner.service

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import retrofit2.Retrofit

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    fun CohereApi(retrofit: Retrofit): CohereHoliday {
        return retrofit.create(CohereHoliday::class.java)
    }
}