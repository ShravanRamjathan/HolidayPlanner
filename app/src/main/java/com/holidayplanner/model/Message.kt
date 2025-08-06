package com.holidayplanner.model

import java.time.LocalDateTime

data class Message(
    val message: String = "",
    val userCase: UseCase,
    val dateTime: LocalDateTime,
)
