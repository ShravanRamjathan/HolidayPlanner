package com.holidayplanner.service

import com.holidayplanner.model.HolidayPlanner
import retrofit2.http.Body
import retrofit2.http.POST

interface CohereHoliday {

    @POST("/generate")
    fun generateHolidayPlan(@Body prompt: String): HolidayPlanner

    @POST("/holiday")
    fun holidayItinary()
}