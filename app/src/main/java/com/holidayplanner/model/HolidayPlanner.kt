package com.holidayplanner.model

import kotlinx.serialization.Serializable

@Serializable
data class HolidayPlanner(
    val text:String ="",
    val citations:List<String> = emptyList<String>()
)
