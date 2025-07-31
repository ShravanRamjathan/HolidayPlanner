package com.holidayplanner.service

import com.holidayplanner.model.HolidayPlanner
import com.holidayplanner.model.Prompt
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CohereHoliday {

    @POST("/generate")
   suspend fun generateHolidayPlan(@Body prompt: Prompt): HolidayPlanner

    @POST("/holiday")
    fun holidayItinary()
}